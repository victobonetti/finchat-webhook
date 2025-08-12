package br.com.kod3.services.util;

import static jakarta.ws.rs.core.Response.Status.OK;

import br.com.kod3.models.evolution.requestpayload.MessageType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class ResponseHandler {
  public Response send(CodigoDeResposta c, MessageType t) {
    return Response.status(OK)
        .header("FC-X-TYPE", t)
        .header("FC-X-CODE", c.codigo.toString())
        .build();
  }
}
