package br.com.kod3.models.evolution.requestpayload.converter;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.recorrencia.PeriodEnum;
import br.com.kod3.models.transaction.Category;
import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.transaction.TransactionType;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static br.com.kod3.services.util.Messages.*;

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

    String title = getTitle(dto);
    TransactionType type = getType(title);
    LocalDate date = extractDate();


    String descricao = "";
    String categoria = "";
    String currencyAndVal = "";

    String idRecorrencia = "";
    String idDebt = "";
    String periodoStr = "";


    if (q.length >= 1) {
      descricao = q[0].replace("Descrição: ", "");
    }

    if (q.length >= 2) {
      categoria = q[1].replace("Categoria: ", "");
    }

    if (q.length >= 3) {
      currencyAndVal = q[2].replace("Valor: ", "");
    }

    if (q.length >= 4) {
      if (q[3].contains("Período")) {
        periodoStr = q[3].replace("Período: ", "");
      }

      if (q[3].contains("Id da recorrência")) {
        idRecorrencia = q[3].replace("Id da recorrência: ", "");
      }

      if (q[3].contains("Id da dívida")) {
        idDebt = q[3].replace("Id da dívida: ", "");
      }
    }


    final String currency = currencyAndVal.replaceAll("[\\d\\s.,]+", "");
    final BigDecimal val = new BigDecimal(currencyAndVal.replaceAll("[^\\d.,]+", "")); // Retira caracteres especiais

    return TransactionPayloadDto.builder()
            .business(descricao)
            .category(Category.fromDescricao(categoria))
            .value(val)
            .currency(currency)
            .period(PeriodEnum.fromString(periodoStr))
            .date(date)
            .type(type)
            .idRecorrencia(!idRecorrencia.isBlank() ? idRecorrencia : null)
            .idDebt(!idDebt.isBlank() ? idDebt : null)
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

