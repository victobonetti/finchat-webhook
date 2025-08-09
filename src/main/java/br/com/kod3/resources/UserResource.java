package br.com.kod3.resources;

import br.com.kod3.models.streak.StreakResponseDto;
import br.com.kod3.services.detail.DetailingService;
import br.com.kod3.services.evolution.EvolutionApiService;
import br.com.kod3.services.streak.StreakService;
import br.com.kod3.services.user.UserService;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.Objects;

@Path("v1/user")
public class UserResource {
    @Inject
    DetailingService detailingService;
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
    @Path("{uid}/debts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDebtsFromUser(@PathParam("uid") String uid) {
        Objects.requireNonNull(uid);

        return Response.ok(detailingService.getFormattedDebts(uid)).build();
    }

}
