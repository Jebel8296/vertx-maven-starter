package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.consumer.ConsumerVerticle;
import io.vertx.starter.publish.PublishVerticle;

public class MainVerticle {

  private Logger log = LoggerFactory.getLogger(MainVerticle.class.getName());

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(PublishVerticle.class.getName());
    vertx.deployVerticle(ConsumerVerticle.class.getName());
  }

}
