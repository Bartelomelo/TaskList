data class Comment(val id: Int, val body: String, val author: String)

fun printComments(commentsData: MutableList<Comment>) {
    for ((_, p1, p2) in commentsData) {
        println("Author: $p2; Text: $p1")
    }
}
