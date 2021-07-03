package org.example
import com.google.gson.Gson
import org.jsoup.Jsoup;


data class ExtractedBiography(val id: String, val data: String)


fun scrape() {
    val doc = Jsoup.connect(System.getenv("url")).get()
    val gson = Gson()
    println(gson.toJson(ExtractedBiography("1", "أحمد ابن إبراهيم ابن خالد الموصلي أبو علي نزيل بغداد صدوق من العاشرة مات سنة ست وثلاثين د فق")))
}