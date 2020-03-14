package introducing.derivation

import java.util.regex.Pattern

internal abstract class Name(open val value: String) {

    protected fun verifyName(namePattern: Pattern) {
        println("Verifying $value is a valid name ...")
        if (!namePattern.matcher(value).matches()) {
            throw IllegalArgumentException("$value is not a legal ${javaClass.simpleName}")
        }
    }

}

internal data class FirstName(override val value: String) : Name(value) {

    // or whatever rules you want to apply for allowed characters for the first name
    private val namePattern: Pattern = Pattern.compile("^[A-Z][A-Za-z-]+$")

    init {
        verifyName(namePattern)
    }

}

internal data class LastName(override val value: String) : Name(value) {

    // or whatever rules you want to apply for allowed characters for the last name
    private val namePattern: Pattern = Pattern.compile("^[A-Z][A-Za-z -]+[a-z]$")

    init {
        verifyName(namePattern)
    }

}

internal data class Person(val firstName: FirstName, val lastName: LastName)

fun main() {
    val valid = Person(FirstName("Henry"), LastName("Le Roux"))
    println(valid)

    Person(FirstName("Le Roux"), LastName("Henry"))
}
