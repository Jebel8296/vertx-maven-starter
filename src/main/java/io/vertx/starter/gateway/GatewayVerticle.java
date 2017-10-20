package io.vertx.starter.gateway;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.starter.common.HuiAbstrackVerticle;
import io.vertx.starter.service.HealthService;
import org.jboss.resteasy.plugins.server.vertx.VertxRegistry;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

/**
 * Created by binger on 2017/9/5.
 */
public class GatewayVerticle extends HuiAbstrackVerticle {

  @Override
  public void start() throws Exception {
    super.start();

    VertxResteasyDeployment vertxResteasyDeployment = new VertxResteasyDeployment();
    vertxResteasyDeployment.start();
    VertxRegistry vertxRegistry = vertxResteasyDeployment.getRegistry();
    //新增服务
    vertxRegistry.addPerInstanceResource(HealthService.class);

    Router router = Router.router(vertx);
    router.route().handler(CookieHandler.create());
    //CORS
    enableCors(router);
    router.route("/micro/v1/*").handler(routingContext -> {
      new VertxRequestHandler(vertx, vertxResteasyDeployment).handle(routingContext.request());
    });
    vertx.createHttpServer().requestHandler(router::accept).listen(9080);
    logger.info("GatewayVerticle Started.");
  }
}
