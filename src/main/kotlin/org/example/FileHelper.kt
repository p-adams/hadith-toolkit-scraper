package org.example

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

fun prettyPrintToJsonFile(path: String, name: String, content: Any) {
    File("src/main/kotlin/org/example/assets/$path", "$name.json").writeText( GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(content))
}

fun chunkExtractedBiographyAssets(fileContent: String) {
    val itemType = object : TypeToken<List<ExtractedBiography>>() {}.type
    val extractedBiographies = Gson().fromJson<List<ExtractedBiography>>(fileContent, itemType)
    val pages = extractedBiographies.chunked(100)
    for((index, p) in pages.withIndex()) {
        prettyPrintToJsonFile("taqrib_al_tahdhib", index.toString(), p)
    }
}

fun totalBiographyIdList(): List<String> {
    return (1..8826).map { it -> it.toString()}
}

fun biographyIdList(): MutableList<String> {
    val jsonContent = File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "taqrib_raw.json").readText()
    val listBiographyType = object : TypeToken<List<ExtractedBiography>>() {}.type
    val biographies: List<ExtractedBiography> = Gson().fromJson(jsonContent, listBiographyType)
    val ids = mutableListOf<String>()
    for(biography in biographies) {
        if(biography.id != "*" && !ids.contains(biography.id)) {
            ids.add(biography.id)
        }
    }
    return ids
}

fun getMissingIds(): MutableList<String> {
    var missingIds = mutableListOf<String>()
    var extractedIds = biographyIdList()
    extractedIds.forEachIndexed { index, s ->
       if (totalBiographyIdList()[index] != s) {
           missingIds.add(totalBiographyIdList()[index])
       }
   }
    return missingIds
}

fun missingBiographiesFromTxt() {
    val lines = File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "missing_bios.txt").readLines()
    val missingBiographiesList = mutableListOf<ExtractedBiography>()
    lines.map { it ->
       if (it.replace("\\s".toRegex(), "").startsWith("[]")) {
            val parsedBiography = it.split("[]")[1]
            missingBiographiesList.add(ExtractedBiography("*", parsedBiography))
        } else {
            val parsedBiography = it.split("-")
           val id = parsedBiography.first().replace("\\s".toRegex(), "")
           val biography = parsedBiography[1]
           missingBiographiesList.add(ExtractedBiography(id, biography))
       }
    }
    prettyPrintToJsonFile("taqrib_al_tahdhib", "missing_bios", missingBiographiesList)
}

fun commitMissingIdsToFile() {
    prettyPrintToJsonFile("taqrib_al_tahdhib", "missing_ids", getMissingIds())
}

fun createExtractedBiographyIdsFile() {
    val ids = biographyIdList()
    prettyPrintToJsonFile("taqrib_al_tahdhib", "ids", ids)
}

fun removeDuplicateBiographies() {
    val jsonContent = File("src/main/kotlin/org/example/assets/taqrib_al_tahdhib", "taqrib_raw.json").readText()
    val listBiographyType = object : TypeToken<List<ExtractedBiography>>() {}.type
    val biographies: List<ExtractedBiography> = Gson().fromJson(jsonContent, listBiographyType)
    val biographiesList = mutableListOf<ExtractedBiography>()
    for(biography in biographies) {
        if(biography.id == "*") {
            biographiesList.add(biography)
        } else if( !biographiesList.contains(biography)) {
            biographiesList.add(biography)
        }
    }
    prettyPrintToJsonFile("taqrib_al_tahdhib", "taqrib_raw_no_dupes", biographiesList)
}