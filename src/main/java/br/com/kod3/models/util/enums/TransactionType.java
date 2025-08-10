package br.com.kod3.models.util.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import java.util.Arrays;
import java.util.Objects;

public enum TransactionType {
  EXPENSE(0),
  RECURRING_EXPENSE(1),
  RECURRING_INCOME(2),
  DEBT(3),
  INCOME(4);

  @Getter
  private final Integer code;

  TransactionType(Integer code) {
    this.code = code;
  }

    public static TransactionType fromCode(Integer code) {
    Objects.requireNonNull(code);
    return Arrays.stream(TransactionType.values())
            .filter(t -> Objects.equals(t.code, code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid TransactionType code: " + code));
  }

  @Converter(autoApply = true)
  public static class TransactionTypeConverter implements AttributeConverter<TransactionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TransactionType attribute) {
      return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public TransactionType convertToEntityAttribute(Integer dbData) {
      return TransactionType.fromCode(dbData);
    }
  }
}
