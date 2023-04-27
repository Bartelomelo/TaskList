import kotlinx.datetime.*

fun isLeapYear(year: String): Boolean {
    // Write your code here
    try {
        val instant = "${year}-02-29T00:00:00Z".toInstant()
    } catch (e: RuntimeException) {
        return false
    }
    return true
    //
}

fun main() {
    val year = readln()
    println(isLeapYear(year))
}