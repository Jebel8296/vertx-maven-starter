package io.vertx.starter.verticle;

import io.vertx.core.json.JsonObject;
import io.vertx.starter.common.GlobalVarialble;
import io.vertx.starter.common.HuiAbstrackVerticle;

import java.util.List;

/**
 * Created by binger on 2017/9/5.
 */
public class HealthVerticle extends HuiAbstrackVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    vertx.eventBus().consumer(GlobalVarialble.ADDRESS_HEALTH, message -> {
      JsonObject jsonObject = (JsonObject) message.body();
      logger.info(jsonObject.encode());
      JsonObject result = new JsonObject();
      result.put("status", "up");
      result.put("timestamp", System.currentTimeMillis());
      message.reply(result);
    });

    vertx.eventBus().consumer(GlobalVarialble.ADDRESS_TEST_MONGO, message -> {
      JsonObject jsonObject = (JsonObject) message.body();
      logger.info(jsonObject.encode());

      JsonObject product = new JsonObject();
      product.put("code", "101010");
      product.put("name", "Tonka");

      mongoClient.find("product", new JsonObject().put("code", "101010"), res -> {
        List<JsonObject> resutl = res.result();
        if (resutl.size() > 0) {
          mongoClient.removeDocuments("product", new JsonObject().put("code", "101010"), rs -> {
            if (rs.succeeded()) {
              System.out.println("Prodcut Removed!");
              mongoClient.save("product", product, id -> {
                System.out.println("Inserted Id: " + id.result());
                mongoClient.find("product", new JsonObject().put("code", "101010"), r -> {
                  message.reply(r.result().get(0));
                });
              });
            } else {
              System.out.println("Product Unremoved!");
              message.reply(null);
            }
          });
        } else {
          mongoClient.save("product", product, id -> {
            System.out.println("Inserted Id: " + id.result());
            mongoClient.find("product", new JsonObject().put("code", "101010"), r -> {
              message.reply(r.result().get(0));
            });
          });
        }
      });
    });

    logger.info("HealthVerticle Started.");
  }
}
