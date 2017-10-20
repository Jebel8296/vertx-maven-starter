package io.vertx.starter.text.config;

import io.vertx.core.json.JsonObject;
import io.vertx.starter.common.GlobalVarialble;
import io.vertx.starter.common.HuiAbstrackVerticle;

/**
 * @author xuxg
 */
public class ZookeeperConfigTest extends HuiAbstrackVerticle {
  @Override
  public void start() throws Exception {

    vertx.eventBus().consumer(GlobalVarialble.ADDRESS_TEST_ZOOKEEPERCONFIG, message -> {
      JsonObject jsonObject = (JsonObject) message.body();
      logger.info(jsonObject.encode());

      getConfigFromZookeeper(jsonObject.getString("key")).setHandler(handler -> {
        if (handler.succeeded()) {
          JsonObject result = handler.result();
          message.reply(result);
        } else {
          JsonObject result = new JsonObject();
          result.put("code", "0");
          message.reply(result);
        }
      });
    });

    logger.info("ZookeeperConfigTest Started.");
  }

}
