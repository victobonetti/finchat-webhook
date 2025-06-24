package br.com.kod3;

import br.com.kod3.models.evolution.WebhookBodyDto;
import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.user.User;
import br.com.kod3.services.TransactionService;
import br.com.kod3.services.UserService;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static br.com.kod3.models.evolution.EvolutionPayloadConverter.parse;

@Path("v1")
public class MainResource {

    @Inject
    UserService userService;

    @Inject
    TransactionService transactionService;

    @POST
    @Path("webhook")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes
    public Response webhook(@Valid WebhookBodyDto body) {
        return Response.ok().header("FC-X-TYPE", parse(body).getType()).build();
    }

}
