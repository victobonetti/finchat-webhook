package br.com.kod3.models.evolution.requestpayload.converter;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConvertedDto {
  @NotBlank private MessageType type;
  @NotBlank private String telefone;
  @NotBlank private String data;
  @Nullable private TransactionPayloadDto transactionPayloadDto;
}
