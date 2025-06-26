package br.com.kod3.models.evolution.list;

import br.com.kod3.services.Messages;

import java.util.List;

public class EvolutionListFactory {

  private EvolutionListFactory() {}

  public static EvolutionListDto getPerfilInvestidorPool(String phone, Messages messages) {
    return EvolutionListDto.builder()
            .number(phone)
            .title(messages.titulo_perfil_investidor())
            .description(messages.descricao_perfil_investidor())
            .buttonText(messages.botao_perfil_investidor())
            .footerText(messages.rodape_perfil_investidor())
            .sections(
                    List.of(
                            EvolutionListDto.Section.builder()
                                    .title("Perfis disponíveis")
                                    .rows(
                                            List.of(
                                                    EvolutionListDto.Row.builder()
                                                            .rowId("a")
                                                            .title(messages.perfil_conservador_titulo())
                                                            .description(messages.perfil_conservador_descricao())
                                                            .build(),
                                                    EvolutionListDto.Row.builder()
                                                            .rowId("b")
                                                            .title(messages.perfil_moderado_titulo())
                                                            .description(messages.perfil_moderado_descricao())
                                                            .build(),
                                                    EvolutionListDto.Row.builder()
                                                            .rowId("c")
                                                            .title(messages.perfil_arrojado_titulo())
                                                            .description(messages.perfil_arrojado_descricao())
                                                            .build()))
                                    .build()))
            .build();
  }


  public static EvolutionListDto getTransactionRegistryPool(String phone) {
    return EvolutionListDto.builder()
        .number(phone)
        .title("Pesquisa Perfil investidor...")
        .description("des")
        .buttonText("teste")
        .footerText("footer")
        .sections(
            List.of(
                EvolutionListDto.Section.builder()
                    .title("section")
                    .rows(
                        List.of(
                            EvolutionListDto.Row.builder()
                                .rowId("a")
                                .title("aa")
                                .description("aaa")
                                .build(),
                            EvolutionListDto.Row.builder()
                                .rowId("b")
                                .title("bb")
                                .description("bbb")
                                .build()))
                    .build()))
        .build();
  }
}
