package br.com.kod3.services;

import lombok.Getter;

@Getter
public enum CodigosDeResposta {
  ERRO(0),
  SOLICITA_CADASTRO(1),
  INSERE_PENDENCIA_E_SOLICITA_PERFIL(2),
  SOLICITA_PERFIL(3),
  CONFIRMA_PERFIL(4),
  CONFIRMA_TRANSACAO(5),
  CANCELA_TRANSACAO(6);

  final Integer codigo;

  CodigosDeResposta(int i) {
    this.codigo = i;
  }

  public Integer toCode() {
    return this.codigo;
  }
}
