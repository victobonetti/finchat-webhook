package br.com.kod3;

import static br.com.kod3.models.evolution.requestpayload.converter.EvolutionPayloadConverter.parse;

import br.com.kod3.models.evolution.list.EvolutionListFactory;
import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.TextMessageDto;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.transaction.TransactionConverter;
import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
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

  @POST
  @Path("webhook")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  public Response webhook(@Valid WebhookBodyDto body) {
    var response = Response.ok();

    final ConvertedDto converted = parse(body);
    response.header("FC-X-TYPE", converted.getType());

    final String phone = converted.getTelefone();
    final Optional<User> userOption = userService.findByPhone(phone);

    if (userOption.isEmpty()) {
      var msg = new TextMessageDto(phone, "sem registro.");
      evolutionApiService.sendMessage(msg);
      return response.build();
    }

    final var user = userOption.get();

    if (Objects.isNull(user.getPerfilInvestidor())) {
      var msg = new TextMessageDto(phone, "Vamos precisar cadastrar o seu perfil de investidor");
      evolutionApiService.sendMessage(msg);
      userService.atualizaPerfilInvestidor(user, PerfilInvestidorType.CADASTRO_PENDENTE);
    }

    if (user.getPerfilInvestidor().equals(PerfilInvestidorType.CADASTRO_PENDENTE)) {
      if (!converted.getType().equals(MessageType.listResponseMessage)) {
        evolutionApiService.sendPool(EvolutionListFactory.getPerfilInvestidorPool(phone));
        return response.build();
      }

      if (PerfilInvestidorType.getValidPerfisList().contains(converted.getData().toUpperCase())) {
        userService.atualizaPerfilInvestidor(
            user, PerfilInvestidorType.fromDescricao(converted.getData()));
        var msg = new TextMessageDto(phone, "Agora você tem acesso total ao sistema");
        evolutionApiService.sendMessage(msg);
        return response.build();
      }
    }

    if (converted.getType().equals(MessageType.listResponseMessage)) {
      if (converted.getData().contains("Confirmar")) {
        var tran = converted.getTransactionPayloadDto();
        Objects.requireNonNull(tran);
        transactionService.createOne(TransactionConverter.toEntity(tran, user));
        var msg = new TextMessageDto(phone, "Registro incluído;");
        evolutionApiService.sendMessage(msg);
      }

      if (converted.getData().contains("Cancelar")) {
        var msg = new TextMessageDto(phone, "Registro cancelado;");
        evolutionApiService.sendMessage(msg);
      }

      return response.build();
    }

    // Posta mensagem na fila

    return response.build();
  }
}
