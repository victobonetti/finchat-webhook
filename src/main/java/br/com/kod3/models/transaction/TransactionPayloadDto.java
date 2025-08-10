package br.com.kod3.models.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.kod3.models.recurrence.PeriodEnum;
import br.com.kod3.models.util.enums.Category;
import br.com.kod3.models.util.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPayloadDto {
  private String userId;
  private String description;
  private BigDecimal value;
  private Category category;
  private TransactionType type;
  private String currency;
  private LocalDate date;
  private PeriodEnum period;
  private String idRecurrence;
  private String idDebt;
}
