package br.com.kod3.clients;

import br.com.kod3.models.evolution.TextMessageDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="evolution-api")
@ClientHeaderParam(name = "apikey", value = "${evo.api.key}")
public interface EvolutionApiClient {
    @POST
    @Path("/message/sendText/Finchat")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void sendMessage(TextMessageDto textMessageDto);
}
