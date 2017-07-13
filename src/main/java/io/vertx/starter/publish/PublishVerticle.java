package io.vertx.starter.publish;

import io.vertx.core.json.JsonObject;
import io.vertx.starter.rabbit.PRabbitmqVerticle;

/**
 * Created by binger on 2017/7/12.
 */
public class PublishVerticle extends PRabbitmqVerticle {

  @Override
  public void start() throws Exception {
    super.start();

    JsonObject message = new JsonObject();
    message.put("body", "hello xu.");
    rabbitMQClient.basicPublish("xu.exchange", "xu.routingkey.t1", message, publishResult -> {
      if (publishResult.succeeded()) {
        log.info("publish successfully.");
      } else {
        publishResult.cause().printStackTrace();
      }
    });
  }
}
