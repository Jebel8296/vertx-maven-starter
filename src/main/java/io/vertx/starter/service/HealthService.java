package io.vertx.starter.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.starter.common.GlobalVarialble;
import io.vertx.starter.common.HuiResteasyAbstrack;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by binger on 2017/9/5.
 */
@Path("/micro/v1")
public class HealthService extends HuiResteasyAbstrack{

  @GET
  @Path("/health")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void health(@Suspended final AsyncResponse asyncResponse, @Context Vertx vertx) {
    send(vertx, GlobalVarialble.VERTX_ADDRESS_HEALTH,new JsonObject(),asyncResponse);
  }
}
