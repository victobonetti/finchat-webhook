package br.com.kod3.services.util;

import lombok.Getter;

@Getter
public enum CodigosDeResposta {
  ERRO_VALIDACAO_RESPOSTA_TRANSACAO(0),
  SOLICITA_CADASTRO(1),
  CONFIRMA_TRANSACAO(5),
  CANCELA_TRANSACAO(6),
  ENVIA_PROMPT(7),
  CASO_DESCONHECIDO(8),
  ERRO_INTERNO(10),
  ERRO_VALOR_EXCEDE_DIVIDA(11);

  final Integer codigo;

  CodigosDeResposta(int i) {
    this.codigo = i;
  }

  public Integer toCode() {
    return this.codigo;
  }
}
