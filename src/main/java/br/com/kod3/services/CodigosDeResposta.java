package br.com.kod3.services;

import lombok.Getter;

@Getter
public enum CodigosDeResposta {
  ERRO_VALIDACAO_RESPOSTA_TRANSACAO(0),
  SOLICITA_CADASTRO(1),
  INSERE_PENDENCIA_E_SOLICITA_PERFIL(2),
  SOLICITA_PERFIL(3),
  CONFIRMA_PERFIL(4),
  CONFIRMA_TRANSACAO(5),
  CANCELA_TRANSACAO(6),
  ENVIA_PROMPT(7),
  CASO_DESCONHECIDO(8),
  PERFIL_INVESTIDOR_INVALIDO(9),
  ERRO_INTERNO(10);

  final Integer codigo;

  CodigosDeResposta(int i) {
    this.codigo = i;
  }

  public Integer toCode() {
    return this.codigo;
  }
}
