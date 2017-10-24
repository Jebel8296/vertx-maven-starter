package io.vertx.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import io.vertx.starter.common.HuiAbstrackVerticle;
import io.vertx.starter.gateway.GatewayVerticle;
import io.vertx.starter.text.config.ZookeeperConfigTest;
import io.vertx.starter.verticle.HealthVerticle;

public class MainVerticle extends HuiAbstrackVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    ClusterManager mgr = new HazelcastClusterManager();
    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        DeploymentOptions workDeploymentOptions = new DeploymentOptions();
        workDeploymentOptions.setWorker(true);
        DeploymentOptions eventDeploymentOtions = new DeploymentOptions();
        Vertx vertx = res.result();
        vertx.deployVerticle(GatewayVerticle.class.getName(), eventDeploymentOtions);
        vertx.deployVerticle(HealthVerticle.class.getName(), workDeploymentOptions);
        vertx.deployVerticle(ZookeeperConfigTest.class.getName(), workDeploymentOptions);
      } else {
        // failed!
        res.cause().printStackTrace();
      }
    });
  }
}
