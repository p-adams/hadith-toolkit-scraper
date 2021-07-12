package org.example
import com.google.gson.Gson
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

val doc: Document = Jsoup.connect(System.getenv("url")).get()
val gson = Gson()

data class ExtractedBiography(val id: String, val data: String)

fun liToJson(liElement: Element): String? {
    return gson.toJson(ExtractedBiography("1", "sample"))
}

fun scrape() {
    val indexList = doc.select(".betaka-index > ul")
    for(ul in indexList.iterator()) {
        val firstIndex = ul.selectFirst("li")
        val allLinks = firstIndex.select("li > a")
        val chapter = allLinks[1]
        // TODO: create directory named after chapterText
        val chapterText = chapter.text()
        val chapterLink = chapter.attr("href")
        val currentChapter = Jsoup.connect(chapterLink).get().select(".nass")
        // create new JSON entry keyed by index
        println(chapterLink)
        println(currentChapter)
    }
}