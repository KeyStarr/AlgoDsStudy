package com.keystarr.algorithm.search.binarysearch

/**
 * 💣️ RETRY LATER, failed by myself
 * LC-540 https://leetcode.com/problems/single-element-in-a-sorted-array/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= nums.length <= 10^5;
 *  • 0 <= nums\[i] <= 10^5.
 *
 * Final notes:
 *  • time O(logn) + sorted array => binary search, my first thought. But I've failed to understand exactly how to apply it
 *   here, what's the formula for half elimination => tried to look for other solutions, but failed;
 *  • came back to binary search, still not knowing how to proceed and just though out loud - how can we determine if
 *   we found the distinct element? if left and right from it are different from it (respecting boundaries). Good, but
 *   how to eliminate the half? Dry ran => noticed that if the pair of the middle element is to its left, then the
 *   target it in that half too, and vice versa;
 *  • implemented => tried to run, failed. Noticed that sometimes we need the opposite, the pair of the mid is on the left
 *   side, but the distinct element is on the right side => tried to come up with dirty hacks, going insane and not even
 *   understanding fully why I try what I try, only to solve it (never a good strategy for learning :)) => failed miserably;
 *  • gave up after ~1h, read the solution and didn't understand it! Watched Neetcode's explanation and then got it, but
 *   not fully. Dry ran multiple times, I understand the algorithm now enough do dry run it, but still haven't develop
 *   the full intuition and confidence into why we do what we do, and why it is correct for all cases...
 *  • on that and [com.keystarr.algorithm.array.twopointers.ContainerWithMostWater] I feel like after some point trying
 *   continuing to try to understand the problem has very much diminishing returns. No? Ok, studied a few solutions and
 *   feel like I've reached a wall in understanding, like I know what and how, but don't at the same time => let it go
 *   and marinade in the brain for some time? Then come back later and try to understand again?
 *
 * Value gained:
 *  • practiced recognizing and solving a wildly unusual binary search problem
 */
class SingleElementInASortedArray {

    // TODO: retry at first understanding/dry-running, only then actually solving by myself. Haven't developed the intuition.

    /**
     * occurrence counting, but can't use a hashmap, its O(n) time. Can't use bitwise with xor mask, its O(n) time.
     * we cant modify the entire collection in any way, or just check each element.
     * we have to work with it as it is.
     *
     * O(logn) time + a sorted input array => binary search? searching for what?
     *
     * 2 pointers somehow?
     *
     * since the array is sorted and each element except 1 has 2 instances => the element we look for always is the only one
     * that, to the both left and right of it, it has elements different from it.
     * can we use that?
     *
     * it has to be with the size of the array! array is always of the odd size, and the half in which the unique element
     * is at must be of the greater size than the half where it is not present, after we remove the middle element and it's pair.
     * Since we always remove 2 elements and the array size is always odd at the start, it always remains odd until the end.
     *
     * use standard binary search template to find the element and return the moment we do, the answer is guaranteed to be present.
     *
     * Time: average/worst O(logn)
     * Space: always O(1)
     */
    fun efficient(nums: IntArray): Int {
        var left = 0
        var right = nums.size - 1
        while (left <= right) {
            val middleInd = left + (right - left) / 2
            println("l: $left, r: $right, m: $middleInd")
            val middleNum = nums[middleInd]

            val isLeftDifferent = middleInd == 0 || nums[middleInd - 1] != middleNum
            val isRightDifferent = middleInd == nums.size - 1 || nums[middleInd + 1] != middleNum
            if (isLeftDifferent && isRightDifferent) return middleNum

            // middleInd = the size of the subarray nums[0:middleInd), and since we determined that middle element isnt the
            // target, and that it's pair is to the left of it => the size of nums[0:middleInd-2] is middleInd-1;
            // else -> middle element's pair is to the right of it => the size of nums[0:middleInd-1] is middleInd
            val leftSize = if (middleInd != 0 && nums[middleInd - 1] == middleNum) middleInd - 1 else middleInd
            println("leftSize: $leftSize")

            // we don't need the rightSize since if the left size isnt odd => we always need the right side
            // if true => the element is in the left half, otherwise in the right half
            if (leftSize % 2 == 1) right = leftSize - 1 else left = middleInd + 1
        }
        throw IllegalStateException("The answer is guaranteed to exist")
    }
}

fun main() {
    println(
        SingleElementInASortedArray().efficient(
            nums = intArrayOf(1, 1, 2, 2, 4, 4, 5, 5, 9),
        )
    )
}
