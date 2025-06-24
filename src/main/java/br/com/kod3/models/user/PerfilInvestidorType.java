package br.com.kod3.models.user;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum PerfilInvestidorType {
    CADASTRO_PENDENTE,
    CONSERVADOR,
    MODERADO,
    ARROJADO;

    public static List<String> getValidPerfisList(){
        return Arrays.stream(PerfilInvestidorType.values())
                .filter(e -> e.equals(PerfilInvestidorType.CADASTRO_PENDENTE))
                        .map(Objects::toString).toList();
    }

    public static PerfilInvestidorType fromDescricao(String descricao) {
        for (PerfilInvestidorType p : PerfilInvestidorType.values()) {
            if (p.name().equalsIgnoreCase(descricao)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }
}