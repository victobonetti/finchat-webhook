package br.com.kod3.resources;

import static br.com.kod3.services.util.CodigosDeResposta.*;
import static br.com.kod3.services.util.Messages.*;
import static br.com.kod3.services.util.Util.getPhone;

import br.com.kod3.batch.Batch;
import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.models.evolution.requestpayload.WebhookBodyDto;
import br.com.kod3.models.evolution.requestpayload.converter.ConvertedDto;
import br.com.kod3.models.evolution.requestpayload.converter.EvolutionPayloadConverter;
import br.com.kod3.models.util.enums.TransactionType;
import br.com.kod3.models.user.User;
import br.com.kod3.services.util.CodigosDeResposta;
import br.com.kod3.services.debt.DebtService;
import br.com.kod3.services.evolution.EvolutionApiService;
import br.com.kod3.services.evolution.EvolutionMessageSender;
import br.com.kod3.services.recurrence.RecurrenceService;
import br.com.kod3.services.util.ResponseHandler;
import br.com.kod3.services.transaction.TransactionService;
import br.com.kod3.services.user.UserService;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("v1/webhook")
public class MainResource {

    private final UserService userService;
    private final EvolutionApiService evolutionApiService;
    private final ResponseHandler res;
    private final EvolutionPayloadConverter converter;
    private final TransactionService transactionService;
    private final DebtService debtService;
    private final RecurrenceService recurrenceService;
    private final Batch batch;

    @Inject
    @Broadcast
    @Channel("my-channel")
    Emitter<ConvertedDto> emitter;

    @Inject
    public MainResource(
            UserService userService,
            EvolutionApiService evolutionApiService,
            ResponseHandler res,
            EvolutionPayloadConverter converter,
            TransactionService transactionService,
            DebtService debtService,
            RecurrenceService recurrenceService,
            Batch batch) {
        this.userService = userService;
        this.evolutionApiService = evolutionApiService;
        this.res = res;
        this.converter = converter;
        this.transactionService = transactionService;
        this.debtService = debtService;
        this.recurrenceService = recurrenceService;
        this.batch = batch;
    }

    @POST
    @Path("batch")
    public Response batch() {
        batch.generateRecorrentTransactions();
        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response webhook(WebhookBodyDto body) {

        final EvolutionMessageSender evo = new EvolutionMessageSender(evolutionApiService, getPhone(body));
        final ConvertedDto converted = convert(body, evo);
        final String phone = converted.getTelefone();
        final Optional<User> userOptional = userService.findByPhone(phone);

        try {
            if (userOptional.isEmpty()) {
                return handleNewUser(converted.getType(), evo);
            }

            final User user = userOptional.get();
            converted.setUserId(user.getId());
            return handleRegisteredUser(user, converted, evo);

        } catch (Exception e) {
            Log.info(erro_interno);
            Log.error(e);
            evo.send(erro_interno);
            return res.send(ERRO_INTERNO, null);
        }
    }

    private ConvertedDto convert(WebhookBodyDto body, EvolutionMessageSender evo) {
        ConvertedDto converted;
        try {
            converted = converter.parse(body);
        } catch (RuntimeException r) {
            evo.send(erro_parse);
            throw r;
        }
        return converted;
    }

    private Response handleNewUser(MessageType type, EvolutionMessageSender evo) {
        evo.send(usuario_sem_registro);
        return res.send(SOLICITA_CADASTRO, type);
    }

    private Response handleRegisteredUser(User user, ConvertedDto converted, EvolutionMessageSender evo) {
        final MessageType type = converted.getType();
        final boolean isPrompt = Objects.isNull(converted.getTransactionPayloadDto()) && !type.equals(MessageType.listResponseMessage);

        if (isPrompt) {
            evo.like(converted.getRemoteJid(), converted.getMessageId());
            emitter.send(converted);
            return res.send(ENVIA_PROMPT, type);
        }

        if (Objects.isNull(converted.getTransactionPayloadDto()) || !type.equals(MessageType.listResponseMessage)) {
            evo.send(erro_validacao_resposta_transacao);
            return res.send(CodigosDeResposta.CASO_DESCONHECIDO, type);
        }

        return res.send(exec(user, converted, evo), type);
    }

    private CodigosDeResposta exec(User user, ConvertedDto converted, EvolutionMessageSender evo) {
        Objects.requireNonNull(converted.getTransactionPayloadDto());
        var t = converted.getTransactionPayloadDto().getType();

        if (t.equals(TransactionType.INCOME) || t.equals(TransactionType.EXPENSE)) {
            return transactionService.handle(converted, user, evo);
        } else if (t.equals(TransactionType.DEBT)) {
            return debtService.handle(converted, user, evo);
        } else if (t.equals(TransactionType.RECURRING_EXPENSE) || t.equals(TransactionType.RECURRING_INCOME)) {
            return recurrenceService.handle(converted, user, evo);
        }

        evo.send(erro_validacao_resposta_transacao);
        return ERRO_VALIDACAO_RESPOSTA_TRANSACAO;
    }

}
