package br.com.kod3.services.detail.formatters;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.services.debt.DebtService;
import br.com.kod3.models.util.enums.SituacaoEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class DebtFormatter implements Formatter {

    private final DebtService debtService;

    // Formato de data para PT-BR
    private static final DateTimeFormatter DATE_FORMATTER_PT_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Inject
    public DebtFormatter(DebtService debtService) {
        this.debtService = debtService;
    }

    /**
     * Gera um relatório completo para uma lista de dívidas, detalhando cada dívida
     * e seus pagamentos, seguido por um sumário geral.
     *
     * @param debts Uma lista de objetos Debt, onde cada um deve conter sua
     *              própria lista de transações de pagamento.
     * @param showId true para exibir o ID, false para ocultar.
     * @return Uma string com o relatório formatado.
     */
    public String formatDebtReport(List<Debt> debts, boolean showId) {
        if (debts == null || debts.isEmpty()) {
            return "📄 *Relatório de Dívidas*\nNenhuma dívida registrada.";
        }

        StringBuilder response = new StringBuilder();
        response.append("📄 *Relatório de Dívidas*\n");

        for (Debt debt : debts) {
            String statusEmoji;
            switch (debt.getSituacao()) {
                case SituacaoEnum.INATIVO -> statusEmoji = "✅";
                case SituacaoEnum.ATIVO -> statusEmoji = "⚠️";
                default -> statusEmoji = "ℹ️";
            }

            BigDecimal pago = debtService.getPaidValue(debt.getId(), debt.getUser().getId());
            BigDecimal total = debt.getTotalValue();
            String currencySymbol = debt.getCurrency(); // Pode ser "R$" ou "$"

            // Barra de progresso
            String progressBar = generateProgressBar(pago, total, 10);

            response.append("\n────────────\n");
            response.append(String.format("%s *%s*\n", statusEmoji, debt.getDescription()));
            response.append(String.format("💰 Pago: *%s%.2f* / Total: *%s%.2f*\n",
                    currencySymbol, pago, currencySymbol, total));
            response.append(String.format("📊 %s\n", progressBar));
            response.append(String.format("📂 Categoria: _%s_\n", debt.getCategory()));
            response.append(String.format("📅 Criada: %s\n",
                    debt.getCreatedAt().toLocalDate().format(DATE_FORMATTER_PT_BR)));

            if (showId) {
                response.append(String.format("🆔 %s\n", debt.getId()));
            }

            List<Transaction> payments = debt.getTransactions();
            if (payments.isEmpty()) {
                response.append("➡️ Nenhum pagamento realizado.\n");
            } else {
                response.append("💳 *Pagamentos:*\n");
                for (Transaction tx : payments) {
                    response.append(String.format(
                            "   • %s → %s%.2f\n",
                            tx.getCreatedAt().toLocalDate().format(DATE_FORMATTER_PT_BR),
                            tx.getCurrency(), tx.getValue()
                    ));
                }
            }
        }
        return response.toString();
    }

    /**
     * Gera uma barra de progresso visual usando blocos.
     *
     * @param pago Valor já pago.
     * @param total Valor total da dívida.
     * @param length Quantidade de blocos da barra.
     * @return String com a barra de progresso.
     */
    private String generateProgressBar(BigDecimal pago, BigDecimal total, int length) {
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            return "⚠️ Sem valor total definido.";
        }
        double percent = pago.divide(total, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        int filled = (int) Math.round(percent * length);
        String bar = "█".repeat(Math.max(0, filled)) + "░".repeat(Math.max(0, length - filled));
        return String.format("[%s] %.0f%%", bar, percent * 100);
    }
}
