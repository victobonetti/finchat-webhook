package br.com.kod3.services.util;

import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;

public class Util {
  public static String getPhone(WebhookBodyDto dto) {
    return dto.data().key().remoteJid().split("@")[0];
  }
}
