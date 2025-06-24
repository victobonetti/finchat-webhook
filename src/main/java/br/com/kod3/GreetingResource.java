package br.com.kod3;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.user.User;
import br.com.kod3.services.TransactionService;
import br.com.kod3.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("v1")
public class GreetingResource {

    @Inject
    UserService userService;

    @Inject
    TransactionService transactionService;

    @POST
    @Path("webhook")
    @Produces(MediaType.APPLICATION_JSON)
    public String webhook() {
        return "webhook";
    }

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return userService.findMany();
    }

    @GET
    @Path("transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> getTransactions() {
        return transactionService.findMany();
    }
}
