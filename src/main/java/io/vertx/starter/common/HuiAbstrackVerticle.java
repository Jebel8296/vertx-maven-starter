package io.vertx.starter.common;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by binger on 2017/9/5.
 */
public class HuiAbstrackVerticle extends AbstractVerticle {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  protected MongoClient mongoClient = null;

  @Override
  public void start() throws Exception {
    super.start();
    JsonObject mongoConfig = new JsonObject();
    mongoConfig.put("connection_string", "mongodb://localhost:27017");
    mongoConfig.put("db_name", "huixiang");
    mongoClient = MongoClient.createShared(vertx, mongoConfig);
  }

  protected void enableCors(Router router) {
    Set<String> allowHeaders = new HashSet<>();
    allowHeaders.add("X-PINGARUNER");
    allowHeaders.add("Content-Type");
    Set<HttpMethod> allowMethods = new HashSet<>();
    allowMethods.add(HttpMethod.GET);
    allowMethods.add(HttpMethod.POST);
    allowMethods.add(HttpMethod.DELETE);
    allowMethods.add(HttpMethod.OPTIONS);
    router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));
  }
}
