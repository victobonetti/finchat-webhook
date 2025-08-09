package br.com.kod3.models.evolution.list;

import br.com.kod3.models.recorrencia.Recorrencia;

import static br.com.kod3.services.util.Messages.*;

import java.text.DecimalFormat;
import java.util.List;

public class EvolutionListFactory {

  private EvolutionListFactory() {}

  public static EvolutionListDto getTransactionList(Recorrencia r) {

    var rows = List.of(
            row("a", confirma_transacao),
            row("b", cancela_transacao)
    );

    return transactionList(r, rows);
  }

  private static List<EvolutionListDto.Section> sections(List<EvolutionListDto.Row> rows) {
    return List.of(
            EvolutionListDto.Section.builder()
            .title("section")
            .rows(rows)
            .build()
    );
  }

  private static EvolutionListDto.Row row(String id, String title){
    return EvolutionListDto.Row.builder()
            .rowId(id)
            .title(title)
            .build();
  }

  private static EvolutionListDto transactionList(Recorrencia r, List<EvolutionListDto.Row> rows) {
    DecimalFormat df = new DecimalFormat("0.00");

    String title = switch (r.getType()) {
      case RECORRENT_EXPENSE -> registrar_gasto;
      case RECORRENT_INCOME -> registrar_receita;
      default -> throw new RuntimeException("Erro ao gerar recorrência");
    };

    String description = """
            Descrição: %s
            Categoria: %s
            Valor: R$ %s
            Id da recorrência: %s
            """.formatted(
            r.getBusiness(),
            r.getCategory(),
            df.format(r.getValue()),
            r.getId()
    );

    return EvolutionListDto.builder()
            .number(r.getUser().getTelefone())
            .title(title)
            .description(description)
            .buttonText("Ver opções")
            .footerText("Escolha se deseja ou não registrar como uma transação")
            .sections(sections(rows))
            .build();
  }

}
