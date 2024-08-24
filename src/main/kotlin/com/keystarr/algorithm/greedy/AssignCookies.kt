package com.keystarr.algorithm.greedy

/**
 * LC-455 https://leetcode.com/problems/assign-cookies/description/
 * difficulty: easy
 * constraints:
 *  • 1 <= children.size <= 3*10^4;
 *  • 0 <= cookies.size <= 3*10^4;
 *  • 1 <= children\[i], cookies\[j] <= 2^31 - 1.
 *
 * Final notes:
 *  • ⚠️⚠️ done [efficient] by myself in 25 mins, NOT OK for an easy problem! Though it is kinda logic-heavy problem
 *   for an easy greedy with 2 arrays and early conditions. I'd say its more of a greedy leet-medium; Still, could've
 *   done faster if paid more attention to the logic correctness and didn't jump into submit straight + maybe need
 *   more practice with greedy;
 *  • ⚠️ failed 2 submit attempts, left glaring errors in the algorithm:
 *   • 1st forgot to actually scroll through the cookies until the current child is satisfied, if instead of while (but meant it);
 *   • 2nd forgot to count the cookie as used when its enough to content the current child and a couple more edge cases I think with early exit conditions.
 *  • was so happy with the idea jumped straight into it and was overconfident => made obvious errors.
 *   => BE CAREFUL WITH GREEDY! These might look easy, BUT EDGE CASES and FOLLOW THE LOGIC FULL, pay attention!
 *  • done [efficientCleaner] after checking Editorial => ⚠️ I AGAIN MADE MY USUAL current MISTAKE and scrolled through the
 *   elements in the inner loop, while it wouldve been so much simpler to scroll through them in the main loop, 1 at a time.
 *   Focus on cookies, not on children in the main loop.
 *   How to learn and solidify that? Try to just pay more attention and try to check if by uncovering such mistakes I'll
 *   notice these cases more and actually do it the right way (if continue making such mistakes => change the tactic ⚠️)
 *
 * Value gained:
 *  • solve a pair matchmaking type problem efficiently using greedy, sorting and 2 pointers.
 */
class AssignCookies {

    // TODO: repeat in 1-2 weeks

    /**
     * problem rephrase:
     *  - given:
     *   - children: IntArray, where children\[i] is the minimum cookie size that the ith child will be content with;
     *   - cookies: IntArray, where cookies\[j] is size of the jth cookie.
     *  - step rule: assign jth cookie to the ith child, if cookies\[j] >= children\[i] then ith child is content and
     *   shouldn't be considered anymore and jth cookie must also not be considered then. Otherwise, its not a match, both
     *   remain available.
     *  Goal: return the maximum number of content children.
     *
     * step rules + subproblems seemingly don't overlap + target is max => try greedy
     *
     * Greedy rule: find the child with minimum greed from remaining and give it the cookie with the minimum passable size
     *  out of available ones. If there's no such cookie => terminate, return contentChildrenCount
     *
     * Why? Because we need to maximize the amount of content children, and each has a minimum requirement for the cookie size
     * => if we select anything but the min greed child and min passable cookie size, that cookie might have been useful
     * for some child with greater greed, and there may be no other cookie that could've satisfied them => we'd end up
     * with at least 1 content child less.
     * (not a real proof but its OK for now)
     *
     * Do we have to, at each step take the min greed child of the remaining? Can we take a random child and just a min
     * passable cookie for them? I think we can, but we'd lose the early return condition property then, cause if for
     * children\[i] there'd be no cookie available, for others children with less greed there might be.
     * Ah, then we'd have to iterate through the cookies every time, or do a binary search through cookies to find the one
     *  no less than child's greed. So time would be then O(n*logn + m * logn), where n=cookies.size, m=children.size
     *
     * If we sorted both, it'd be O(n*logn + m*logm), so it doesn't really matter on average time complexity is roughly the same
     * since both m and n are in a similar range, unless we know for sure there'd be more of either, some special input patterns
     * of asymptotic scale.
     *
     * Do the 1st approach then cause its simpler to implement and maintain.
     *
     * Edge cases:
     *  - cookies.size == 0 => return 0, correct as-is, we would not enter the loop then.
     *
     * Time: always O(nlogn + mlogm), where n=cookies.size, m=children.size
     * Space: O(logn + logm) for sorting
     *x
     */
    fun efficient(children: IntArray, cookies: IntArray): Int {
        children.sort()
        cookies.sort()

        var childInd = 0
        var cookiesInd = 0
        var contentChildren = 0
        while (cookiesInd < cookies.size && childInd < children.size) {
            // try to find a min remaining cookie that can satisfy the child at childInd:
            while (cookies[cookiesInd] < children[childInd]) {
                cookiesInd++
                if (cookiesInd == cookies.size) return contentChildren
            }
            // make the child at [childInd] content with cookie [cookiesInd]:
            childInd++
            cookiesInd++
            contentChildren++
        }
        return contentChildren
    }

    /**
     * Core algorithm same as [efficient], but improve based on 2 major observations:
     *  - notice that childInd is always equal to the amount of content children (cause array index = the number of elements
     *   before it, and we only move index if the childInd th child is content);
     *  - we may scroll not through the children, but through the cookies foremost. That is, not try to go next iteration
     *   of the main while loop only when the current child is content, but each iteration go through a cookie, and
     *   only if it satisfies the current child, simply move the child pointer.
     *
     * Learned thanks to the Editorial.
     */
    fun efficientCleaner(children: IntArray, cookies: IntArray): Int {
        children.sort()
        cookies.sort()

        var childInd = 0
        var cookiesInd = 0
        while (cookiesInd < cookies.size && childInd < children.size) {
            if (cookies[cookiesInd] >= children[childInd]) childInd++
            cookiesInd++
        }
        return childInd
    }
}

fun main() {
    println(
        AssignCookies().efficient(
            children = intArrayOf(1, 2, 3),
            cookies = intArrayOf(1, 1),
        )
    )
}
