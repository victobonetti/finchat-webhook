package br.com.kod3.models.util.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;

public enum SituacaoEnum {
  ATIVO(0),
  INATIVO(1);

  @Getter final Integer code;

  SituacaoEnum(Integer code) {
    this.code = code;
  }

  public static SituacaoEnum fromCode(Integer code) {
    Objects.requireNonNull(code);
    return Arrays.stream(SituacaoEnum.values())
        .filter(s -> Objects.equals(s.code, code))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid SituacaoEnum code: " + code));
  }

  @Converter(autoApply = true)
  public static class SituacaoConverter implements AttributeConverter<SituacaoEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SituacaoEnum attribute) {
      return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public SituacaoEnum convertToEntityAttribute(Integer dbData) {
      return dbData != null ? SituacaoEnum.fromCode(dbData) : null;
    }
  }
}
