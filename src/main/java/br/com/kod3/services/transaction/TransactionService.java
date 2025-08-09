package br.com.kod3.services.transaction;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.user.User;
import br.com.kod3.repositories.transaction.TransactionRepository;
import br.com.kod3.services.debt.DebtService;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.recorrencia.RecorrenciaService;
import br.com.kod3.services.streak.StreakService;
import br.com.kod3.services.util.CodigosDeResposta;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static br.com.kod3.services.util.CodigosDeResposta.*;
import static br.com.kod3.services.util.Messages.*;
import static br.com.kod3.services.util.Messages.registro_cancelado;

@ApplicationScoped
public class TransactionService {

  @Inject DebtService debtService;
  @Inject StreakService streakService;
  @Inject RecorrenciaService recorrenciaService;

  @Inject TransactionRepository transactionRepository;

  public List<Transaction> findMany() {
    return transactionRepository.findAll().stream().toList();
  }

  @CacheInvalidate(cacheName = "has-transaction-today")
  public void createOne(Transaction transaction, @CacheKey String userId) {
    transactionRepository.persistAndFlush(transaction);
    Log.info("Criada nova transação para o usuario" + userId + ": " + transaction);
  }

  public void createOneFromRecorrencia(Recorrencia recorrencia) {
    var transaction = TransactionConverter.fromRecorrencia(recorrencia);
    transactionRepository.persistAndFlush(transaction);
  }

  public List<Transaction> getTransactionsByUidAndPeriod(String uid, LocalDate startDate, LocalDate endDate){
    return transactionRepository.findByUserAndDateRange(uid, startDate, endDate);
  }

  public List<Transaction> getTransactionsFromRecorrencia(String recorrenciaId, String uid){
    return transactionRepository.find("recorrencia.id = ?1 and user.id = ?2", recorrenciaId, uid).list();
  }

  @Transactional
  public CodigosDeResposta handle(ConvertedDto converted, User user, EvolutionMessageSender evo){
    Objects.requireNonNull(converted.getTransactionPayloadDto());

    final String data = converted.getData().toLowerCase();

    if (data.contains(confirma_transacao)) {

      final var idRecorrencia = converted.getTransactionPayloadDto().getIdRecorrencia();

      Debt debt = null;
      Recorrencia recorrencia = null;

      if (idRecorrencia != null) {
        recorrencia = recorrenciaService.getById(idRecorrencia);
      }

      var shouldShowStreak = !streakService.hasTransactionToday(user.getId());

      createOne(
              TransactionConverter.toEntity(converted.getTransactionPayloadDto(), user, debt, recorrencia), user.getId());
      evo.send(registro_incluido);

      if (shouldShowStreak){
        streakService.handleStreak(user, evo);
      }

      return CONFIRMA_TRANSACAO;
    }

    if (data.contains(cancela_transacao)) {
      evo.send(registro_cancelado);
      return CANCELA_TRANSACAO;
    }

    return ERRO_INTERNO;
  }

  public BigDecimal getPaidValueFromDebt(String debtId, String uid) {
    return transactionRepository.getPaidValueFromDebt(debtId, uid);
  }
}
