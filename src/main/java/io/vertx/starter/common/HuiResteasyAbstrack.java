package io.vertx.starter.common;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

/**
 * Created by binger on 2017/9/5.
 */
public abstract class HuiResteasyAbstrack {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  protected DeliveryOptions deliveryOptions = new DeliveryOptions().setSendTimeout(5000L);

  protected void send(Vertx vertx, String address, JsonObject param, AsyncResponse asyncResponse) {
    vertx.eventBus().send(address, param, deliveryOptions, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {
        JsonObject result = (JsonObject) messageAsyncResult.result().body();
        logger.info(result);
        if (result != null) {
          asyncResponse.resume((result.encodePrettily()));
        } else {
          asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
        }
      } else {
        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }
}
