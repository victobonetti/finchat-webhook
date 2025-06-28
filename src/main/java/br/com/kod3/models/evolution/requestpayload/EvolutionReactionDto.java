package br.com.kod3.models.evolution.requestpayload;

import io.quarkus.logging.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionReactionDto {
  WebhookBodyDto.DataDto.KeyDto key;
  final String reaction = "👍";

  public EvolutionReactionDto(String remoteJid, String messageId) {
    this.key = new WebhookBodyDto.DataDto.KeyDto(remoteJid, true, messageId);
    Log.info(this.reaction);
    Log.info(this.key.remoteJid());
    Log.info(this.key.id());
    Log.info(this.key.fromMe());
  }
}
