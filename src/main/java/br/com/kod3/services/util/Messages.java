package br.com.kod3.services.util;

public final class Messages {

  private Messages() {}

  public static final String usuario_sem_registro =
      """
        🚫 *Usuário não encontrado!*
        
        Ainda estamos em fase de testes. Solicite liberação de acesso no grupo:
        
        https://chat.whatsapp.com/CHXCUruElUa2kRaA8jRFfU
        """;

  public static final String erro_parse =
          """
            Ocorreu um erro ao processar sua mensagem. Desative o modo de mensagens temporátias, ou aguarde alguns minutos.
          """;

  public static final String erro_interno = "Ocorreu um erro interno ao processar a solicitação.";

  public static final String erro_validacao_resposta_transacao =
      """
        ❌ *Erro na validação da resposta da transação!*

        Algo deu errado ao processar a resposta.
        Tente novamente em instantes ou entre em contato com o suporte. 🔧
      """;

  public static final String confirma_transacao = "confirmar";

  public static final String valor_excede_divida = "Oops! O valor que você quer pagar excede o valor da dívida! Refaça a operação.";

  public static final String debito_quitado = "Sua dívida foi quitada! Parabéns.";

  public static final String divida_criada = "Dívida criada ✅";

  public static final String gasto_recorrente_criado = "Gasto recorrente criado ✅\nUm registro de gasto foi feito para o dia atual.";

  public static final String receita_recorrente_criada = "Receita recorrente criada ✅\nUm registro de entrada foi feito para o dia atual.";

  public static final String cancela_transacao = "cancelar";

  public static final String registro_incluido = "Registro incluído ✅";

  public static final String registro_cancelado = "Registro cancelado ❌";

  public static final String registrar_gasto = "\uD83D\uDCB0 Registrar gasto";

  public static final String registrar_receita = "\uD83D\uDCB0 Registrar entrada";

  public static final String registrar_gasto_recorrente = "\uD83D\uDCB0 Registrar gasto recorrente";

  public static final String registrar_receita_recorrente = "\uD83D\uDCB0 Registrar receita recorrente";

  public static final String registrar_divida = "\uD83D\uDCB0 Registrar divida";

  public static final String conferencia_registro_recorrente = "Foi encontrado um registro recorrente par ao dia atual. Por favor, confirme o registro.";
}
