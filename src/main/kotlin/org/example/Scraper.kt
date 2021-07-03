package org.example
import org.jsoup.Jsoup;

fun scrape() {
    val doc = Jsoup.connect(System.getenv("url")).get()
}