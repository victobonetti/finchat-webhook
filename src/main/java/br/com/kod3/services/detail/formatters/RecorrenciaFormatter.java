package br.com.kod3.services.detail.formatters;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class RecorrenciaFormatter implements Formatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Generates a comprehensive report for a list of recurrences, detailing each
     * recurrence and its associated transactions, followed by an overall summary.
     *
     * @param recorrencias A list of Recorrencia objects, where each is expected
     * to contain its own list of associated transactions.
     * @return A formatted string report.
     */
    public String formatRecorrenciaReport(List<Recorrencia> recorrencias) {
        if (recorrencias == null || recorrencias.isEmpty()) {
            return "No recurring transactions to report.";
        }

        FormatedStringBuilder response = new FormatedStringBuilder();
        response.append("===== Recurring Transactions Report =====");

        // Process each recurrence and its own transactions
        for (Recorrencia rec : recorrencias) {
            response.append(""); // Add a space before the new section
            response.append(String.format(
                    "[%s] %s: %.2f %s every %s",
                    rec.getType().name().replace("RECORRENT_", ""), // More concise type name
                    rec.getBusiness(),
                    rec.getValue(),
                    rec.getCurrency(),
                    rec.getPeriod()
            ));
            response.append(String.format(
                    "  - Category: %s | Payment Day: %s",
                    rec.getCategory(),
                    rec.getPaymentDay().format(DATE_FORMATTER)
            ));

            List<Transaction> transactions = rec.getTransactions();
            if (transactions == null || transactions.isEmpty()) {
                response.append("  -> No transactions generated for this recurrence yet.");
            } else {
                response.append("  -> Generated Transactions:");

                // Calculate total for this specific recurrence's transactions
                BigDecimal totalForThisRecurrence = transactions.stream()
                        .map(Transaction::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                for (Transaction tx : transactions) {
                    response.append(String.format(
                            "    - [%s] %.2f",
                            tx.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
                            tx.getValue()
                    ));
                }
                response.append(String.format("  -> Subtotal: %.2f", totalForThisRecurrence));
            }
            response.append("---");
        }

        // Calculate overall summary totals based on the scheduled values of recurrences
        BigDecimal totalScheduledExpenses = recorrencias.stream()
                .filter(r -> r.getType() == TransactionType.RECORRENT_EXPENSE)
                .map(Recorrencia::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalScheduledIncome = recorrencias.stream()
                .filter(r -> r.getType() == TransactionType.RECORRENT_INCOME)
                .map(Recorrencia::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Final Summary Section
        response.append("");
        response.append("===== Overall Scheduled Summary =====");
        response.append(String.format("Total Scheduled Monthly Expenses: %.2f", totalScheduledExpenses));
        response.append(String.format("Total Scheduled Monthly Income: %.2f", totalScheduledIncome));
        response.append(String.format("Projected Net: %.2f", totalScheduledIncome.subtract(totalScheduledExpenses)));

        return response.toString();
    }

}