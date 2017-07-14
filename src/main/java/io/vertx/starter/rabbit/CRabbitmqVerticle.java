package io.vertx.starter.rabbit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rabbitmq.RabbitMQClient;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Created by binger on 2017/7/12.
 */
public abstract class CRabbitmqVerticle extends AbstractVerticle {

  protected Logger log = LoggerFactory.getLogger(getClass());
  protected RabbitMQClient rabbitMQClient;

  @Override
  public void start() throws Exception {
    super.start();
    rabbitMQClient = RabbitMQClient.create(vertx, config());
    rabbitMQClient.start(startResult -> {
      if (startResult.succeeded()) {
        log.info("rabbitmq clent started.");

        rabbitMQClient.basicConsume("xu.queue", "xu.address", consumeResult -> {
          if (consumeResult.succeeded()) {
            log.info("Xu Consume Created");
          } else {
            consumeResult.cause().printStackTrace();
          }
        });
      } else {
        startResult.cause().printStackTrace();
      }
    });
  }
}
