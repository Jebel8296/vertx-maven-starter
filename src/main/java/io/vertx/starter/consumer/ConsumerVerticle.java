package io.vertx.starter.consumer;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.starter.rabbit.CRabbitmqVerticle;

import java.util.Optional;

/**
 * Created by binger on 2017/7/12.
 */
public class ConsumerVerticle extends CRabbitmqVerticle {


  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();

    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorker(false);

    vertx.deployVerticle(ConsumerVerticle.class.getName(), deploymentOptions);

  }

  @Override
  public void start() throws Exception {
    super.start();


    vertx.eventBus().consumer("xu.address", msg -> {
      JsonObject jsonObject = (JsonObject) msg.body();
      log.info("EventBus Got message: " + jsonObject.getString("body"));
      rabbitMQClient.basicAck(jsonObject.getLong("deliveryTag"), false, jsonObjectAsyncResult -> {
      });
    });

    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.route().handler(CookieHandler.create());

    router.get("/publish").handler(routingContext -> {
      HttpServerResponse httpServerResponse = routingContext.response();
      httpServerResponse.putHeader("ontent-type", "application/json;charset=UTF-8");

      rabbitMQClient.basicGet("xu.queue", true, getResult -> {
        if (getResult.succeeded()) {
          Optional<JsonObject> result = Optional.ofNullable(getResult.result());
          if (result.isPresent()) {
            httpServerResponse.end(result.get().encode());
          } else {
            httpServerResponse.end("No Data");
          }
        } else {
          getResult.cause().printStackTrace();
          httpServerResponse.setStatusCode(500).end("Publish Failing");
        }
      });
    });
    httpServer.requestHandler(router::accept).listen(5051);
  }
}
