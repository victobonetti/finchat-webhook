package br.com.kod3.models.transaction;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.user.User;

import java.util.Objects;

public class TransactionConverter {
  private TransactionConverter() {}

  public static Transaction toEntity(TransactionPayloadDto dto, User user, Debt debt, Recorrencia recorrencia) {
    return Transaction.builder()
        .business(dto.getBusiness())
        .value(dto.getValue())
        .category(dto.getCategory())
        .type(dto.getType())
        .currency(dto.getCurrency())
            .divida(debt)
            .recorrencia(recorrencia)
        .user(user)
        .build();
  }

    public static Transaction fromRecorrencia(Recorrencia recorrencia) {

      TransactionType t = null;

      if (recorrencia.getType().equals(TransactionType.RECORRENT_EXPENSE)) {
        t = TransactionType.EXPENSE;
      } else if (recorrencia.getType().equals(TransactionType.RECORRENT_INCOME)){
        t = TransactionType.INCOME;
      }

      if (Objects.isNull(t)) {
        throw new RuntimeException("Tipo inesperado para recorrência.");
      }

      return Transaction.builder()
              .business(recorrencia.getBusiness())
              .value(recorrencia.getValue())
              .category(recorrencia.getCategory())
              .type(t)
              .currency(recorrencia.getCurrency())
              .user(recorrencia.getUser())
              .build();
    }
}
