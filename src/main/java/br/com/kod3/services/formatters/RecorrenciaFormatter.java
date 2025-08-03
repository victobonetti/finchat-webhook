package br.com.kod3.services.formatters;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecorrenciaFormatter implements Formatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String formatRecorrenciaReport(List<Recorrencia> recorrencias) {
        if (recorrencias == null || recorrencias.isEmpty()) {
            return "No recurring transactions found.";
        }

        Map<TransactionType, List<Recorrencia>> groupedByType = recorrencias.stream()
                .collect(Collectors.groupingBy(Recorrencia::getType, TreeMap::new, Collectors.toList()));

        BigDecimal totalExpenses = recorrencias.stream()
                .filter(r -> r.getType() == TransactionType.RECORRENT_EXPENSE)
                .map(Recorrencia::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSavings = recorrencias.stream()
                .filter(r -> r.getType() == TransactionType.RECORRENT_INCOME)
                .map(Recorrencia::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        FormatedStringBuilder response = new FormatedStringBuilder();

        groupedByType.forEach((type, list) -> {
            response.append(String.format("%s:", type == TransactionType.EXPENSE ? "Expenses" : "Savings"));

            list.forEach(r -> {
                response.append(String.format(
                        "- %s of %.2f (%s) scheduled every %s on %s [Category: %s, Currency: %s]",
                        r.getBusiness(),
                        r.getValue(),
                        type.name(),
                        r.getPeriod(),
                        r.getPaymentDay().format(DATE_FORMATTER),
                        r.getCategory(),
                        r.getCurrency()
                ));
            });

            response.append("");
        });

        response.append("---");
        response.append("Recurring Transaction Summary:");
        response.append(String.format("Total Recurring Expenses: %.2f", totalExpenses));
        response.append(String.format("Total Recurring Savings: %.2f", totalSavings));

        return response.toString();
    }

    public String formatRecorrenciaDetailReport(Recorrencia recorrencia, List<Transaction> transactions) {
        FormatedStringBuilder response = new FormatedStringBuilder();

        response.append("Recurring Transaction Details:");
        response.append(String.format("- Business: %s", recorrencia.getBusiness()));
        response.append(String.format("- Value: %.2f %s", recorrencia.getValue(), recorrencia.getCurrency()));
        response.append(String.format("- Type: %s", recorrencia.getType()));
        response.append(String.format("- Category: %s", recorrencia.getCategory()));
        response.append(String.format("- Payment Day: %s", recorrencia.getPaymentDay().format(DATE_FORMATTER)));
        response.append(String.format("- Periodicity: %s", recorrencia.getPeriod()));
        response.append("---");

        if (transactions == null || transactions.isEmpty()) {
            response.append("No transactions have been generated for this recurrence yet.");
            return response.toString();
        }

        BigDecimal total = BigDecimal.ZERO;

        response.append("Generated Transactions:");
        for (Transaction tx : transactions) {
            String date = tx.getCreatedAt().toLocalDate().format(DATE_FORMATTER);
            response.append(String.format("- [%s] %.2f at %s (%s)", date, tx.getValue(), tx.getBusiness(), tx.getCategory()));
            total = total.add(tx.getValue());
        }

        response.append("---");
        response.append(String.format("Total Generated from Recurrence: %.2f", total));

        return response.toString();
    }

}