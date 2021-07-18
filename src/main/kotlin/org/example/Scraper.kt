package org.example
import com.google.gson.Gson
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.nio.file.Paths

val BASE_URL: String = System.getenv("url")
val doc: Document = Jsoup.connect(BASE_URL).get()
val gson = Gson()

data class ExtractedBiography(val id: String, val data: String)

fun scrape() {
    val indexList = doc.select(".betaka-index > ul")
    for(ul in indexList.iterator()) {
        val listItems = ul.select("li")
        for(index in  0 until listItems.size - 1) {
            // TODO: extract chapter title to organize data by chapters
            val link = listItems[index].select("a")
            val nextLink = listItems[index + 1].select("a")
            val href = link.attr("href")
            val nextHref = nextLink.attr("href")
            if(href != "javascript:;" && nextHref != "javascript:;") {
                val path = Paths.get(href).fileName.toString().toInt()
                val nextPath = Paths.get(nextHref).fileName.toString().toInt()
                val range = path..nextPath
                if(path != nextPath) {
                    for(page in range) {
                        val currentPage = Jsoup.connect("$BASE_URL/$page").get().select(".nass > p")
                        val currentPageTextNodes = currentPage.textNodes()
                        for(node in currentPageTextNodes) {
                            val text = node.text()

                        }
                    }
                }
            }
        }
    }
}