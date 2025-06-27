package br.com.kod3.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

  @Override
  public void filter(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    responseContext
        .getHeaders()
        .add("Access-Control-Allow-Origin", "*"); // Or restrict to your frontend domain
    responseContext
        .getHeaders()
        .add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    responseContext
        .getHeaders()
        .add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
  }
}
