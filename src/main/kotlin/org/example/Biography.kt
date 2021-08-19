package org.example


interface BiographyName {
    val name: String;
    // kunya
    val teknonym: String?;
    // nisba
    val connection: String?;

}

data class Biography(
     val id: Number,
     val nameDetails: BiographyName,
     val residence: String?,
     val reliability: String?,
     val period: String?,
     val dod: String?,
     val collectors: List<String>?
     )
