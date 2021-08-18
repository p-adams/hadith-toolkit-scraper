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


fun pageRanges(): MutableList<Int> {
    val rangeList = mutableListOf<Int>()
    val indexList = doc.select(".betaka-index > ul")
    for(ul in indexList.iterator()) {
        val listItems = ul.select("li")
        for (index in 0 until listItems.size - 1) {
            val link = listItems[index].select("a")
            val nextLink = listItems[index + 1].select("a")
            val href = link.attr("href")
            val nextHref = nextLink.attr("href")
            if (href == "javascript:;") {
                rangeList.add(Paths.get(nextHref).fileName.toString().toInt())
            }
        }
    }
    return rangeList
}

fun scrape() {
    var extractedBiographies = mutableListOf<ExtractedBiography>()
    for(index in 0 until pageRanges().size - 1) {
        val start = pageRanges()[index]
        val end = pageRanges()[index + 1]
        for(page in start until end) {
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
    File("src/main/kotlin/org/example", "taqrib_raw.json").writeText(jsonExtractedBiographyList)
}