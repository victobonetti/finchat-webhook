package br.com.kod3.services.formatters;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionType;
import br.com.kod3.models.util.SituacaoEnum;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class DebtFormatter implements Formatter {

    public String formatDebtReport(List<Debt> debts) {
        FormatedStringBuilder response = new FormatedStringBuilder();

        if (debts == null || debts.isEmpty()) {
            return "No debts registered.";
        }

        BigDecimal totalDebt = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;

        response.append("Debt Summary:");

        for (Debt debt : debts) {
            response.append(String.format(
                    "- %s: %.2f/%s %.2f (%s) | %s",
                    debt.getBusiness(),
                    debt.getPaidValue(),
                    debt.getCurrency(),
                    debt.getTotalValue(),
                    debt.getSituacao(),
                    debt.getId()
            ));
            totalDebt = totalDebt.add(debt.getTotalValue());
            totalPaid = totalPaid.add(debt.getPaidValue());
        }

        response.append("---");
        response.append(String.format("Total Debt: %.2f", totalDebt));
        response.append(String.format("Total Paid: %.2f", totalPaid));
        response.append(String.format("Remaining: %.2f", totalDebt.subtract(totalPaid)));

        return response.toString();
    }

    public String formatDebtDetailReport(Debt debt, List<Transaction> transactions) {
        FormatedStringBuilder response = new FormatedStringBuilder();

        response.append("Debt Detail:");
        response.append(String.format("- Business: %s", debt.getBusiness()));
        response.append(String.format("- Total Value: %.2f %s", debt.getTotalValue(), debt.getCurrency()));
        response.append(String.format("- Paid Value: %.2f", debt.getPaidValue()));
        response.append(String.format("- Category: %s", debt.getCategory()));
        response.append(String.format("- Status: %s", debt.getSituacao()));
        response.append(String.format("- Created At: %s", debt.getCreatedAt().toLocalDate().format(DATE_FORMATTER)));
        response.append("---");

        if (transactions == null || transactions.isEmpty()) {
            response.append("No payments have been made for this debt yet.");
            return response.toString();
        }

        BigDecimal paidFromTransactions = BigDecimal.ZERO;

        response.append("Payments:");
        for (Transaction tx : transactions) {
            if (tx.getType() == TransactionType.EXPENSE) {  // Consider payments as EXPENSE
                String date = tx.getCreatedAt().toLocalDate().format(DATE_FORMATTER);
                response.append(String.format("- [%s] %.2f at %s (%s)", date, tx.getValue(), tx.getBusiness(), tx.getCategory()));
                paidFromTransactions = paidFromTransactions.add(tx.getValue());
            }
        }

        response.append("---");
        response.append(String.format("Total Paid in Transactions: %.2f", paidFromTransactions));
        response.append(String.format("Remaining Balance: %.2f", debt.getTotalValue().subtract(paidFromTransactions)));

        return response.toString();
    }

}
