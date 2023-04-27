fun <T> getStringsOnly(list: List<T>): List<String> {
    val result = mutableListOf<String>()
    for (element in list) {
        if (element is String) result.add(element)
    }
    return result
}