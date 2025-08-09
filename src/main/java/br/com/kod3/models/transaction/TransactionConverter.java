package br.com.kod3.models.transaction;

import br.com.kod3.models.debt.Debt;
import br.com.kod3.models.recurrence.Recurrence;
import br.com.kod3.models.user.User;

import java.util.Objects;

public class TransactionConverter {
  private TransactionConverter() {}

  public static Transaction toEntity(TransactionPayloadDto dto, User user, Debt debt, Recurrence recurrence) {
    return Transaction.builder()
        .business(dto.getBusiness())
        .value(dto.getValue())
        .category(dto.getCategory())
        .type(dto.getType())
        .currency(dto.getCurrency())
            .debt(debt)
            .recurrence(recurrence)
        .user(user)
        .build();
  }

    public static Transaction fromRecurrence(Recurrence recurrence) {

      TransactionType t = null;

      if (recurrence.getType().equals(TransactionType.RECURRING_EXPENSE)) {
        t = TransactionType.EXPENSE;
      } else if (recurrence.getType().equals(TransactionType.RECURRING_INCOME)){
        t = TransactionType.INCOME;
      }

      if (Objects.isNull(t)) {
        throw new RuntimeException("Tipo inesperado para recorrência.");
      }

      return Transaction.builder()
              .business(recurrence.getBusiness())
              .value(recurrence.getValue())
              .category(recurrence.getCategory())
              .type(t)
              .currency(recurrence.getCurrency())
              .user(recurrence.getUser())
              .build();
    }
}
