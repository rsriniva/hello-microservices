package com.redhat.training.msa.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void stop() {
        LOG.info("Shutting down Ciao service...");
  }

    @Override
    public void start(Future<Void> future) {

        LOG.info("Welcome to Vertx. Starting Ciao service...");

        Router router = Router.router(vertx);
        router.get("/api/ciao")
              .produces("application/text")
              .handler(rc ->
              {
                String host = rc.request().host();
                rc.response().end("Ciao da " + host +"\n");
              });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

}
