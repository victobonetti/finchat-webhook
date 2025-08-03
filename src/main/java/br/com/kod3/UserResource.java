package br.com.kod3;

import br.com.kod3.models.evolution.list.EvolutionListFactory;
import br.com.kod3.models.streak.StreakResponseDto;
import br.com.kod3.models.user.PerfilInvestidorType;
import br.com.kod3.models.user.User;
import br.com.kod3.models.user.UserDataDto;
import br.com.kod3.services.*;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static br.com.kod3.services.Messages.solicita_perfil_investidor;

@Path("v1/user")
public class UserResource {
    @Inject
    DetailingService detailingService;
    @Inject
    UserService userService;
    @Inject
    EvolutionApiService evolutionApiService;
    @Inject
    StreakService streakService;

    @GET
    @Path("/{uid}/streak")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserStreak(@QueryParam("uid") String userId){
        Log.info("Obtendo streaks para usuario " + userId);
        return Response.ok().entity(new StreakResponseDto(userId, streakService.getStreakFromUserId(userId))).build();
    }

    @GET
    @Path("{uid}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsFromUser(@PathParam("uid") String uid, @QueryParam("dtIni") LocalDate dtIni, @QueryParam("dtFim") LocalDate dtFim) {
        Objects.requireNonNull(uid);
        Objects.requireNonNull(dtIni);
        Objects.requireNonNull(dtFim);

        var response = detailingService.getFormattedTransactions(uid, dtIni, dtFim);
        return Response.ok(response).build();
    }

    @GET
    @Path("{uid}/recurring-transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecorrenciasFromUser(@PathParam("uid") String uid) {
        Objects.requireNonNull(uid);

        return Response.ok(detailingService.getFormattedRecorrencias(uid)).build();
    }

    @GET
    @Path("{uid}/recurring-transactions/{recId}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsFromRecorrencia(@PathParam("uid") String uid, @QueryParam("recId") String recId) {
        Objects.requireNonNull(uid);
        Objects.requireNonNull(recId);

        return Response.ok(detailingService.getFormattedRecorrencias(uid, recId)).build();
    }

    @GET
    @Path("{uid}/debts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDebtsFromUser(@PathParam("uid") String uid) {
        Objects.requireNonNull(uid);

        return Response.ok(detailingService.getFormattedDebts(uid)).build();
    }

    @GET
    @Path("{uid}/debts/{debtId}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsFromDebt(@PathParam("uid") String uid, @QueryParam("debtId") String debtId) {
        Objects.requireNonNull(uid);

        return Response.ok(detailingService.getFormattedDebts(uid, debtId)).build();
    }

    @POST
    @Path("/{uid}/ask-investor-profile")
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
            evo.opts(EvolutionListFactory.getPerfilInvestidorList(body.telefone()));
        }

        return Response.ok().build();
    }
}
