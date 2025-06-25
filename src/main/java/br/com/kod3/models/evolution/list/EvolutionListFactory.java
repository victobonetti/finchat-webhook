package br.com.kod3.models.evolution.list;

import java.util.List;

public class EvolutionListFactory {

  private EvolutionListFactory() {}

  public static EvolutionListDto getPerfilInvestidorPool(String phone) {
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
                                .title("ARROJADO")
                                .description("aaa")
                                .build(),
                            EvolutionListDto.Row.builder()
                                .rowId("b")
                                .title("ARROJADO")
                                .description("bbb")
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
