package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import io.vertx.starter.gateway.GatewayVerticle;
import io.vertx.starter.service.HealthService;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

public class MainVerticle {

  private Logger log = LoggerFactory.getLogger(MainVerticle.class.getName());

  public static void main(String[] args) throws Exception {

    VertxResteasyDeployment vertxResteasyDeployment = new VertxResteasyDeployment();
    vertxResteasyDeployment.start();
    vertxResteasyDeployment.getRegistry().addPerInstanceResource(HealthService.class);

    ClusterManager mgr = new HazelcastClusterManager();
    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(GatewayVerticle.class.getName());
      } else {
        // failed!
      }
    });
  }
}
