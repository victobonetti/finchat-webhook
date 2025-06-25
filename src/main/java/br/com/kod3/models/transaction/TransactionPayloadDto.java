package br.com.kod3.models.transaction;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionPayloadDto {
  private String userId;
  private String business;
  private BigDecimal value;
  private Category category;
  private TransactionType type;
  private String currency;
}
