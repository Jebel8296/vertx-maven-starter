package io.vertx.starter.verticle;

import io.vertx.core.json.JsonObject;
import io.vertx.starter.common.GlobalVarialble;
import io.vertx.starter.common.HuiAbstrackVerticle;

/**
 * Created by binger on 2017/9/5.
 */
public class HealthVerticle extends HuiAbstrackVerticle {
  @Override
  public void start() throws Exception {
    super.start();

    vertx.eventBus().consumer(GlobalVarialble.VERTX_ADDRESS_HEALTH, message -> {
      JsonObject jsonObject = (JsonObject) message.body();
      logger.info(jsonObject.encode());
      JsonObject result = new JsonObject();
      result.put("status", "up");
      result.put("timestamp", System.currentTimeMillis());
      JsonObject reply = new JsonObject();
      reply.put("code", 200);
      reply.put("message", result);
      message.reply(reply);
    });

    logger.info("HealthVerticle Started.");
  }
}
