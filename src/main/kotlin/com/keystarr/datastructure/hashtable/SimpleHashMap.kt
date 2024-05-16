package com.keystarr.datastructure.hashtable

/**
 * LC-706: https://leetcode.com/problems/design-hashmap/
 * constraints:
 *  • 0 <= key, value <= 10^6;
 *  • at most 10^4 calls to put, get and remove;
 *  • to explicit time/space.
 *
 * Final notes:
 *  • a weird problem, cause:
 *      • no real hashtable implementation would simply overwrite values on collisions, right?
 *      • allocating 10^6 space always is extremely impractical either.
 *      • doesn't have more useful methods like `containsValue(..)`.
 *  • I should either implement a closer to "the real thing" hashtable myself or find a similar problem on another platform.
 */
class DesignHashMap {

    private val values = IntArray(1_000_000) { EMPTY_VALUE }

    /**
     * Time: always O(1)
     * Space: always O(1)
     */
    fun put(key: Int, value: Int) {
        values[key.toIndex()] = value
    }

    /**
     * Time: always O(1)
     * Space: always O(1)
     */
    fun get(key: Int): Int {
        val value = values[key.toIndex()]
        return if (value == EMPTY_VALUE) -1 else value
    }

    /**
     * Time: always O(1)
     * Space: always O(1)
     */
    fun remove(key: Int) {
        values[key.toIndex()] = EMPTY_VALUE
    }

    fun getValues() = values.filter { it != EMPTY_VALUE }

    private fun Int.toIndex() = hash() % values.size

    private fun Int.hash() = hashCode()
}

// by constraints value is always positive or 0
private const val EMPTY_VALUE = -1

fun main() {
    println(DesignHashMap().apply {
        put(3, 4)
        put(5, 6)
        put(5, 90)
        remove(3)
        println(get(5))
    }.getValues())
}
