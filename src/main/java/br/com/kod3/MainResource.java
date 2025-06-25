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
import br.com.kod3.services.CodigosDeResposta;
import br.com.kod3.services.EvolutionApiService;
import br.com.kod3.services.TransactionService;
import br.com.kod3.services.UserService;
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

  @POST
  @Path("webhook")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  public Response webhook(@Valid WebhookBodyDto body) {

    final ConvertedDto converted = converter.parse(body);
    final MessageType type = converted.getType();

    final String phone = converted.getTelefone();
    final Optional<User> userOption = userService.findByPhone(phone);

    // User dos not exists
    if (userOption.isEmpty()) {
      var msg = new TextMessageDto(phone, "sem registro.");
      evolutionApiService.sendMessage(msg);

      return res.send(SOLICITA_CADASTRO, type, NO_CONTENT);
    }

    final var user = userOption.get();

    if (Objects.isNull(user.getPerfilInvestidor())) {
      var msg = new TextMessageDto(phone, "Vamos precisar cadastrar o seu perfil de investidor");
      evolutionApiService.sendMessage(msg);
      userService.atualizaPerfilInvestidor(user, PerfilInvestidorType.CADASTRO_PENDENTE);
      evolutionApiService.sendPool(EvolutionListFactory.getPerfilInvestidorPool(phone));

      return res.send(INSERE_PENDENCIA_E_SOLICITA_PERFIL, type, CREATED);
    }

    if (user.getPerfilInvestidor().equals(PerfilInvestidorType.CADASTRO_PENDENTE)) {
      if (!converted.getType().equals(MessageType.listResponseMessage)) {
        evolutionApiService.sendPool(EvolutionListFactory.getPerfilInvestidorPool(phone));

        return res.send(CodigosDeResposta.SOLICITA_PERFIL, type, OK);
      }

      if (PerfilInvestidorType.getValidPerfisList().contains(converted.getData().toUpperCase())) {
        userService.atualizaPerfilInvestidor(
            user, PerfilInvestidorType.fromDescricao(converted.getData()));
        var msg = new TextMessageDto(phone, "Agora você tem acesso total ao sistema");
        evolutionApiService.sendMessage(msg);

        return res.send(CONFIRMA_PERFIL, type, CREATED);
      }
    }

    if (converted.getType().equals(MessageType.listResponseMessage)) {
      if (converted.getData().toLowerCase().contains("confirmar")) {
        var tran = converted.getTransactionPayloadDto();
        Objects.requireNonNull(tran);
        transactionService.createOne(TransactionConverter.toEntity(tran, user));
        var msg = new TextMessageDto(phone, "Registro incluído;");
        evolutionApiService.sendMessage(msg);

        return res.send(CONFIRMA_TRANSACAO, type, CREATED);
      }

      if (converted.getData().toLowerCase().contains("cancelar")) {
        var msg = new TextMessageDto(phone, "Registro cancelado;");
        evolutionApiService.sendMessage(msg);

        return res.send(CANCELA_TRANSACAO, type, NO_CONTENT);
      }

      return res.send(CodigosDeResposta.ERRO, type, Response.Status.BAD_REQUEST);
    }

    // Posta mensagem na fila

    return res.send(CodigosDeResposta.ERRO, type, Response.Status.BAD_REQUEST);
  }
}
