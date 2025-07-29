package br.com.kod3.models.evolution.requestpayload.converter;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.transaction.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static br.com.kod3.services.Messages.*;

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

      var isTransactionPattern = dto.data().contextInfo().quotedMessage().listMessage().title().contains("Registrar ");

      if (isTransactionPattern) {
        builder.transactionPayloadDto(generateTransactionDto(dto));
      }

      return builder.build();
    }

    throw new RuntimeException("Erro ao converter webhook.");
  }

  private TransactionPayloadDto generateTransactionDto(WebhookBodyDto dto){
    var q = dto.data().contextInfo().quotedMessage().listMessage().description().split("\n");
    var currencyAndVal = q[2].replace("Valor: ", "");

    String title = getTitle(dto);
    TransactionType type = getType(title);
    LocalDate date = extractDate();

    return TransactionPayloadDto.builder()
            .business(q[0].replace("Descrição: ", ""))
            .category(Category.fromDescricao(q[1].replace("Categoria: ", "")))
            .value(new BigDecimal(currencyAndVal.replaceAll("[^\\d.,]+", "")))
            .currency(currencyAndVal.replaceAll("[\\d\\s.,]+", ""))
            .date(date)
            .type(type)
            .build();
  }

  private String getTitle(WebhookBodyDto body){
    return body.data()
            .message()
            .listResponseMessage()
            .contextInfo()
            .quotedMessage()
            .listMessage()
            .title();
  }

  private LocalDate extractDate() {
    return LocalDate.now();
  }

  private static TransactionType getType(String title) {
    if (title.equalsIgnoreCase(registrar_gasto)) {
      return TransactionType.EXPENSE;
    } else if (title.equalsIgnoreCase(registrar_receita)) {
      return TransactionType.INCOME;
    } else if (title.equalsIgnoreCase(registrar_gasto_recorrente)) {
      return TransactionType.RECORRENT_EXPENSE;
    } else if (title.equalsIgnoreCase(registrar_receita_recorrente)) {
      return TransactionType.RECORRENT_INCOME;
    } else if (title.equalsIgnoreCase(registrar_divida)) {
      return TransactionType.DEBT;
    }
    throw new RuntimeException("Tipo indefinido: " + title);
  }
}

