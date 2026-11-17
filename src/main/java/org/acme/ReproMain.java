package org.acme;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.core.Vertx;
import jakarta.inject.Inject;

@QuarkusMain
public class ReproMain implements QuarkusApplication {

    @Inject
    Vertx vertx;

    @Override
    public int run(String... args) {
        System.out.println("OK: Vert.x started, " + vertx);
        return 0;
    }
}
