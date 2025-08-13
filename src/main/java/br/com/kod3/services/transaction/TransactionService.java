package br.com.kod3.services.transaction;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.user.User;
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
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static br.com.kod3.services.util.CodigoDeResposta.*;
import static br.com.kod3.services.util.Messages.*;
import static br.com.kod3.services.util.Messages.registro_cancelado;

@ApplicationScoped
public class TransactionService implements FinchatHandler {

    private final DebtService debtService;
    private final StreakService streakService;
    private final RecurrenceService recurrenceService;
    private final TransactionRepository transactionRepository;

    @Inject
    public TransactionService(DebtService debtService, StreakService streakService, RecurrenceService recurrenceService, TransactionRepository transactionRepository) {
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

    public List<Transaction> getTransactionsByUidAndPeriod(String uid, LocalDate startDate, LocalDate endDate) {
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

                if (debt.getTotalValue().floatValue() < converted.getTransactionPayloadDto().getValue().floatValue() + debtService.getPaidValue(debt.getId(), user.getId()).floatValue()) {
                    evo.send(valor_excede_divida);
                    return ERRO_VALOR_EXCEDE_DIVIDA;
                }

                if (debt.getTotalValue().floatValue() == converted.getTransactionPayloadDto().getValue().floatValue() + debtService.getPaidValue(debt.getId(), user.getId()).floatValue()) {
                    evo.send(debito_quitado);
                    debtService.baixar(idDebt);
                }
            }

            if (idRecurrence != null) {
                recurrence = recurrenceService.getById(idRecurrence);
            }

            var shouldShowStreak = !streakService.hasTransactionToday(user.getId());

            if (converted.getTransactionPayloadDto().getType().equals(TransactionType.INCOME) && data.contains(confirma_entrada_com_bloqueio)) {
                blocked = true;
            }

            createOne(
                    TransactionConverter.toEntity(converted.getTransactionPayloadDto(), user, debt, recurrence, blocked), user.getId());
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
}
