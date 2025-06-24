package br.com.kod3.models.transaction;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

@Getter
public enum Category {
    JOGOS_DE_AZAR("Jogos de Azar"),
    ALIMENTACAO("Alimentação"),
    TRANSPORTE("Transporte"),
    EDUCACAO("Educação"),
    SAUDE("Saúde"),
    LAZER("Lazer"),
    MORADIA("Moradia"),
    OUTROS("Outros");

    private final String descricao;

    Category(String descricao) {
        this.descricao = descricao;
    }

    public static Category fromDescricao(String descricao) {
        for (Category c : Category.values()) {
            if (c.descricao.equalsIgnoreCase(descricao)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }

    @Converter(autoApply = true)
    public static class CategoryConverter implements AttributeConverter<Category, String> {
        @Override
        public String convertToDatabaseColumn(Category attribute) {
            return attribute != null ? attribute.getDescricao() : null;
        }

        @Override
        public Category convertToEntityAttribute(String dbData) {
            return dbData != null ? Category.fromDescricao(dbData) : null;
        }
    }
}
