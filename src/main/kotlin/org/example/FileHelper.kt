package org.example

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

fun chunkExtractedBiographyAssets(fileContent: String) {
    val itemType = object : TypeToken<List<ExtractedBiography>>() {}.type
    val extractedBiographies = Gson().fromJson<List<ExtractedBiography>>(fileContent, itemType)
    val pages = extractedBiographies.chunked(100)
    for((index, p) in pages.withIndex()) {
        File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "$index.json").writeText( GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(p))
    }
}

fun totalBiographyIds(): List<String> {
    return (1..8826).map { it -> it.toString()}
}

fun biographyIdList(): MutableList<String> {
    val jsonContent = File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "taqrib_raw.json").readText()
    val listBiographyType = object : TypeToken<List<ExtractedBiography>>() {}.type
    val biographies: List<ExtractedBiography> = Gson().fromJson(jsonContent, listBiographyType)
    val ids = mutableListOf<String>()
    for(biography in biographies) {
        if(biography.id != "*") {
            ids.add(biography.id)
        }
    }
    return ids
}


fun createExtractedBiographyIdsFile() {
    val ids = biographyIdList()

    File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "ids.json").writeText( GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(ids))
}