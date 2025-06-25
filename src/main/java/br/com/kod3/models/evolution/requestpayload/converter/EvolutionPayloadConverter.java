package br.com.kod3.models.evolution.requestpayload.converter;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.transaction.TransactionType;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

@ApplicationScoped
public class EvolutionPayloadConverter {

  public ConvertedDto parse(WebhookBodyDto dto) {
    MessageType type = dto.data().messageType();
    var builder =
        ConvertedDto.builder().type(type).telefone(dto.data().key().remoteJid().split("@")[0]);

    if (type.equals(MessageType.audioMessage) || type.equals(MessageType.imageMessage)) {
      return builder.data(dto.data().message().base64()).build();
    }

    if (type.equals(MessageType.conversation)) {
      return builder.data(dto.data().message().conversation()).build();
    }

    if (type.equals(MessageType.listResponseMessage)) {
      builder.data(dto.data().message().listResponseMessage().singleSelectReply().selectedRowId());

      try {
        var q = dto.data().contextInfo().quotedMessage().listMessage().description().split("\n");
        var currencyAndVal = q[2].replace("Valor: ", "");

        var transactionType =
            dto.data()
                    .message()
                    .listResponseMessage()
                    .contextInfo()
                    .quotedMessage()
                    .listMessage()
                    .title()
                    .replace("Registrar ", "")
                    .equalsIgnoreCase("gasto")
                ? TransactionType.EXPENSE
                : TransactionType.INCOME;

        var transactionDto =
            TransactionPayloadDto.builder()
                .business(q[0])
                .category(Category.fromDescricao(q[1].replace("Categoria: ", "")))
                .value(new BigDecimal(currencyAndVal.replaceAll("[^\\d.,]+", "")))
                .currency(currencyAndVal.replaceAll("[\\d\\s.,]+", ""))
                .type(transactionType)
                .build();

        builder.transactionPayloadDto(transactionDto);

      } catch (Exception e) {
        Log.info("Não foi encontrado payload de transação compatível;");
      }

      return builder.build();
    }

    throw new RuntimeException("Erro ao converter webhook.");
  }
}
