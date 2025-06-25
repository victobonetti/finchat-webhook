package br.com.kod3.models.transaction;

import br.com.kod3.models.user.User;

public class TransactionConverter {
  private TransactionConverter() {}

  public static Transaction toEntity(TransactionPayloadDto dto, User user) {
    return Transaction.builder()
        .business(dto.getBusiness())
        .value(dto.getValue())
        .category(dto.getCategory())
        .type(dto.getType())
        .currency(dto.getCurrency())
        .user(user)
        .build();
  }
}
