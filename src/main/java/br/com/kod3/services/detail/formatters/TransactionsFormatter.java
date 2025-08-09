package br.com.kod3.services.detail.formatters;

import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ApplicationScoped
public class TransactionsFormatter implements Formatter {

    public String formatTransactionReport(List<Transaction> transactions) {
        BigDecimal periodTotalSpent = BigDecimal.ZERO;
        BigDecimal periodTotalSaved = BigDecimal.ZERO;

        Map<LocalDate, DailySummary> groupedByDate = new TreeMap<>(Collections.reverseOrder());
        Map<YearMonth, Map<Category, BigDecimal>> monthlyCategorySpending = new TreeMap<>(Collections.reverseOrder());

        for (Transaction tx : transactions) {
            LocalDate transactionDate = tx.getCreatedAt().toLocalDate();

            DailySummary dailySummary = groupedByDate.computeIfAbsent(transactionDate, k -> new DailySummary());

            Category category = tx.getCategory();
            String detail = String.format("%.2f in %s (%s)", tx.getValue(), tx.getBusiness(), category);

            if (tx.getType() == TransactionType.EXPENSE) {
                periodTotalSpent = periodTotalSpent.add(tx.getValue());
                dailySummary.addExpense(tx.getValue(), detail);

                YearMonth month = YearMonth.from(transactionDate);
                monthlyCategorySpending
                        .computeIfAbsent(month, k -> new TreeMap<>())
                        .merge(category, tx.getValue(), BigDecimal::add);

            } else {
                periodTotalSaved = periodTotalSaved.add(tx.getValue());
                dailySummary.addSaving(tx.getValue(), detail);
            }
        }

        return buildResponseList(periodTotalSpent, periodTotalSaved, groupedByDate, monthlyCategorySpending);
    }

    private String buildResponseList(BigDecimal totalSpent, BigDecimal totalSaved, Map<LocalDate, DailySummary> dailySummaries, Map<YearMonth, Map<Category, BigDecimal>> monthlySpending) {
        FormatedStringBuilder response = new FormatedStringBuilder();

        dailySummaries.forEach((date, summary) -> {
            response.append(String.format("On %s:", date.format(DATE_FORMATTER)));
            if (!summary.spentDetails.isEmpty()) {
                response.append(String.format("- spent a total of %.2f on: %s.", summary.totalSpent, String.join(", ", summary.spentDetails)));
            }
            if (!summary.savedDetails.isEmpty()) {
                response.append(String.format("- saved a total of %.2f from: %s.", summary.totalSaved, String.join(", ", summary.savedDetails)));
            }
        });

        response.append("---");
        response.append("Period Summary:");
        response.append(String.format("Total Spent: %.2f", totalSpent));
        response.append(String.format("Total Saved: %.2f", totalSaved));

        if (!monthlySpending.isEmpty()) {
            response.append("---");
            response.append("Category Spending Summary by Month:");
            monthlySpending.forEach((month, categories) -> {
                response.append(String.format("For %s:", month.format(MONTH_FORMATTER)));
                BigDecimal monthTotal = categories.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

                categories.forEach((category, amount) ->
                        response.append(String.format("- %s: %.2f", category, amount))
                );

                response.append("--------------------");
                response.append(String.format("  Month Total: %.2f", monthTotal));
            });
        }

        return response.toString();
    }
}
