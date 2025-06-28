package br.com.kod3.clients;

import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "n8n-processing")
public interface N8nApiClient {
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  void process(ConvertedDto converted);
}
