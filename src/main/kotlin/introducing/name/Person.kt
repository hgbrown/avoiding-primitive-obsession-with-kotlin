package introducing.name

import java.util.regex.Pattern

internal data class Name(val value: String) {

    // or whatever rules you want to apply for allowed characters
    private val namePattern: Pattern = Pattern.compile("^[A-Z][A-Za-z-]+$")

    init {
        println("Verifying $value is a valid name ...")
        if (!namePattern.matcher(value).matches()) {
            throw IllegalArgumentException("$value is not a legal name")
        }
    }
}

internal data class Person(val name: Name)

fun main() {
    Person(Name("@!#$%^&*(321"))
}