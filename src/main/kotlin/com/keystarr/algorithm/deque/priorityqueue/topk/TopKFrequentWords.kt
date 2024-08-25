package com.keystarr.algorithm.deque.priorityqueue.topk

import java.util.PriorityQueue
import kotlin.math.min

/**
 * ⭐️ a cool multi-level condition top k heap problem.
 * LC-692 https://leetcode.com/problems/top-k-frequent-words/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= words.length <= 500;
 *  • 1 <= words\[i].length <= 10;
 *  • words\[i] consists of lowercase English only;
 *  • 1 <= k <= number of unique words.
 *
 * Final notes:
 *  • done [suboptimal] by myself in 20 mins;
 *  • 🥇 done [efficient] in 20 mins, so at the mark of 40 mins total;
 *  • wow, the [efficient] heap's reversal conditions broke my brain a little, a brainteaser literally, cause technically
 *   we have then a min(count)-max(lexicographical) heap. So the more levels of conditions we pack in, if these have
 *   different order, the actual heap definition may be different for each level of the condition (min-max-max-min etc). Cool.
 *
 * Value gained:
 *  • practiced recognizing and solving a top k problem efficiently using a min-max heap with a 2 level comparator;
 *  • 🔥 learned that aside from just min or max heap the heap we have may require different ordering on each condition level,
 *   so technically a heap may be described via a min/max for EACH condition level.
 */
class TopKFrequentWords {

    /**
     * goal: return k most frequent strings (return distinct, 1 for each) from the array, and:
     *  - sorted descending by frequency;
     *  - if same frequency, sort lexicographically.
     *
     * 1. count the frequency of all strings. Since max string length is up to 10 => may use itself as the map key str->count;
     *  => result is str->count map
     * 2. then we may convert the map into a list and sort it via following rules:
     *  - descending by frequency;
     *  - if same frequency, whichever string is greater lexicographically (whichever is first with str1\[i] > str2\[i]).
     *
     * strings are at most of length 10 and are immutable => OK for map key (retrieval and put is worst O(10) then, which is const still)
     *
     * edge cases:
     *  - all distinct words => all words frequencies are equal to 1 => each sorting comparison is lexicographical => correct as-is;
     *  - words.length == 1 => return itself, k can only by 1 => correct;
     *  - k is such that we need to return all words => nothing special, correct as-is.
     *
     * Time: O(n + nlogn + k) = O(nlogn + k) = always O(nlogn)
     *  - counting always has n iterations, and hashcode computation for a string is O(string.length), so worst case
     *   its O(n*g), where g=max string length, O(n*10) = O(n) ;
     *  - sorting by itself is O(nlogn), but we may compare a string lexicographically with all other (n-1) strings, so
     *   its O(g*nlogn), but worst g=10 so its O(nlogn);
     *  - taking k sublist and mapping to keys is O(k)
     * Space: O(n+logn+k) = average/worst O(n)
     *  - worst map size is when all strings are unique = n, average/worst O(n);
     *  - sorting O(logn) space;
     *  - sublist of size k is O(k)
     */
    fun suboptimal(words: Array<String>, k: Int): List<String> {
        val wordsToCountMap = words.countWords()
        return wordsToCountMap.entries
            .sortedWith { o1, o2 ->
                val (word1, count1) = o1
                val (word2, count2) = o2
                if (count1 != count2) count2 - count1 else word1.compareLexicographically(word2)
            }
            .take(k)
            .map { it.key }
    }

    /**
     * Follow-up - can you do time O(nlogk)?
     *
     * 2 3 1 7 3 5 9
     *
     * Sure, use a top k heap pattern. Since we only need k most frequent words => count words, then iterate through
     * map entries and add each entry into the minHeap with our custom sorting algorithm. If heap.size == k+1, remove from
     * the heap (thus removing the smallest element) => process all elements, we get k maxes.
     *
     * i.o. loop invariant - heap contains up to K max elements from items[0:i-1]
     *  init: i=0, so items[0:-1] is an empty subarray, trivially true
     *  maintenance:
     *   - i=1, items[0:0] is exactly one element, and if k>0 we'd have it in the heap;
     *   - i=2, items[0:1] is 2 elements, if k>2 we'd have both in the heap, if k==1 then we'd add the items[1] to items[0]
     *    already in the heap, and remove the minimum element of those two => correct, either heap contains k maxes from items[0;i].
     *   etc
     *  termination: i=items.size, so items[0:items.size-1] is the entire array, and the heap contains k maxes from it.
     *
     *
     * use all the reversed result sorting conditions in the comparator of the heap in order to remove the elements which
     *  would be less first and not max in terms of the placement in the final sorting:
     *   - count1-count2: ascending by count (reversed the original descending);
     *   - word2.lexicographically(word1): descending by lexicographical (reversed the original ascending).
     *
     * Time: O(n+n*logk+k)=O(nlogk)
     *  - counting O(n);
     *  - min heap add all is O(n*logk);
     *  - transform heap into list and reverse O(k).
     * Space: O(n+k)
     *  - heap worst size is k + cause max k is the number of unique words so its always reaches k at some point = always O(k);
     *  - counting worst size is n = O(n).
     *
     *
     * k = 2
     *
     * leetcode: 1
     * i : 2
     * love : 2
     * coding: 1
     *
     * minHeap:
     *  i, love
     *
     * result:
     *  remove() = i
     *  remove() = love
     * reverse = love, i
     */
    fun efficient(words: Array<String>, k: Int): List<String> {
        val wordsToCountMap = words.countWords().toList()
        val minHeap = PriorityQueue<Int> { ind1, ind2 ->
            val (word1, count1) = wordsToCountMap[ind1]
            val (word2, count2) = wordsToCountMap[ind2]
            if (count1 != count2) count1 - count2 else word2.compareLexicographically(word1)
        }
        repeat(wordsToCountMap.size) { ind ->
            minHeap.add(ind)
            if (minHeap.size == k + 1) minHeap.remove()
        }
        return mutableListOf<String>().apply {
            while (minHeap.isNotEmpty()) add(wordsToCountMap[minHeap.remove()].first)
            reverse()
        }
    }

    private fun Array<String>.countWords() = mutableMapOf<String, Int>().also { map ->
        forEach { word -> map[word] = map.getOrDefault(word, 0) + 1 }
    }

    private fun String.compareLexicographically(other: String): Int {
        for (i in 0 until min(length, other.length)) {
            if (this[i] > other[i]) return 1 else if (this[i] < other[i]) return -1
        }
        return if (length == other.length) 0 else if (length > other.length) 1 else -1
    }
}

fun main() {
    println(
        TopKFrequentWords().efficient(
            words = arrayOf("i", "love", "leetcode", "i", "love", "coding"),
            k = 2,
        )
    )
}
