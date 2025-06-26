package br.com.kod3;

import static br.com.kod3.services.CodigosDeResposta.*;
import static jakarta.ws.rs.core.Response.Status.*;

import br.com.kod3.models.evolution.list.EvolutionListFactory;
import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.TextMessageDto;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.evolution.requestpayload.converter.EvolutionPayloadConverter;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
import br.com.kod3.services.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Objects;
import java.util.Optional;

@Path("v1")
public class MainResource {

  @Inject UserService userService;
  @Inject TransactionService transactionService;
  @Inject EvolutionApiService evolutionApiService;
  @Inject ResponseHandler res;
  @Inject EvolutionPayloadConverter converter;

  //  @Inject
  //  @Channel("my-channel")
  //  Emitter<ConvertedDto> emitter;

  @POST
  @Path("webhook")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response webhook(@Valid WebhookBodyDto body) {
    final ConvertedDto converted = converter.parse(body);
    final String phone = converted.getTelefone();
    final Optional<User> userOptional = userService.findByPhone(phone);

    if (userOptional.isEmpty()) {
      return handleNewUser(phone, converted.getType());
    }

    final User user = userOptional.get();
    final EvolutionMessageSender evo = new EvolutionMessageSender(evolutionApiService, phone);

    if (isInvestorProfilePending(user)) {
      return handleInvestorProfilePending(user, converted, evo);
    } else {
      return handleRegisteredUser(user, converted, evo);
    }
  }

  private Response handleNewUser(String phone, MessageType type) {
    var msg = new TextMessageDto(phone, "sem registro.");
    evolutionApiService.sendMessage(msg);
    return res.send(SOLICITA_CADASTRO, type, NO_CONTENT);
  }

  private Response handleInvestorProfilePending(
      User user, ConvertedDto converted, EvolutionMessageSender evo) {
    final MessageType type = converted.getType();

    if (Objects.isNull(user.getPerfilInvestidor())) {
      userService.atualizaPerfilInvestidor(user, PerfilInvestidorType.CADASTRO_PENDENTE);
      evo.send("Vamos precisar cadastrar o seu perfil de investidor.");
      evo.opts(EvolutionListFactory.getPerfilInvestidorPool(user.getTelefone()));
      return res.send(INSERE_PENDENCIA_E_SOLICITA_PERFIL, type, CREATED);
    }

    final boolean isListResponse = type.equals(MessageType.listResponseMessage);
    if (!isListResponse) {
      evo.opts(EvolutionListFactory.getPerfilInvestidorPool(user.getTelefone()));
      return res.send(CodigosDeResposta.SOLICITA_PERFIL, type, OK);
    }

    final String profileData = converted.getData().toUpperCase();
    if (PerfilInvestidorType.getValidPerfisList().contains(profileData)) {
      userService.atualizaPerfilInvestidor(
          user, PerfilInvestidorType.fromDescricao(converted.getData()));
      evo.send("Agora você tem acesso total ao sistema");
      return res.send(CONFIRMA_PERFIL, type, CREATED);
    } else {
      return res.send(CodigosDeResposta.PERFIL_INVESTIDOR_INVALIDO, type, BAD_REQUEST);
    }
  }

  private Response handleRegisteredUser(
      User user, ConvertedDto converted, EvolutionMessageSender evo) {
    final MessageType type = converted.getType();

    final boolean isPrompt =
        Objects.isNull(converted.getTransactionPayloadDto())
            && !type.equals(MessageType.listResponseMessage);
    if (isPrompt) {
      // emitter.send(converted);
      return res.send(ENVIA_PROMPT, type, CREATED);
    }

    final boolean isTransactionResponse =
        !Objects.isNull(converted.getTransactionPayloadDto())
            && type.equals(MessageType.listResponseMessage);
    if (isTransactionResponse) {
      final String data = converted.getData().toLowerCase();
      if (data.contains("confirmar")) {
        transactionService.createOne(
            TransactionConverter.toEntity(converted.getTransactionPayloadDto(), user));
        evo.send("Registro incluído;");
        return res.send(CONFIRMA_TRANSACAO, type, CREATED);
      }

      if (data.contains("cancelar")) {
        evo.send("Registro cancelado;");
        return res.send(CANCELA_TRANSACAO, type, NO_CONTENT);
      }

      return res.send(ERRO_VALIDACAO_RESPOSTA_TRANSACAO, type, BAD_REQUEST);
    }

    return res.send(CodigosDeResposta.CASO_DESCONHECIDO, type, BAD_REQUEST);
  }

  private boolean isInvestorProfilePending(User user) {
    return Objects.isNull(user.getPerfilInvestidor())
        || user.getPerfilInvestidor().equals(PerfilInvestidorType.CADASTRO_PENDENTE);
  }
}
