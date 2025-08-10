package br.com.kod3.models.recurrence;

import br.com.kod3.models.transaction.TransactionPayloadDto;
import br.com.kod3.models.user.User;
import br.com.kod3.models.util.enums.SituacaoEnum;

import java.time.DayOfWeek;


public class RecurrenceConverter {

    private RecurrenceConverter(){}

    public static Recurrence toEntity(TransactionPayloadDto dto, User user){
        return Recurrence.builder()
                .description(dto.getDescription())
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
