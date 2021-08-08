package org.example


interface BiographyName {
    val name: String;
    val teknonym: String?;
    val connection: String?;

}

data class Biography(
     val id: Number,
     val nameDetails: BiographyName,
     val residence: String,
     val reliability: String,
     val period: String,
     val dod: String?,
     val collectors: String
     )
