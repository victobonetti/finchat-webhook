package br.com.kod3.services.detail;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.util.Detail;
import br.com.kod3.services.recurrence.RecurrenceService;
import br.com.kod3.services.debt.DebtService;
import br.com.kod3.services.detail.formatters.DebtFormatter;
import br.com.kod3.services.detail.formatters.RecurrenceFormatter;
import br.com.kod3.services.detail.formatters.TransactionsFormatter;
import br.com.kod3.services.transaction.TransactionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class DetailingService {

    private final TransactionService transactionService;
    private final RecurrenceService recurrenceService;
    private final DebtService debtService;
    private final TransactionsFormatter tf;
    private final RecurrenceFormatter rf;
    private final DebtFormatter df;

    @Inject
    public DetailingService(
            TransactionService transactionService,
            RecurrenceService recurrenceService,
            DebtService debtService,
            TransactionsFormatter tf,
            RecurrenceFormatter rf,
            DebtFormatter df
    ) {
        this.transactionService = transactionService;
        this.recurrenceService = recurrenceService;
        this.debtService = debtService;
        this.tf = tf;
        this.rf = rf;
        this.df = df;
    }

    public Detail getFormattedTransactions(String uid, LocalDate startDate, LocalDate endDate) {
        List<Transaction> userTransactions = transactionService.getTransactionsByUidAndPeriod(uid, startDate, endDate);

        if (userTransactions.isEmpty()) {
            return new Detail("No transactions found for the selected period.");
        }

        return new Detail(tf.formatTransactionReport(userTransactions));
    }

    public Detail getFormattedRecurrences(String uid) {
        List<Recurrence> userTransactions = recurrenceService.getAllRecurrences(uid);

        if (userTransactions.isEmpty()) {
            return new Detail("No transactions found for the selected period.");
        }

        return new Detail(rf.formatRecurrenceReport(userTransactions));
    }

    public Detail getFormattedDebts(String uid) {
        List<Debt> userDebts = debtService.getDebitsFromUser(uid);

        return new Detail(df.formatDebtReport(userDebts));
    }


}
