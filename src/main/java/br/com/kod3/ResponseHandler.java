package br.com.kod3;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import br.com.kod3.services.CodigosDeResposta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ResponseHandler {
  public Response send(CodigosDeResposta c, MessageType t, Response.Status s) {
    return Response.status(s).header("FC-X-TYPE", t).header("FC-X-CODE", c.toCode()).build();
  }
}
