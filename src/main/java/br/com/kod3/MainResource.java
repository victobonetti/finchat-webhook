package br.com.kod3;

import static br.com.kod3.services.CodigosDeResposta.*;
import static br.com.kod3.services.Messages.*;

import br.com.kod3.models.evolution.list.EvolutionListFactory;
import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.evolution.requestpayload.converter.EvolutionPayloadConverter;
import br.com.kod3.models.streak.StreakResponseDto;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
import br.com.kod3.models.user.UserDataDto;
import br.com.kod3.services.*;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("v1")
public class MainResource {

  @Inject UserService userService;
  @Inject TransactionService transactionService;
  @Inject EvolutionApiService evolutionApiService;
  @Inject StreakService streakService;
  @Inject ResponseHandler res;
  @Inject EvolutionPayloadConverter converter;

  @Inject
  @Broadcast
  @Channel("my-channel")
  Emitter<ConvertedDto> emitter;

  @POST
  @Path("webhook/get-streak")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserStreak(@QueryParam("uid") String userId){
    Log.info("Obtendo streaks para usuario " + userId);
    return Response.ok().entity(new StreakResponseDto(userId, streakService.getStreakFromUserId(userId))).build();
  }

  @POST
  @Path("webhook/ask-investor-profile")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response askInvestorProfile(@Valid UserDataDto body) {

    final String phone = body.telefone();
    final Optional<User> userOptional = userService.findByPhone(phone);

    if (userOptional.isPresent()) {
      final EvolutionMessageSender evo = new EvolutionMessageSender(evolutionApiService, phone);
      userService.atualizaPerfilInvestidor(
          userOptional.get(), PerfilInvestidorType.CADASTRO_PENDENTE);
      evo.send(solicita_perfil_investidor);
      evo.opts(EvolutionListFactory.getPerfilInvestidorPool(body.telefone()));
    }

    return Response.ok().build();
  }

  @POST
  @Path("webhook")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response webhook(@Valid WebhookBodyDto body) {

    final ConvertedDto converted = converter.parse(body);

    final String phone = converted.getTelefone();
    final Optional<User> userOptional = userService.findByPhone(phone);

    final EvolutionMessageSender evo = new EvolutionMessageSender(evolutionApiService, phone);

    try {
      if (userOptional.isEmpty()) {
        return handleNewUser(converted.getType(), evo);
      }

      final User user = userOptional.get();

      converted.setUserId(user.getId());

      if (isInvestorProfilePending(user)) {
        return handleInvestorProfilePending(user, converted, evo);
      } else {
        return handleRegisteredUser(user, converted, evo);
      }
    } catch(Exception e) {
        Log.info(erro_interno);
        Log.error(e);
        evo.send(erro_interno);
        return res.send(ERRO_INTERNO, null);
    }
  }

  private Response handleNewUser(MessageType type, EvolutionMessageSender evo) {
    evo.send(usuario_sem_registro);
    return res.send(SOLICITA_CADASTRO, type);
  }

  private Response handleInvestorProfilePending(
      User user, ConvertedDto converted, EvolutionMessageSender evo) {
    final MessageType type = converted.getType();

    if (Objects.isNull(user.getPerfilInvestidor())) {
      userService.atualizaPerfilInvestidor(user, PerfilInvestidorType.CADASTRO_PENDENTE);
      evo.send(solicita_perfil_investidor);
      evo.opts(EvolutionListFactory.getPerfilInvestidorPool(user.getTelefone()));
      return res.send(INSERE_PENDENCIA_E_SOLICITA_PERFIL, type);
    }

    final boolean isListResponse = type.equals(MessageType.listResponseMessage);
    if (!isListResponse) {
      evo.opts(EvolutionListFactory.getPerfilInvestidorPool(user.getTelefone()));
      return res.send(CodigosDeResposta.SOLICITA_PERFIL, type);
    }

    final String profileData = converted.getData().toUpperCase();

    for (var p : PerfilInvestidorType.getValidPerfisList()) {
      if (profileData.contains(p)) {
        userService.atualizaPerfilInvestidor(user, PerfilInvestidorType.fromDescricao(p));
        evo.send(acesso_total_sistema);
        return res.send(CONFIRMA_PERFIL, type);
      }
    }

    evo.send(perfil_investidor_invalido);
    evo.opts(EvolutionListFactory.getPerfilInvestidorPool(user.getTelefone()));
    return res.send(CodigosDeResposta.PERFIL_INVESTIDOR_INVALIDO, type);
  }

  private Response handleRegisteredUser(
      User user, ConvertedDto converted, EvolutionMessageSender evo) {
    final MessageType type = converted.getType();

    final boolean isPrompt =
        Objects.isNull(converted.getTransactionPayloadDto())
            && !type.equals(MessageType.listResponseMessage);
    if (isPrompt) {
      evo.like(converted.getRemoteJid(), converted.getMessageId());
      emitter.send(converted);
      return res.send(ENVIA_PROMPT, type);
    }

    final boolean isTransactionResponse =
        !Objects.isNull(converted.getTransactionPayloadDto())
            && type.equals(MessageType.listResponseMessage);
    if (isTransactionResponse) {
      final String data = converted.getData().toLowerCase();
      if (data.contains(confirma_transacao)) {

        var shouldShowStreak = !streakService.hasTransactionToday(user.getId());

        transactionService.createOne(
            TransactionConverter.toEntity(converted.getTransactionPayloadDto(), user), user.getId());
        evo.send(registro_incluido);

        if (shouldShowStreak){
            handleStreak(user, evo);
        }

        return res.send(CONFIRMA_TRANSACAO, type);
      }

      if (data.contains(cancela_transacao)) {
        evo.send(registro_cancelado);
        return res.send(CANCELA_TRANSACAO, type);
      }

      evo.send(erro_validacao_resposta_transacao);
      return res.send(ERRO_VALIDACAO_RESPOSTA_TRANSACAO, type);
    }

    evo.send(erro_validacao_resposta_transacao);
    return res.send(CodigosDeResposta.CASO_DESCONHECIDO, type);
  }

  private void handleStreak(User user, EvolutionMessageSender evo) {
    var streak = streakService.getStreakFromUserId(user.getId());
    if (streak == 1){
      evo.send("Sua ofensiva começou! Continue assim! \uFE0F\u200D\uD83D\uDD25\uD83D\uDC2F");
    } else {
      evo.send("Sua ofensiva está em " + streak + " dias. Continue assim! \uFE0F\u200D\uD83D\uDD25\uD83D\uDC2F");
    }
  }

  private boolean isInvestorProfilePending(User user) {
    return Objects.isNull(user.getPerfilInvestidor())
        || user.getPerfilInvestidor().equals(PerfilInvestidorType.CADASTRO_PENDENTE);
  }
}
