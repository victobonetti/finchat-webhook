package br.com.kod3.models.evolution.requestpayload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionReactionDto {
  WebhookBodyDto.DataDto.KeyDto key;
  final String reaction = "👍";

  public EvolutionReactionDto(String remoteJid, String messageId) {
    this.key = new WebhookBodyDto.DataDto.KeyDto(remoteJid, true, messageId);
  }
}
