package primitive.obsession

internal data class Person (val name: String)

fun main() {
    val rerig = Person("@!#$%^&*(321")
    println(rerig)
}