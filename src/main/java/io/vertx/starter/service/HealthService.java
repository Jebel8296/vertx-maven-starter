package io.vertx.starter.service;

import io.vertx.core.json.JsonObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by binger on 2017/9/5.
 */
@Path("/micro/v1")
public class HealthService {

  @GET
  @Path("/health")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response doGet() {
    JsonObject message = new JsonObject();
    message.put("status", "up");
    message.put("timestamp", System.currentTimeMillis());
    return Response.status(200).entity(message.encodePrettily()).build();
  }
}
