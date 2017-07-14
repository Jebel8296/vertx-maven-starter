package io.vertx.starter.publish;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.starter.rabbit.PRabbitmqVerticle;

/**
 * Created by binger on 2017/7/12.
 */
public class PublishVerticle extends PRabbitmqVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();

    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorker(false);

    vertx.deployVerticle(PublishVerticle.class.getName());
  }


  @Override
  public void start() throws Exception {
    super.start();

    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.route().handler(CookieHandler.create());

    router.post("/publish").handler(routingContext -> {
      HttpServerResponse httpServerResponse = routingContext.response();
      httpServerResponse.putHeader("ontent-type", "application/json;charset=UTF-8");

      JsonObject body = routingContext.getBodyAsJson();

      JsonObject message = new JsonObject();
      message.put("body", body.encode());
      rabbitMQClient.confirmSelect(confirmSelectResult -> {
        if (confirmSelectResult.succeeded()) {
          rabbitMQClient.basicPublish("xu.exchange", "xu.routingkey.t1", message, publishResult -> {
            if (publishResult.succeeded()) {
              rabbitMQClient.waitForConfirms(waitForConfirmResult -> {
                if (waitForConfirmResult.succeeded()) {
                  log.info("publish successfully.");
                  httpServerResponse.end("Publish Successfully");
                } else {
                  waitForConfirmResult.cause().printStackTrace();
                  httpServerResponse.setStatusCode(500).end("Publish Failing");
                }
              });
            } else {
              publishResult.cause().printStackTrace();
              httpServerResponse.setStatusCode(500).end("Publish Failing");
            }
          });
        } else {
          confirmSelectResult.cause().printStackTrace();
          httpServerResponse.setStatusCode(500).end("Publish Failing");
        }
      });
    });
    httpServer.requestHandler(router::accept).listen(5050);
  }
}
