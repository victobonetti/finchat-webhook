package br.com.kod3.models.evolution.list;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EvolutionListDto {
  private String number;
  private String title;
  private String description;
  private String buttonText;
  private String footerText;
  private List<Section> sections;

  @Getter
  @Builder
  static class Section {
    private String title;
    private List<Row> rows;
  }

  @Getter
  @Builder
  static class Row {
    private String title;
    private String description;
    private String rowId;
  }
}
