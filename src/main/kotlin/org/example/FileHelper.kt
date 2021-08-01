package org.example

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

fun paginateExtractedBiographyAssets(fileContent: String) {
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