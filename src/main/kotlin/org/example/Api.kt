package org.example

import io.javalin.Javalin

fun launchApi() {
    Javalin.create { config ->
        config.requestLogger { ctx, ms ->
            println(ctx)
            println(ms)
        }
    }
    val app = Javalin.create().start(4000)
    app.get("/") {ctx -> ctx.result("Test")}
}