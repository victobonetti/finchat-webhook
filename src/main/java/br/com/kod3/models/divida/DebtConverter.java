package br.com.kod3.models.divida;

import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.user.User;

import java.math.BigDecimal;

public class DebtConverter {
    private DebtConverter (){}

    public static Debt toEntity(TransactionPayloadDto dto, User user){
        return Debt.builder()
                .business(dto.getBusiness())
                .paidValue(BigDecimal.ZERO)
                .totalValue(dto.getValue())
                .category(dto.getCategory())
                .currency(dto.getCurrency())
                .user(user)
                .build();
    }
}
