package br.com.kod3.services.transaction;

import static br.com.kod3.services.util.CodigoDeResposta.*;
import static br.com.kod3.services.util.Messages.*;
import static br.com.kod3.services.util.Messages.registro_cancelado;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.transaction.*;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.PaginatedResponse;
import br.com.kod3.models.util.enums.TransactionType;
import br.com.kod3.repositories.transaction.TransactionRepository;
import br.com.kod3.services.debt.DebtService;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.recurrence.RecurrenceService;
import br.com.kod3.services.streak.StreakService;
import br.com.kod3.services.util.CodigoDeResposta;
import br.com.kod3.services.util.FinchatHandler;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class TransactionService implements FinchatHandler {

  private final DebtService debtService;
  private final StreakService streakService;
  private final RecurrenceService recurrenceService;
  private final TransactionRepository transactionRepository;

  @Inject
  public TransactionService(
      DebtService debtService,
      StreakService streakService,
      RecurrenceService recurrenceService,
      TransactionRepository transactionRepository) {
    this.debtService = debtService;
    this.streakService = streakService;
    this.recurrenceService = recurrenceService;
    this.transactionRepository = transactionRepository;
  }

  @CacheInvalidate(cacheName = "has-transaction-today")
  public void createOne(Transaction transaction, @CacheKey String userId) {
    transactionRepository.persistAndFlush(transaction);
    Log.info("Criada nova transação para o usuario" + userId + ": " + transaction);
  }

  public List<Transaction> getTransactionsByUidAndPeriod(
      String uid, LocalDate startDate, LocalDate endDate) {
    return transactionRepository.findByUserAndDateRange(uid, startDate, endDate);
  }

  @Transactional
  @Override
  public CodigoDeResposta handle(ConvertedDto converted, User user, EvolutionMessageSender evo) {
    Objects.requireNonNull(converted.getTransactionPayloadDto());

    final String data = converted.getData().toLowerCase();

    if (data.contains(confirma_transacao)) {

      final var idRecurrence = converted.getTransactionPayloadDto().getIdRecurrence();
      final var idDebt = converted.getTransactionPayloadDto().getIdDebt();

      Debt debt = null;
      Recurrence recurrence = null;
      boolean blocked = false;

      if (idDebt != null) {
        debt = debtService.getDebtById(idDebt);

        if (debt.getTotalValue().floatValue()
            < converted.getTransactionPayloadDto().getValue().floatValue()
                + debtService.getPaidValue(debt.getId(), user.getId()).floatValue()) {
          evo.send(valor_excede_divida);
          return ERRO_VALOR_EXCEDE_DIVIDA;
        }

        if (debt.getTotalValue().floatValue()
            == converted.getTransactionPayloadDto().getValue().floatValue()
                + debtService.getPaidValue(debt.getId(), user.getId()).floatValue()) {
          evo.send(debito_quitado);
          debtService.baixar(idDebt);
        }
      }

      if (idRecurrence != null) {
        recurrence = recurrenceService.getById(idRecurrence);
      }

      var shouldShowStreak = !streakService.hasTransactionToday(user.getId());

      if (converted.getTransactionPayloadDto().getType().equals(TransactionType.INCOME)
          && data.contains(confirma_entrada_com_bloqueio)) {
        blocked = true;
      }

      createOne(
          TransactionConverter.toEntity(
              converted.getTransactionPayloadDto(), user, debt, recurrence, blocked),
          user.getId());
      evo.send(registro_incluido);

      if (shouldShowStreak) {
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

  public BigDecimal sumExpenses(String uid) {
    var tot = transactionRepository.sumValueByUserIdAndType(uid, TransactionType.EXPENSE);

    if (tot == null) {
      return BigDecimal.ZERO;
    }

    return tot;
  }

  public BigDecimal sumIncomes(String uid) {
    var tot = transactionRepository.sumValueByUserIdAndType(uid, TransactionType.INCOME);

    if (tot == null) {
      return BigDecimal.ZERO;
    }

    return tot;
  }

  public PaginatedResponse<TransactionResponseDto> getTransactions(
      @Valid TransactionsParams param, String uid) {
    final PanacheQuery<Transaction> query;
    final List<TransactionType> types = new ArrayList<>();

    if (param.findExpenses()) {
      types.add(TransactionType.EXPENSE);
    }

    if (param.findIncomes()) {
      types.add(TransactionType.INCOME);
    }

    if (param.dataIni() != null && param.dataFim() != null) {
      if (param.showBlocked() && !param.showNonBlocked()) {
        query =
            transactionRepository.find(
                "user.id = ?1 and createdAt > ?2 and createdAt <= ?3 and type in ?4 and blocked = true",
                uid,
                param.dataIni().atStartOfDay(),
                param.dataFim().atTime(23, 59, 59),
                types);
      } else if (!param.showBlocked() && param.showNonBlocked()) {
        query =
            transactionRepository.find(
                "user.id = ?1 and createdAt > ?2 and createdAt <= ?3 and type in ?4 and blocked = false",
                uid,
                param.dataIni().atStartOfDay(),
                param.dataFim().atTime(23, 59, 59),
                types);
      } else {
        query =
            transactionRepository.find(
                "user.id = ?1 and createdAt > ?2 and createdAt <= ?3 and type in ?4",
                uid,
                param.dataIni().atStartOfDay(),
                param.dataFim().atTime(23, 59, 59),
                types);
      }
    } else {
      query = transactionRepository.find("user.id = ?1 and type in ?2", uid, types);
    }

    var listResponse =
        query.page(param.pageIndex(), param.pageSize()).stream()
            .map(TransactionMapper::fromEntity)
            .toList();

    var count = query.count();

    return new PaginatedResponse<>(listResponse, param.pageIndex(), listResponse.size(), count);
  }
}
