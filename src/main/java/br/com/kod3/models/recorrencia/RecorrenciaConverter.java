package br.com.kod3.models.recorrencia;

import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.SituacaoEnum;

import java.time.DayOfWeek;


public class RecorrenciaConverter {

    private RecorrenciaConverter (){}

    public static Recorrencia toEntity(TransactionPayloadDto dto, User user){
        return Recorrencia.builder()
                .business(dto.getBusiness())
                .value(dto.getValue())
                .category(dto.getCategory())
                .type(dto.getType())
                .currency(dto.getCurrency())
                .paymentDay(dto.getDate())
                .dayOfWeek(DayOfWeek.from(dto.getDate()).getValue())
                .dayOfMonth(dto.getDate().getDayOfMonth())
                .situacao(SituacaoEnum.ATIVO)
                .period(dto.getPeriod())
                .user(user)
                .build();
    }
}
