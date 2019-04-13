package telekt.util

fun String.splitByLength(maxLength: Int): List<String> = splitByLength(maxLength, mutableListOf())

private tailrec fun String.splitByLength(maxLength: Int, ret: MutableList<String>): List<String> =
    if (length > maxLength) {
        ret += substring(0, maxLength)
        substring(maxLength).splitByLength(maxLength, ret)
    } else {
        ret += this
        ret
    }
