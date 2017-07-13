package io.vertx.starter.consumer;

import io.vertx.starter.rabbit.CRabbitmqVerticle;

/**
 * Created by binger on 2017/7/12.
 */
public class ConsumerVerticle extends CRabbitmqVerticle {

  @Override
  public void start() throws Exception {
    super.start();

    rabbitMQClient.basicGet("xu.queue", false, getResult -> {
      if (getResult.succeeded()) {
        log.info(">>>>>>>>Result:" + getResult.result().encode());
      } else {
        getResult.cause().printStackTrace();
      }
    });
  }
}
