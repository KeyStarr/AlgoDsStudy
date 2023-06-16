package grokking.chapter1

fun binarySearchWhile(sortedElementsAscending: List<Int>, target: Int): Int {
    if (sortedElementsAscending.isEmpty()) return -1

    var startInd = 0
    var endInd = sortedElementsAscending.size - 1
    while (endInd - startInd > 1) {
        val middleInd = startInd + (endInd - startInd) / 2
        val currentElement = sortedElementsAscending[middleInd]
        if (target == currentElement) {
            return middleInd
        } else if (target > currentElement) {
            startInd = middleInd + 1
        } else {
            endInd = middleInd - 1
        }
    }
    return when (target) {
        sortedElementsAscending[startInd] -> startInd
        sortedElementsAscending[endInd] -> endInd
        else -> -1
    }
}

fun binarySearchRecursive(sortedElementsAscending: List<Int>, target: Int): Int =
    if (sortedElementsAscending.isEmpty()) {
        -1
    } else {
        searchRecursive(
            sortedElementsAscending = sortedElementsAscending,
            target = target,
            startInd = 0,
            endInd = sortedElementsAscending.size - 1
        )
    }

private fun searchRecursive(sortedElementsAscending: List<Int>, target: Int, startInd: Int, endInd: Int): Int {
    if (endInd - startInd < 2) {
        return when (target) {
            sortedElementsAscending[startInd] -> startInd
            sortedElementsAscending[endInd] -> endInd
            else -> -1
        }
    }

    val middleInd = startInd + (endInd - startInd) / 2
    val middleElement = sortedElementsAscending[middleInd]
    if (middleElement == target) return middleInd

    return searchRecursive(
        sortedElementsAscending = sortedElementsAscending,
        target = target,
        startInd = if (target > middleElement) middleInd + 1 else startInd,
        endInd = if (target < middleElement) middleInd - 1 else endInd,
    )
}

fun main() {
    println("result is " + binarySearchRecursive(listOf(-1, 0, 3, 5, 9, 12), 9))
}
