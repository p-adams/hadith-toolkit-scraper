package org.example

interface IsnadToken {
    val narrator: String
    val acquisition: String
}

class IsnadService {
    // tokenize takes raw comma-separated isnad string input and return list of isnad tokens
    fun tokenize(isnadString: String): List<IsnadToken> {
        return listOf()
    }
}


