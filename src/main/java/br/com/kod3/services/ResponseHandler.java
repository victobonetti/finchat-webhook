package br.com.kod3.services;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ResponseHandler {
  public Response send(CodigosDeResposta c, MessageType t, Response.Status s) {
    return Response.status(s).header("FC-X-TYPE", t).header("FC-X-CODE", c.toCode()).build();
  }
}
