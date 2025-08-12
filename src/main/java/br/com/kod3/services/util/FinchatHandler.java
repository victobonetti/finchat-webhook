package br.com.kod3.services.util;

import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.user.User;
import br.com.kod3.services.evolution.EvolutionMessageSender;

public interface FinchatHandler {
    CodigoDeResposta handle(ConvertedDto converted, User user, EvolutionMessageSender evo);
}
