package br.com.kod3.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This filter handles both CORS (Cross-Origin Resource Sharing) headers and logs incoming request
 * details for debugging purposes. It implements both ContainerRequestFilter (for logging) and
 * ContainerResponseFilter (for CORS).
 */
@Provider
public class CorsFilter implements ContainerResponseFilter, ContainerRequestFilter {

  private static final Logger LOG = Logger.getLogger(CorsFilter.class.getName());

  /**
   * This method is called for every incoming request before it reaches the resource method. It logs
   * the request URI, method, headers, and body.
   *
   * @param requestContext The context of the incoming request.
   * @throws IOException If an IO error occurs while reading the request body.
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    StringBuilder logMessage = new StringBuilder();
    logMessage.append("\n--- Incoming Request ---\n");
    logMessage.append("URI: ").append(requestContext.getUriInfo().getAbsolutePath()).append("\n");
    logMessage.append("Method: ").append(requestContext.getMethod()).append("\n");

    logMessage.append("Headers: {\n");
    requestContext
        .getHeaders()
        .forEach(
            (key, value) ->
                logMessage.append("  ").append(key).append(": ").append(value).append("\n"));
    logMessage.append("}\n");

    if (requestContext.hasEntity()) {
      byte[] requestBodyBytes = requestContext.getEntityStream().readAllBytes();

      String requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
      logMessage.append("Body: \n").append(requestBody).append("\n");

      requestContext.setEntityStream(new ByteArrayInputStream(requestBodyBytes));
    } else {
      logMessage.append("Body: [No Body Present]\n");
    }

    logMessage.append("----------------------------------\n");

    LOG.log(Level.INFO, logMessage.toString());
  }

  /**
   * This method is called for every outgoing response. It adds the necessary CORS headers to allow
   * cross-origin requests.
   *
   * @param requestContext The context of the original request.
   * @param responseContext The context of the outgoing response.
   */
  @Override
  public void filter(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    // Allows any domain to access the resources. For production, you should
    // restrict this to your frontend's actual domain.
    // e.g., "https://your-frontend-app.com"
    responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

    responseContext
        .getHeaders()
        .add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");

    responseContext
        .getHeaders()
        .add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
  }
}
