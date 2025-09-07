package br.com.kod3.services.detail.formatters;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.util.enums.Category;
import br.com.kod3.models.util.enums.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransactionsFormatter implements Formatter {

  private static final String CURRENCY_SYMBOL = "R$";
  private static final DateTimeFormatter DATE_FORMATTER_PT_BR =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  // Nome dos meses em PT-BR
  private static final String[] MESES_PT_BR = {
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
  };

  public String formatTransactionReport(
      List<Transaction> transactions, Integer allTimeTotalSpent, Integer allTimeTotalSaved) {
    BigDecimal periodTotalSpent = BigDecimal.ZERO;
    BigDecimal periodTotalSaved = BigDecimal.ZERO;

    Map<LocalDate, List<Transaction>> groupedByDate = new TreeMap<>(Collections.reverseOrder());
    Map<YearMonth, Map<Category, BigDecimal>> monthlyCategorySpending =
        new TreeMap<>(Collections.reverseOrder());

    // Agrupa por data
    for (Transaction tx : transactions) {
      LocalDate transactionDate = tx.getCreatedAt().toLocalDate();
      groupedByDate.computeIfAbsent(transactionDate, k -> new ArrayList<>()).add(tx);

      Category category = tx.getCategory();

      if (tx.getType() == TransactionType.EXPENSE) {
        periodTotalSpent = periodTotalSpent.add(tx.getValue());
        YearMonth month = YearMonth.from(transactionDate);
        monthlyCategorySpending
            .computeIfAbsent(month, k -> new TreeMap<>())
            .merge(category, tx.getValue(), BigDecimal::add);
      } else {
        periodTotalSaved = periodTotalSaved.add(tx.getValue());
      }
    }

    return buildResponseList(
        periodTotalSpent,
        periodTotalSaved,
        groupedByDate,
        monthlyCategorySpending,
        allTimeTotalSpent,
        allTimeTotalSaved);
  }

  private String buildResponseList(
      BigDecimal totalSpent,
      BigDecimal totalSaved,
      Map<LocalDate, List<Transaction>> dailyTransactions,
      Map<YearMonth, Map<Category, BigDecimal>> monthlySpending,
      Integer allTimeTotalSpent,
      Integer allTimeTotalSaved) {

    StringBuilder response = new StringBuilder();
    response.append("📄 *Resumo de Transações*\n");

    dailyTransactions.forEach(
        (date, txList) -> {
          response.append("\n📅 *").append(date.format(DATE_FORMATTER_PT_BR)).append("*\n");

          // Agrupa primeiro por tipo (despesa/receita), depois por categoria
          Map<TransactionType, Map<Category, List<Transaction>>> byType =
              txList.stream()
                  .sorted(Comparator.comparing(Transaction::getCategory))
                  .collect(
                      Collectors.groupingBy(
                          Transaction::getType,
                          LinkedHashMap::new,
                          Collectors.groupingBy(
                              Transaction::getCategory, LinkedHashMap::new, Collectors.toList())));

          byType.forEach(
              (type, categoryMap) -> {
                String tituloTipo = type == TransactionType.EXPENSE ? "💸 Gastos" : "💰 Receitas";
                response.append("\n").append(tituloTipo).append("\n");

                categoryMap.forEach(
                    (category, transactions) -> {
                      response.append("   📂 ").append(category).append("\n");

                      boolean firstOfType = true;
                      for (Transaction tx : transactions) {
                        String symbol = "";
                        if (firstOfType) {
                          symbol =
                              type == TransactionType.EXPENSE ? "\uD83D\uDCC9" : "\uD83D\uDCC8";
                          firstOfType = false;
                        }

                        String status = "";
                        if (tx.getType() == TransactionType.INCOME && tx.getBlocked()) {
                          status = " (Bloqueado)";
                          if (firstOfType) {
                            symbol = "🔒";
                            firstOfType = false;
                          }
                        }

                        response.append(
                            String.format(
                                "      %s %s%.2f - %s%s\n",
                                symbol,
                                CURRENCY_SYMBOL,
                                tx.getValue(),
                                tx.getDescription(),
                                status));
                      }
                    });
              });
        });

    // Resumo do período
    response.append("\n════════════\n");
    response.append("📊 *Resumo do Período*\n");
    response.append(String.format("💸 Total Gasto: *%s%.2f*\n", CURRENCY_SYMBOL, totalSpent));
    response.append(String.format("💰 Total Recebido: *%s%.2f*\n", CURRENCY_SYMBOL, totalSaved));
    response.append(
        String.format(
            "🧾 Saldo líquido: *%s%.2f*\n", CURRENCY_SYMBOL, totalSaved.subtract(totalSpent)));

    // Resumo mensal por categoria
    if (!monthlySpending.isEmpty()) {
      response.append("\n📂 *Gastos por Categoria/Mês*\n");
      monthlySpending.forEach(
          (month, categories) -> {
            String mesNome = MESES_PT_BR[month.getMonthValue() - 1];
            response.append("📅 ").append(mesNome).append("/").append(month.getYear()).append("\n");

            BigDecimal monthTotal =
                categories.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

            categories.forEach(
                (category, amount) ->
                    response.append(
                        String.format("   - %s: *%s%.2f*\n", category, CURRENCY_SYMBOL, amount)));
            response.append(
                String.format("   📊 Total do mês: *%s%.2f*\n", CURRENCY_SYMBOL, monthTotal));
          });
    }

    // ---
    // Cálculo do total de receitas bloqueadas do período
    BigDecimal allTimeBlockedIncome =
        dailyTransactions.values().stream()
            .flatMap(List::stream)
            .filter(tx -> tx.getType() == TransactionType.INCOME && tx.getBlocked())
            .map(Transaction::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Converte os valores para BigDecimal para o cálculo
    BigDecimal allTimeTotalSpentBD = BigDecimal.valueOf(allTimeTotalSpent);
    BigDecimal allTimeTotalSavedBD = BigDecimal.valueOf(allTimeTotalSaved);

    // Saldo líquido geral
    BigDecimal allTimeNetBalance =
        allTimeTotalSavedBD.subtract(allTimeBlockedIncome).subtract(allTimeTotalSpentBD);

    // Total consolidado
    response.append("\n🏆 *Total Consolidado (Todos os meses)*\n");

    if (allTimeBlockedIncome.compareTo(BigDecimal.ZERO) > 0) {
      response.append(
          String.format(
              "❌ Entradas Bloqueadas: *%s%.2f*\n", CURRENCY_SYMBOL, allTimeBlockedIncome));
    }

    response.append(String.format("💸 Total Gasto: *%s%d*\n", CURRENCY_SYMBOL, allTimeTotalSpent));
    response.append(
        String.format("💰 Total Recebido: *%s%d*\n", CURRENCY_SYMBOL, allTimeTotalSaved));
    response.append(
        String.format("🧾 **SEU SALDO REAL**: *%s%.2f*\n", CURRENCY_SYMBOL, allTimeNetBalance));

    return response.toString();
  }
}
