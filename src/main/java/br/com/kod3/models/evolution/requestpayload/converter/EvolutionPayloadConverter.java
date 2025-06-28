package br.com.kod3.models.evolution.requestpayload.converter;

import static br.com.kod3.services.Messages.registrar_gasto;
import static br.com.kod3.services.Messages.registrar_receita;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.transaction.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Objects;

@ApplicationScoped
public class EvolutionPayloadConverter {

  public ConvertedDto parse(WebhookBodyDto dto) {
    MessageType type = dto.data().messageType();
    var builder =
        ConvertedDto.builder()
            .messageId(dto.data().key().id())
            .type(type)
            .telefone(dto.data().key().remoteJid().split("@")[0])
            .remoteJid(dto.data().key().remoteJid());

    if (type.equals(MessageType.audioMessage)) {
      return builder.data(dto.data().message().base64()).build();
    }

    if (type.equals(MessageType.imageMessage)) {
      String caption = dto.data().message().imageMessage().caption();
      if (!Objects.isNull(caption)) {
        builder.caption(caption);
      }
      return builder.data(dto.data().message().base64()).build();
    }

    if (type.equals(MessageType.conversation)) {
      return builder.data(dto.data().message().conversation()).build();
    }

    if (type.equals(MessageType.listResponseMessage)) {
      builder.data(dto.data().message().listResponseMessage().title());

      var listMessage = dto.data().contextInfo().quotedMessage().listMessage();

      var isTransactionPattern = listMessage.title().contains("Registrar ");

      if (isTransactionPattern) {
        var q = listMessage.description().split("\n");
        var currencyAndVal = q[2].replace("Valor: ", "");

        var title =
            dto.data()
                .message()
                .listResponseMessage()
                .contextInfo()
                .quotedMessage()
                .listMessage()
                .title();

        TransactionType transactionType =
            title.equalsIgnoreCase(registrar_gasto)
                ? TransactionType.EXPENSE
                : title.equalsIgnoreCase(registrar_receita) ? TransactionType.INCOME : null;

        var transactionDto =
            TransactionPayloadDto.builder()
                .business(q[0].replace("Descrição: ", ""))
                .category(Category.fromDescricao(q[1].replace("Categoria: ", "")))
                .value(new BigDecimal(currencyAndVal.replaceAll("[^\\d.,]+", "")))
                .currency(currencyAndVal.replaceAll("[\\d\\s.,]+", ""))
                .type(transactionType)
                .build();

        builder.transactionPayloadDto(transactionDto);
      }

      return builder.build();
    }

    throw new RuntimeException("Erro ao converter webhook.");
  }
}
