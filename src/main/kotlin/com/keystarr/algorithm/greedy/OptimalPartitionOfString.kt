package com.keystarr.algorithm.greedy

/**
 * ⭐️ a beautiful bit manipulation for existence checking example
 * LC-2405 https://leetcode.com/problems/optimal-partition-of-string/description/
 * difficulty: medium (imho, leet-easy)
 * constraints:
 *  • 1 <= text.length <= 10^5;
 *  • text consists of only lowercase English.
 *
 * Final notes:
 *  • done [set] by myself in 10 mins, [array] a few minutes later;
 *  • went for the array first, but pivoted to set first after all => good call, not to get too distracted in the details,
 *   do it step-by-step and be careful with extra optimizations. Everything's gotta be reasoned and accounted for;
 *  • intuitively felt the core solution rule straight away, but couldn't prove it clearly and simply, and can't even still!
 *   very typical of greedy problem that is, but that's not really greedy, is it? rule - include the letter, but only if
 *   we added such a letter in the current partition, otherwise, ahem, partition. Actually, we make a locally optimal choice
 *   not considering what comes next => it is greedy! Just quite an unusual setting for it.
 *
 * Value gained:
 *  • practiced solving a string modification type problem using greedy and a hashset/counting array;
 *  • uh-huh! so an even more efficient approach to keep track of which values were met, if the amount of distinct values
 *   is quite narrow (<32,64) is to not even use an array, but simply an integer as the container! Where ith bit is whether
 *   we've encountered the (initialOffset + i) character.
 */
class OptimalPartitionOfString {

    /**
     * naive approach - iterate through the [text], keep a hashset of current characters. As soon as we encounter a character
     *  that is already in the set => increment the partitions counter and clear the set.
     *
     * is it a correct algorithm here?
     * intuitively - we minimize the number of partitions => we want to include as many characters in any single partition
     *  as we can => if we try not including the first char start with the 2nd => we'd have to count the first one as a partition,
     *  whereas it might not have been + if there's the same char further..
     *
     * might also use an IntArray for counting.
     *
     * Time: always O(n), n=text.length
     * Space: average/worst O(k), here worst k=26, so O(1)
     */
    fun set(text: String): Int {
        val currentPartition = mutableSetOf<Char>()
        var partitionsCount = 0
        text.forEach { char ->
            if (currentPartition.contains(char)) {
                partitionsCount++
                currentPartition.clear()
            }
            currentPartition.add(char)
        }
        return partitionsCount + 1
    }

    /**
     * could use an IntArray(size=26) for counting to alleviate the costs of computing the hash. Clearing is similar, since
     *  worst hash clearing is O(26) as well, though on average set clearing may work faster. Asymptotically same as the [set].
     */
    fun array(text: String): Int {
        val currentPartition = IntArray(size = 26)
        var partitionsCount = 0
        text.forEach { char ->
            val key = char - 'a'
            if (currentPartition[key] > 0) {
                partitionsCount++
                repeat(currentPartition.size) { ind -> currentPartition[ind] = 0 }
            }
            currentPartition[key] = 1
        }
        return partitionsCount + 1
    }

    /**
     * Huh, since we only got up to 26 distinct chars consecutively ordered by their codes after some offset => try
     *  using a single integer variable as a container, where each ith bit, if set, means that ('a'+i) char was already added
     *  to the current partition.
     *
     * An even better time+space const then [array], but same asymptotic complexities as both previous solutions.
     *
     * -------
     *
     * Learned about that angle thanks to https://leetcode.com/problems/optimal-partition-of-string/solutions/3376693/image-explanation-3-approaches-o-1-space-c-java-python/
     * just that the bitwise approach is possible here => done the rest myself.
     */
    fun bitManipulation(text: String): Int {
        var currentPartition = 0
        var partitionsCount = 0
        text.forEach { char ->
            val offset = char - 'a'
            val keyBit = 1 shl offset
            if ((currentPartition and keyBit) != 0) {
                currentPartition = 0
                partitionsCount++
            }
            currentPartition = currentPartition or keyBit
        }
        return partitionsCount + 1
    }
}

fun main() {
    println(
        OptimalPartitionOfString().bitManipulation("ssssss")
    )
}
