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

    // Log Headers
    logMessage.append("Headers: {\n");
    requestContext
        .getHeaders()
        .forEach(
            (key, value) ->
                logMessage.append("  ").append(key).append(": ").append(value).append("\n"));
    logMessage.append("}\n");

    // --- Log Request Body ---
    // The request entity stream can only be read once. To log the body, we must
    // read the stream, store its content, and then replace the original stream
    // with a new one containing the stored content. This allows the JAX-RS
    // framework and your application to read the body as if it were untouched.
    if (requestContext.hasEntity()) {
      // Read the original stream into a byte array
      byte[] requestBodyBytes = requestContext.getEntityStream().readAllBytes();

      // Convert byte array to a string for logging
      String requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
      logMessage.append("Body: \n").append(requestBody).append("\n");

      // IMPORTANT: Replace the consumed stream with a new one
      requestContext.setEntityStream(new ByteArrayInputStream(requestBodyBytes));
    } else {
      logMessage.append("Body: [No Body Present]\n");
    }

    logMessage.append("------------------------\n");

    // Use a logger to print the details. This is better practice than System.out.
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
