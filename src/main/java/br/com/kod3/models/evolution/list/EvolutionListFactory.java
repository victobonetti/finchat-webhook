package br.com.kod3.models.evolution.list;

import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.transaction.TransactionType;

import static br.com.kod3.services.Messages.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class EvolutionListFactory {

  private EvolutionListFactory() {}

  public static EvolutionListDto getPerfilInvestidorList(String phone) {
    return EvolutionListDto.builder()
        .number(phone)
        .title(titulo_perfil_investidor)
        .description(descricao_perfil_investidor)
        .buttonText(botao_perfil_investidor)
        .footerText(rodape_perfil_investidor)
        .sections(
            List.of(
                EvolutionListDto.Section.builder()
                    .title("Perfis disponíveis")
                    .rows(
                        List.of(
                            EvolutionListDto.Row.builder()
                                .rowId("a")
                                .title(perfil_conservador_titulo)
                                .description(perfil_conservador_descricao)
                                .build(),
                            EvolutionListDto.Row.builder()
                                .rowId("b")
                                .title(perfil_moderado_titulo)
                                .description(perfil_moderado_descricao)
                                .build(),
                            EvolutionListDto.Row.builder()
                                .rowId("c")
                                .title(perfil_arrojado_titulo)
                                .description(perfil_arrojado_descricao)
                                .build()))
                    .build()))
        .build();
  }

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
