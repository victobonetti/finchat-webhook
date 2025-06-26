package br.com.kod3.services;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle
public interface Messages {
  @Message(
      """
      🚫 *Usuário não encontrado!*
      
      Parece que você ainda não tem um registro no nosso sistema.
      Vamos te ajudar com isso! 😊
      Você só precisa se cadastrar em:
      
      https://finchat-app-kappa.vercel.app
      """)
  String usuario_sem_registro();

  @Message(
      """
      📋 *Precisamos de uma informação importante!*
      
      Antes de continuar, precisamos cadastrar o seu *perfil de investidor*.
      Isso leva só alguns minutinhos! 😉
      """)
  String solicita_perfil_investidor();

  @Message(
      """
      ✅ *Tudo certo!*
      
      Seu acesso foi liberado. Agora você tem *acesso total* ao sistema! 🎉
      Aproveite todos os recursos disponíveis! 🚀
      """)
  String acesso_total_sistema();

  @Message(
      """
      ⚠️ *Perfil de investidor inválido!*
      
      Verifique as informações preenchidas e tente novamente.
      Se precisar de ajuda, estamos aqui! 💬
      """)
  String perfil_investidor_invalido();

  @Message(
      """
      ❌ *Erro na validação da resposta da transação!*
      
      Algo deu errado ao processar a resposta.
      Tente novamente em instantes ou entre em contato com o suporte. 🔧
      """)
  String erro_validacao_resposta_transacao();

  @Message("confirmar")
  String confirma_transacao();

  @Message("cancelar")
  String cancela_transacao();

  @Message("Registro incluido")
  String registro_incluido();

  @Message("Registro cancelado")
  String registro_cancelado();

  @Message("Registrar gasto")
  String registrar_gasto();

  @Message("Registrar receita")
  String registrar_receita();

  // PI

  @Message("📊 *Qual o seu perfil de investidor?*")
  String titulo_perfil_investidor();

  @Message("🔎 Isso nos ajuda a recomendar melhores opções para você!")
  String descricao_perfil_investidor();

  @Message("Selecionar")
  String botao_perfil_investidor();

  @Message("Responda com sinceridade para começarmos 🚀")
  String rodape_perfil_investidor();

  @Message("🛡️ Conservador")
  String perfil_conservador_titulo();

  @Message("Prefere segurança e estabilidade, mesmo que os ganhos sejam menores.")
  String perfil_conservador_descricao();

  @Message("⚖️ Moderado")
  String perfil_moderado_titulo();

  @Message("Busca equilíbrio entre segurança e rentabilidade, aceitando alguns riscos moderados.")
  String perfil_moderado_descricao();

  @Message("📈 Arrojado")
  String perfil_arrojado_titulo();

  @Message("Aceita correr riscos maiores em troca de maiores retornos no longo prazo.")
  String perfil_arrojado_descricao();

}
