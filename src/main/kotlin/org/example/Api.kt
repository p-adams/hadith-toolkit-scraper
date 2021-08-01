package org.example
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.javalin.Javalin
import io.javalin.plugin.json.FromJsonMapper
import io.javalin.plugin.json.JavalinJson
import io.javalin.plugin.json.ToJsonMapper
import java.io.File

fun launchApi() {

    val gson = GsonBuilder().create()

    JavalinJson.fromJsonMapper = object : FromJsonMapper {
        override fun <T> map(json: String, targetClass: Class<T>) = gson.fromJson(json, targetClass)
    }

    JavalinJson.toJsonMapper = object : ToJsonMapper {
        override fun map(obj: Any): String = gson.toJson(obj)
    }

    Javalin.create { config ->
        config.requestLogger { ctx, ms ->
            println(ctx)
            println(ms)
        }
    }
    val app = Javalin.create().start(4001)
    app.get("/taqrib_raw") { ctx ->
        val text = File("src/main/kotlin/org/example/assets/taqrib_raw.json").readText()
        ctx.json(text)
    }
}