package br.com.kod3.models.evolution.pool;

import br.com.kod3.models.user.PerfilInvestidorType;

import java.util.List;

public class EvolutionPoolFactory {

    private EvolutionPoolFactory(){}

    public static EvolutionPoolDto getPerfilInvestidorPool(String phone) {
        return EvolutionPoolDto.builder()
                .number(phone)
                .name("Pesquisa Perfil investidor...")
                .values(PerfilInvestidorType.getValidPerfisList())
                .build();
    }

    public static EvolutionPoolDto getTransactionRegistryPool(String phone) {
        return EvolutionPoolDto.builder()
                .number(phone)
                .name("Registrar transacao...")
                .values(List.of("Confirmar", "Cancelar"))
                .build();
    }
}
