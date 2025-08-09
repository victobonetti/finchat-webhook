package br.com.kod3.models.evolution.list;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.TransactionType;

import static br.com.kod3.services.Messages.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class EvolutionListFactory {

  private EvolutionListFactory() {}

  public static EvolutionListDto getTransactionRegistryPoolFromRecorrencia(Recorrencia r) {

    DecimalFormat df = new DecimalFormat("0.00");

    String title = r.getType() == TransactionType.RECORRENT_EXPENSE ?
            registrar_gasto :
            r.getType() == TransactionType.RECORRENT_INCOME ?
                    registrar_receita : null;

    if (Objects.isNull(title)) {
      throw new RuntimeException("Erro ao gerar recorrência");
    }

    return EvolutionListDto.builder()
        .number(r.getUser().getTelefone())
        .title(title)
        .description(
                "Descrição: " + r.getBusiness() + "\n" +
                        "Categoria: " + r.getCategory() + "\n" +
                        "Valor: " + df.getCurrency() + df.format(r.getValue()) + "\n" +
                        "Id da recorrência: " + r.getId()
        )
        .buttonText("Ver opções")
        .footerText("Escolha se deseja ou não registrar como uma transação")
        .sections(
            List.of(
                EvolutionListDto.Section.builder()
                    .title("section")
                    .rows(
                        List.of(
                            EvolutionListDto.Row.builder()
                                .rowId("a")
                                .title(confirma_transacao)
                                .description(confirma_transacao_descricao)
                                .build(),
                            EvolutionListDto.Row.builder()
                                .rowId("b")
                                .title(cancela_transacao)
                                .description(cancela_transacao_descricao)
                                .build()))
                    .build()))
        .build();
  }
}
