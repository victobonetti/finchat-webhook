package br.com.kod3.models.evolution.list;

import static br.com.kod3.services.Messages.*;

import java.util.List;

public class EvolutionListFactory {

  private EvolutionListFactory() {}

  public static EvolutionListDto getPerfilInvestidorPool(String phone) {
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
