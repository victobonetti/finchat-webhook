package br.com.kod3.services;

public final class Messages {

  private Messages() {}

  public static final String usuario_sem_registro =
      """
        🚫 *Usuário não encontrado!*

        Parece que você ainda não tem um registro no nosso sistema.
        Vamos te ajudar com isso! 😊
        Você só precisa se cadastrar em:

        https://finchat-app-kappa.vercel.app
        """;

  public static final String erro_parse =
          """
            Ocorreu um erro ao processar sua mensagem. Desative o modo de mensagens temporátias, ou aguarde alguns minutos.
          """;

  public static final String solicita_perfil_investidor =
      """
        📋 *Precisamos de uma informação importante!*

        Antes de continuar, precisamos cadastrar o seu *perfil de investidor*.
        Isso leva só alguns minutinhos! 😉
        """;

  public static final String erro_interno = "Ocorreu um erro interno ao processar a solicitação.";

  public static final String acesso_total_sistema =
      """
        ✅ *Tudo certo!*

        Seu acesso foi liberado. Agora você tem *acesso total* ao sistema! 🎉
        Aproveite todos os recursos disponíveis! 🚀
        """;

  public static final String perfil_investidor_invalido =
      """
        ⚠️ *Perfil de investidor inválido!*

        Verifique as informações preenchidas e tente novamente.
        Se precisar de ajuda, estamos aqui! 💬
        """;

  public static final String erro_validacao_resposta_transacao =
      """
        ❌ *Erro na validação da resposta da transação!*

        Algo deu errado ao processar a resposta.
        Tente novamente em instantes ou entre em contato com o suporte. 🔧
      """;

  public static final String confirma_transacao = "confirmar";

  public static final String confirma_transacao_descricao = "✅ confirmar essa transação (registrar)";

  public static final String cancela_transacao_descricao = "❌ cancelar essa transação (não registrar)";

  public static final String divida_criada = "Dívida criada ✅";

  public static final String gasto_recorrente_criado = "Gasto recorrente criado ✅";

  public static final String receita_recorrente_criada = "Receita recorrente criada ✅";

  public static final String cancela_transacao = "cancelar";

  public static final String registro_incluido = "Registro incluído ✅";

  public static final String registro_cancelado = "Registro cancelado ❌";

  public static final String registrar_gasto = "\uD83D\uDCB0 Registrar gasto";

  public static final String registrar_receita = "\uD83D\uDCB0 Registrar entrada";

  public static final String registrar_gasto_recorrente = "\uD83D\uDCB0 Registrar gasto recorrente";

  public static final String registrar_receita_recorrente = "\uD83D\uDCB0 Registrar receita recorrente";

  public static final String registrar_divida = "\uD83D\uDCB0 Registrar divida";

  public static final String titulo_perfil_investidor = "📊 *Qual o seu perfil de investidor?*";

  public static final String descricao_perfil_investidor =
      "🔎 Isso nos ajuda a recomendar melhores opções para você!";

  public static final String botao_perfil_investidor = "Selecionar";

  public static final String rodape_perfil_investidor =
      "Responda com sinceridade para começarmos 🚀";

  public static final String perfil_conservador_titulo = "🛡️ Conservador";

  public static final String perfil_conservador_descricao =
      "Prefere segurança e estabilidade, mesmo que os ganhos sejam menores.";

  public static final String perfil_moderado_titulo = "⚖️ Moderado";

  public static final String perfil_moderado_descricao =
      "Busca equilíbrio entre segurança e rentabilidade, aceitando alguns riscos moderados.";

  public static final String perfil_arrojado_titulo = "📈 Arrojado";

  public static final String perfil_arrojado_descricao =
      "Aceita correr riscos maiores em troca de maiores retornos no longo prazo.";

  public static final String enviando_prompt =
      "Estamos processando sua mensagem... Aguarde alguns segundos...";

  public static final String conferencia_registro_recorrente = "Foi encontrado um registro recorrente par ao dia atual. Por favor, confirme o registro.";
}
