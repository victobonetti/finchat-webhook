package br.com.kod3.services.detail.formatters;

import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.util.enums.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class RecurrenceFormatter implements Formatter {

  private static final DateTimeFormatter DATE_FORMATTER_PT_BR =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");

  /**
   * Gera um relatório de recorrências no estilo do DebtFormatter.
   *
   * @param recurrences Lista de recorrências
   * @return Relatório formatado
   */
  public String formatRecurrenceReport(List<Recurrence> recurrences) {
    if (recurrences == null || recurrences.isEmpty()) {
      return "📄 *Relatório de Recorrências*\nNenhuma recorrência registrada.";
    }

    StringBuilder response = new StringBuilder();
    response.append("📄 *Relatório de Recorrências*\n");

    for (Recurrence rec : recurrences) {
      String tipo = rec.getType() == TransactionType.RECURRING_EXPENSE ? "Despesa" : "Receita";
      String tipoIcon = rec.getType() == TransactionType.RECURRING_EXPENSE ? "🔻" : "🔼";

      BigDecimal valor = rec.getValue();
      String moeda = rec.getCurrency();

      response.append("\n────────────\n");
      response.append(String.format("%s *%s*\n", tipoIcon, rec.getDescription()));
      response.append(String.format("💰 Valor: *%s%.2f* / %s\n", moeda, valor, rec.getPeriod()));
      response.append(String.format("📂 Categoria: _%s_\n", rec.getCategory()));
      response.append(
          String.format(
              "📅 Dia de Pagamento: %s\n", rec.getPaymentDay().format(DATE_FORMATTER_PT_BR)));
      response.append(String.format("📌 Tipo: _%s_\n", tipo));

      List<Transaction> transactions = rec.getTransactions();
      if (transactions == null || transactions.isEmpty()) {
        response.append("➡️ Nenhuma transação gerada.\n");
      } else {
        BigDecimal subtotal = BigDecimal.ZERO;
        response.append("💳 *Transações:*\n");
        for (Transaction tx : transactions) {
          subtotal = subtotal.add(tx.getValue());
          response.append(
              String.format(
                  "   • %s → %s%.2f\n",
                  tx.getCreatedAt().toLocalDate().format(DATE_FORMATTER_PT_BR),
                  tx.getCurrency(),
                  tx.getValue()));
        }
        response.append(String.format("📊 Subtotal: *%s%.2f*\n", moeda, subtotal));
      }
    }

    // Resumo geral
    BigDecimal totalExpenses =
        recurrences.stream()
            .filter(r -> r.getType() == TransactionType.RECURRING_EXPENSE)
            .map(Recurrence::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalIncome =
        recurrences.stream()
            .filter(r -> r.getType() == TransactionType.RECURRING_INCOME)
            .map(Recurrence::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    response.append("\n════════════\n");
    response.append("📊 *Resumo Geral*\n");
    response.append(String.format("🔻 Despesas Mensais: *R$%.2f*\n", totalExpenses));
    response.append(String.format("🔼 Receitas Mensais: *R$%.2f*\n", totalIncome));
    response.append(
        String.format("🧾 Saldo Projetado: *R$%.2f*\n", totalIncome.subtract(totalExpenses)));

    return response.toString();
  }
}
