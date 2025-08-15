package br.com.kod3.processors;

import br.com.kod3.clients.N8nApiClient;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class ChatResponseProcessor {

  @RestClient N8nApiClient client;

  @Incoming("my-channel")
  @Blocking
  public void process(ConvertedDto dto) {
    Log.info("Iniciando processamento...");
    client.process(dto);
  }
}
