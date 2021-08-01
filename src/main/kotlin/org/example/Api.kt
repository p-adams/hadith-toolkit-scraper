package org.example
import com.google.gson.Gson
import io.javalin.Javalin
import java.io.File

fun launchApi() {
    Javalin.create { config ->
        config.requestLogger { ctx, ms ->
            println(ctx)
            println(ms)
        }
    }
    val app = Javalin.create().start(4001)
    app.get("/taqrib_raw") { ctx ->
        val text = File("src/main/kotlin/org/example/assets/taqrib_raw.json").readText()
        ctx.result(text)
    }
}