package io.vertx.starter.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.starter.common.GlobalVarialble;
import io.vertx.starter.common.HuiResteasyAbstrack;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by binger on 2017/9/5.
 */
@Path("/micro/v1")
public class HealthService extends HuiResteasyAbstrack {

  @GET
  @Path("/health")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void health(@Suspended final AsyncResponse asyncResponse, @Context Vertx vertx) {
    send(vertx, GlobalVarialble.ADDRESS_HEALTH, new JsonObject(), asyncResponse);
  }

  @GET
  @Path("/mongo")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void testMongo(@Suspended final AsyncResponse asyncResponse, @Context Vertx vertx) {
    send(vertx, GlobalVarialble.ADDRESS_TEST_MONGO, new JsonObject(), asyncResponse);
  }

  @GET
  @Path("/config/{key}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void testConfig(@Suspended final AsyncResponse asyncResponse, @PathParam("key") String key, @Context Vertx vertx) {
    send(vertx, GlobalVarialble.ADDRESS_TEST_ZOOKEEPERCONFIG, new JsonObject().put("key", key), asyncResponse);
  }
}
