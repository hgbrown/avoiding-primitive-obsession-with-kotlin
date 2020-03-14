# Avoiding Primitive obsession with Kotlin

Primitive obsession refers to the tendency to model things which are 
best represented as custom types as primitives instead.

For example, it is common to model a name as a `String` despite the fact that there are rules that govern what 
type of characters should be allowed in a name.

Consider the following `Person`:

```kotlin
package primitive.obsession

data class Person (val name: String)

fun main() {
    val rerig = Person("@!#$%^&*(321")
    println(rerig)
}
```

Although you would not really expect a person to have such a name it is perfectly legal code.
We can of course fix this by creating a custom type:

```kotlin
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
```

By creating the custom type to represent the name we can no longer use the non-sensical string as a name.

We can take this idea even further using derivation to apply different rules for a first and last name.

```kotlin
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
```

In this implementation we are relying on the fact that implementers of `Name` know that they have to call the `verify` method
in their `init` method. But if this is for internal use (i.e. non-library) then perhaps this is a worthwhile trade-off to ensure
the integrity of the data.
