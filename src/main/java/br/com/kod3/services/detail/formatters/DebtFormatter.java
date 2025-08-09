package br.com.kod3.services.detail.formatters;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.services.debt.DebtService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class DebtFormatter implements Formatter {

        @Inject
        DebtService debtService;

        /**
         * Gera um relatório completo para uma lista de dívidas, detalhando cada dívida
         * e seus pagamentos, seguido por um sumário geral.
         *
         * @param debts Uma lista de objetos Debt, onde cada um deve conter sua
         * própria lista de transações de pagamento.
         * @return Uma string com o relatório formatado.
         */
        public String formatDebtReport(List<Debt> debts) {
            if (debts == null || debts.isEmpty()) {
                return "Nenhuma dívida registrada.";
            }

            FormatedStringBuilder response = new FormatedStringBuilder();
            response.append("===== Relatório de Dívidas =====");

            // Processa cada dívida individualmente
            for (Debt debt : debts) {
                response.append(""); // Adiciona espaço para separar as seções
                response.append(String.format(
                        "[%s] %s: %.2f / %.2f %s",
                        debt.getSituacao(),
                        debt.getBusiness(),
                        debtService.getPaidValue(debt.getId(), debt.getUser().getId()),
                        debt.getTotalValue(),
                        debt.getCurrency()
                ));
                response.append(String.format(
                        "  - Categoria: %s | Criada em: %s",
                        debt.getCategory(),
                        debt.getCreatedAt().toLocalDate().format(DATE_FORMATTER)
                ));

                response.append(String.format(
                        "  - id: %s",
                        debt.getId()
                ));

                List<Transaction> payments = debt.getTransactions();
                if (payments.isEmpty()) {
                    response.append("  -> Nenhum pagamento realizado para esta dívida.");
                } else {
                    response.append("  -> Pagamentos Realizados:");
                    for (Transaction tx : payments) {
                        response.append(String.format(
                                "    - [%s] Pagamento de %.2f",
                                tx.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
                                tx.getValue()
                        ));
                    }
                }
            }
            return response.toString();
        }
    }


