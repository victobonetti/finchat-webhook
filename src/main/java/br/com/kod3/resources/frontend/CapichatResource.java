package br.com.kod3.resources.frontend;

import br.com.kod3.models.transaction.Transaction;
import br.com.kod3.models.transaction.TransactionsParams;
import br.com.kod3.services.transaction.TransactionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("v1/capi")
@RequestScoped
public class CapichatResource {

  private final JsonWebToken jwt;
  private final TransactionService transactionService;

  @Inject
  public CapichatResource(JsonWebToken jwt, TransactionService transactionService) {
    this.jwt = jwt;
    this.transactionService = transactionService;
  }

  @POST
  @Path("transactions")
  @RolesAllowed({"User"})
  public Response transactions(@Valid TransactionsParams param) {
    var uid = jwt.getName();
    var transactions = transactionService.getTransactions(param, uid);
    return Response.ok().entity(transactions).build();
  }
}
