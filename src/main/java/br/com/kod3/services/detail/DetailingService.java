package br.com.kod3.services.detail;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.util.Detail;
import br.com.kod3.services.recorrencia.RecorrenciaService;
import br.com.kod3.services.debt.DebtService;
import br.com.kod3.services.detail.formatters.DebtFormatter;
import br.com.kod3.services.detail.formatters.RecorrenciaFormatter;
import br.com.kod3.services.detail.formatters.TransactionsFormatter;
import br.com.kod3.services.transaction.TransactionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class DetailingService {

    @Inject
    TransactionService transactionService;

    @Inject
    RecorrenciaService recorrenciaService;

    @Inject
    TransactionsFormatter tf;

    @Inject
    DebtService debtService;

    @Inject
    RecorrenciaFormatter rf;

    @Inject
    DebtFormatter df;

    public Detail getFormattedTransactions(String uid, LocalDate startDate, LocalDate endDate) {
        List<Transaction> userTransactions = transactionService.getTransactionsByUidAndPeriod(uid, startDate, endDate);

        if (userTransactions.isEmpty()) {
            return new Detail("No transactions found for the selected period.");
        }

        return new Detail(tf.formatTransactionReport(userTransactions));
    }

    public Detail getFormattedRecorrencias(String uid) {
        List<Recorrencia> userTransactions = recorrenciaService.getAllRecorrencias(uid);

        if (userTransactions.isEmpty()) {
            return new Detail("No transactions found for the selected period.");
        }

        return new Detail(rf.formatRecorrenciaReport(userTransactions));
    }

    public Detail getFormattedDebts(String uid) {
        List<Debt> userDebts = debtService.getDebitsFromUser(uid);

        return new Detail(df.formatDebtReport(userDebts));
    }


}
