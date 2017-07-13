package io.vertx.starter.rabbit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rabbitmq.RabbitMQClient;

/**
 * Created by binger on 2017/7/12.
 */
public abstract class PRabbitmqVerticle extends AbstractVerticle {

  protected Logger log = LoggerFactory.getLogger(getClass());
  protected RabbitMQClient rabbitMQClient;

  @Override
  public void start() throws Exception {
    super.start();
    rabbitMQClient = RabbitMQClient.create(vertx, config());
    rabbitMQClient.start(startResult -> {
      if (startResult.succeeded()) {
        log.info("publish rabbitmq started.");
        rabbitMQClient.exchangeDeclare("xu.exchange", "fanout", false, false, exchangeResult -> {
          if (exchangeResult.succeeded()) {
            log.info("xu.exchange declare successfully.");

            rabbitMQClient.queueDeclare("xu.queue", false, false, false, queueResult -> {
              if (queueResult.succeeded()) {
                log.info("my.queue declare successfully.");

                rabbitMQClient.queueBind("xu.queue", "xu.exchange", "xu.routingkey.t1", bindResult -> {
                  if (bindResult.succeeded()) {
                    log.info("queue binded.");
                  } else {
                    bindResult.cause().printStackTrace();
                  }
                });
              }
            });
          } else {
            exchangeResult.cause().printStackTrace();
          }
        });
      } else {
        startResult.cause().printStackTrace();
      }
    });
  }
}
