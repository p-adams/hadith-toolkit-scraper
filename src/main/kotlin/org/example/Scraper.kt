package org.example
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File
import java.nio.file.Paths

val BASE_URL: String = System.getenv("url")
val doc: Document = Jsoup.connect(BASE_URL).get()
val gson = Gson()

data class ExtractedBiography(val id: String, val data: String)


fun pageRanges(): MutableList<IntRange> {
    val rangeList = mutableListOf<IntRange>()
    val pages = mutableListOf<Int>()
    val indexList = doc.select(".betaka-index > ul")
    for(ul in indexList.iterator()) {
        val listItems = ul.select("li > a")
        for (link in listItems) {
            val href = link.select("a").attr("href")
            if(href != "javascript:;") {
                val path = Paths.get(href).fileName.toString().toInt()
                if(!pages.contains(path)) {
                    pages.add(path)
                }
            }
        }
    }
    for(page in 0 until pages.size - 1) {
        rangeList.add(pages[page]..pages[page + 1])
    }
    return rangeList
}

fun scrape() {
    var extractedBiographies = mutableListOf<ExtractedBiography>()
    for(range in pageRanges()) {
        for(page in range) {
            val currentPage = Jsoup.connect("$BASE_URL/$page").get().select(".nass")
            val currentPageTextNodes = currentPage.select("p")
               for(p in currentPageTextNodes) {
                   // best case scenario is that the text has ID and biographical text data
                   if(p.text().first().digitToIntOrNull() !== null) {
                       val splitText = p.text().split("-")
                       val biographyIndex = splitText.first()
                       val biographyText = splitText.last()
                       extractedBiographies.add(ExtractedBiography(biographyIndex , biographyText))
                   } else {
                       // handle case where biography entry does not have an ID, in which case
                       // set ID as "*"
                       val biographyText = p.text().split("[]").last()
                       // chapter and section headings sometimes appear here, so only add the biographical data
                       if(!biographyText.contains("[")) {
                           extractedBiographies.add(ExtractedBiography("*", biographyText))
                       }
                   }
               }
        }
    }
    val jsonExtractedBiographyList = GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(extractedBiographies)
    File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "taqrib_raw.json").writeText(jsonExtractedBiographyList)
}