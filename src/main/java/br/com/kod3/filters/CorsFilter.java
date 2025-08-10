package br.com.kod3.filters;

import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CORS filter and full request/response logger.
 */
@Provider
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger LOG = Logger.getLogger(CorsFilter.class.getName());

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    StringBuilder logMessage = new StringBuilder();
    logMessage.append("\n--- Incoming Request ---\n");
    logMessage.append("Endpoint: ")
            .append(requestContext.getUriInfo().getAbsolutePath())
            .append("\n");
    logMessage.append("Method: ").append(requestContext.getMethod()).append("\n");

    logMessage.append("Headers: {\n");
    requestContext.getHeaders()
            .forEach((key, value) -> logMessage.append("  ")
                    .append(key)
                    .append(": ")
                    .append(value)
                    .append("\n"));
    logMessage.append("}\n");

    if (requestContext.hasEntity()) {
      byte[] requestBodyBytes = requestContext.getEntityStream().readAllBytes();
      String requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
      logMessage.append("Body:\n").append(requestBody).append("\n");
      requestContext.setEntityStream(new ByteArrayInputStream(requestBodyBytes));
    } else {
      logMessage.append("Body: [No Body Present]\n");
    }

    logMessage.append("----------------------------------");
    LOG.log(Level.INFO, logMessage.toString());
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
          throws IOException {

    // Add CORS headers
    responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

    // Log outgoing response
    StringBuilder logMessage = new StringBuilder();
    logMessage.append("\n--- Outgoing Response ---\n");
    logMessage.append("Endpoint: ").append(requestContext.getUriInfo().getAbsolutePath()).append("\n");
    logMessage.append("Status: ").append(responseContext.getStatus()).append("\n");

    logMessage.append("Headers: {\n");
    responseContext.getHeaders()
            .forEach((key, value) -> logMessage.append("  ")
                    .append(key)
                    .append(": ")
                    .append(value)
                    .append("\n"));
    logMessage.append("}\n");

    if (responseContext.hasEntity()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStream originalStream = responseContext.getEntityStream();
      responseContext.setEntityStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          baos.write(b);
          originalStream.write(b);
        }
      });

      responseContext.setEntityStream(originalStream);
      String body = baos.toString(StandardCharsets.UTF_8);
      logMessage.append("Body:\n").append(body).append("\n");
    } else {
      logMessage.append("Body: [No Body Present]\n");
    }

    logMessage.append("----------------------------------");
    LOG.log(Level.INFO, logMessage.toString());
  }
}
