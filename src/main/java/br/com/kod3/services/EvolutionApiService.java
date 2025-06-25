package br.com.kod3.services;

import br.com.kod3.clients.EvolutionApiClient;
import br.com.kod3.models.evolution.list.EvolutionListDto;
import br.com.kod3.models.evolution.requestpayload.TextMessageDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@RegisterRestClient
public class EvolutionApiService {
  @RestClient
  private EvolutionApiClient evoClient;

  public void sendMessage(TextMessageDto textMessageDto) {
    evoClient.sendMessage(textMessageDto);
  }

  public void sendPool(EvolutionListDto pool) {
    evoClient.sendPool(pool);
  }
}
