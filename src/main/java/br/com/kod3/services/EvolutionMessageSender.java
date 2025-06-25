package br.com.kod3.services;

import br.com.kod3.models.evolution.list.EvolutionListDto;
import br.com.kod3.models.evolution.requestpayload.TextMessageDto;

public class EvolutionMessageSender {
  private final String phone;
  private final EvolutionApiService evolutionApiService;

  public EvolutionMessageSender(EvolutionApiService evolutionApiService, String phone) {
    this.phone = phone;
    this.evolutionApiService = evolutionApiService;
  }

  public void send(String message) {
    evolutionApiService.sendMessage(new TextMessageDto(this.phone, message));
  }

  public void opts(EvolutionListDto listDto) {}
}
