package br.com.kod3.filters;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Provider
public class HttpClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

  private static final Logger LOG = Logger.getLogger(HttpClientLoggingFilter.class.getName());

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("\n=== [HTTP Client Request] ===\n");
    sb.append("Method: ").append(requestContext.getMethod()).append("\n");
    sb.append("URL: ").append(requestContext.getUri()).append("\n");

    sb.append("Headers:\n");
    requestContext
        .getHeaders()
        .forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));

    if (requestContext.hasEntity()) {
      String body = requestContext.getEntity().toString();
      sb.append("Body:\n").append(body).append("\n");
    }

    LOG.info(sb.toString());
  }

  @Override
  public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
      throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("\n=== [HTTP Client Response] ===\n");
    sb.append("Status: ").append(responseContext.getStatus()).append("\n");

    sb.append("Headers:\n");
    responseContext
        .getHeaders()
        .forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append("\n"));

    if (responseContext.hasEntity()) {
      byte[] bodyBytes = responseContext.getEntityStream().readAllBytes();
      String body = new String(bodyBytes, StandardCharsets.UTF_8);
      sb.append("Body:\n").append(body.isBlank() ? "[Empty]" : body).append("\n");

      responseContext.setEntityStream(new ByteArrayInputStream(bodyBytes)); // reset
    }

    LOG.info(sb.toString());
  }
}
