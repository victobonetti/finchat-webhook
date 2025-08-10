package br.com.kod3.models.debt;

import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.user.User;

public class DebtConverter {
    private DebtConverter (){}

    public static Debt toEntity(TransactionPayloadDto dto, User user){
        return Debt.builder()
                .description(dto.getDescription())
                .totalValue(dto.getValue())
                .category(dto.getCategory())
                .currency(dto.getCurrency())
                .user(user)
                .build();
    }
}
