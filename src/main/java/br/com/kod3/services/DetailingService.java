package br.com.kod3.services;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.util.Detail;
import br.com.kod3.services.formatters.DebtFormatter;
import br.com.kod3.services.formatters.RecorrenciaFormatter;
import br.com.kod3.services.formatters.TransactionsFormatter;
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

    public Detail getFormattedRecorrencias(String uid, String recorrenciaId) {
        Recorrencia r = recorrenciaService.getById(recorrenciaId);
        List<Transaction> t = transactionService.getTransactionsFromRecorrencia(recorrenciaId, uid);

        if (t.isEmpty()) {
            return new Detail("No transactions found for the selected period.");
        }

        return new Detail(rf.formatRecorrenciaDetailReport(r, t));
    }

    public Detail getFormattedDebts(String uid) {
        List<Debt> userDebts = debtService.getDebitsFromUser(uid);

        return new Detail(df.formatDebtReport(userDebts));
    }

    public Detail getFormattedDebts(String uid, String debtId) {
        List<Transaction> userTransactions = transactionService.getTransactionsFromDebt(uid, debtId);
        Debt debt = debtService.getDebtById(debtId);

        if (userTransactions.isEmpty()) {
            return new Detail("No transactions found for the selected period.");
        }

        return new Detail(df.formatDebtDetailReport(debt, userTransactions));
    }

}
