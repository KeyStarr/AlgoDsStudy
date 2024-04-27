package algos.hashtable

// LC-771 https://leetcode.com/problems/jewels-and-stones/description/
// difficulty: easy
// constraints:
//  * 1 <= jewels.length, stones.length <= 50
//  * jewels and stones consist of only English letters.
//  * All the characters of jewels are unique.
class JewelsAndStones {

    // time: O(n), where n = max(jewels.length,stones.length)
    // space: O(m), m = jewels.length
    // why beats only >40-50% kotlin out of 4 tries?? haven't found any faster solutions online
    fun fast(jewels: String, stones: String): Int {
        val jewelsSet = jewels.toSet() // O(n), n = jewels.length
        var jewelsCount = 0
        stones.forEach { if (jewelsSet.contains(it)) jewelsCount++ }
        return jewelsCount
    }
}
