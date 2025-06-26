package br.com.kod3;

import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class Processor {

  @Incoming("my-channel")
  public void process(ConvertedDto dto) {
    Log.info(dto);
  }
}
