package br.com.kod3.models.transaction;

import br.com.kod3.models.divida.Debt;
import br.com.kod3.models.recorrencia.Recorrencia;
import br.com.kod3.models.user.User;

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
      return Transaction.builder()
              .business(recorrencia.getBusiness())
              .value(recorrencia.getValue())
              .category(recorrencia.getCategory())
              .type(recorrencia.getType())
              .currency(recorrencia.getCurrency())
              .user(recorrencia.getUser())
              .build();
    }
}
