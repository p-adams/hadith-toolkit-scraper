package org.example
import com.google.gson.Gson
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element

val doc = Jsoup.connect(System.getenv("url")).get()
val gson = Gson()

data class ExtractedBiography(val id: String, val data: String)

fun liToJson(liElement: Element): String? {
    return gson.toJson(ExtractedBiography("1", "sample"))
}

fun scrape() {
    val indexList = doc.select(".betaka-index > ul")
    println(indexList)
}