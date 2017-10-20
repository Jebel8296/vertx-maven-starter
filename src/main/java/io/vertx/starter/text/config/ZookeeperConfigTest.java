package io.vertx.starter.text.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
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

      ConfigStoreOptions storeOptions = new ConfigStoreOptions();
      storeOptions.setType("zookeeper");
      storeOptions.setConfig(new JsonObject().put("connection", "192.168.108.171:2181").put("path", "/" + jsonObject.getString("key")));
      configRetriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(storeOptions));
      configRetriever.getConfig(handler -> {
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
