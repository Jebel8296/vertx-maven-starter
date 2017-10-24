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
    super.start();
    vertx.eventBus().consumer(GlobalVarialble.ADDRESS_TEST_ZOOKEEPERCONFIG, message -> {
      JsonObject jsonObject = (JsonObject) message.body();
      logger.info(jsonObject.encode());

      getConfigFromZookeeper(jsonObject.getString("key")).setHandler(handler -> {
        JsonObject reply = new JsonObject();
        reply.put("code", "0");
        if (handler.succeeded()) {
          JsonObject result = handler.result();
          if (!result.isEmpty()) {
            logger.info("ZookeeperConfigTest," + Thread.currentThread().getName());
            message.reply(result);
          } else {
            message.reply(reply);
          }
        } else {
          message.reply(reply);
        }
      });
    });
    logger.info("【" + Thread.currentThread().getName() + "】：" + "ZookeeperConfigTest Started.");
  }

}
