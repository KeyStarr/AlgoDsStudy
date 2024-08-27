package com.keystarr.algorithm.deque.stack

/**
 * LC-735 https://leetcode.com/problems/asteroid-collision/description/
 * difficulty: medium
 * constraints:
 *  • 2 <= asteroids.size <= 10^4;
 *  • -10^3 <= asteroids\[i] <= 10^3, but != 0.
 *
 * Final notes:
 *  • done [efficient] by myself in 20 mins;
 *  • arrived at the solution by reasoning from the simulation rules, from the smallest input size possible, decided
 *   to traverse left-to-right. Reasoning works, lol;
 *  • ⚠️ huh, I just felt there gotta be a better asymptotically solution, but it seems that there isn't.
 *
 * Value gained:
 *  • solve a simulation problem efficiently using a stack.
 */
class AsteroidCollision {

    /**
     * observations:
     *  - all asteroids move at the same speed and there are only 2 possible directions
     *    => if asteroid X moves right and its right neighbor moves left, then X's first collision is with it. Otherwise
     *     X will collide with the first asteroid to the right of it, that moves left, only if it will survive all other
     *     asteroids to the right of X which also move right.
     *    and vice versa if X moves left
     *
     * approach - try simulation?
     *
     * collision rules:
     *  - smaller one explodes, greater one preserves its size
     *  - if equal, both explode
     *
     * 10 2 -5
     *
     * naive:
     *  - iterate through asteroids:
     *   - if asteroid is moving left and:
     *    - we encountered none surviving moving right, add it to the answer;
     *    - we have some surviving moving right => simulate collisions, until either all right moving are destroyed, or
     *     left moving is destroyed. If left survives, add it to answer. "Destroy right" = remove it from answer.
     *   - if asteroid is moving right, add it to the answer.
     *
     * outer loop invariant:
     *  stack contains all remaining asteroids after all possible collisions occurred from asteroids\[0:i)
     *
     * Time: always O(n)
     *  - main loop always has n iterations, n=asteroids.size;
     *  - across all outer loop iterations inner loop each asteroid can be only once added to answer, and only once removed from answer;
     *  => always O(n) time
     * Space: always O(n)
     *  - worst is we add n-1 asteroids moving right and the last asteroid moves left and destroys all them. Actual result is
     *   O(1) but we had O(n-1) space
     *
     * -----------
     *
     * can we optimize?
     *  - probably we can optimize inner loop
     *    note that if ith asteroid is moving left, and if stack.last() is asteroid moving right
     *    =>
     *     1. from the top of the stack down we might have multiple asteroids moving right;
     *     2. aa a result of collisions we remove from stack all asteroids that are <= ith asteroid
     *      => monotonic stack? repeatedly getting max preserving order? well yea, but we still have to add all smaller
     *       asteroids in between local maximums to result, if local maximums survive => monotonic wouldn't improve asymptotic here.
     *
     *  - can we do O(1) space?
     *   try going right to left? it would be the same algorithm but reverse signs
     *   I don't think we can, since result is anyway O(n) space and we technically collect the result in `stack`.
     *
     * => I don't see paths to asymptotic complexities optimization.
     */
    fun efficient(asteroids: IntArray): IntArray {
        val stack = mutableListOf<Int>()
        asteroids.forEach { asteroid ->
            if (asteroid > 0 || stack.isEmpty() || stack.last() < 0) {
                stack.add(asteroid)
            } else {
                while (stack.isNotEmpty() && stack.last() > 0 && stack.last() < asteroid * -1) stack.removeLast()
                if (stack.isEmpty() || stack.last() < 0) {
                    stack.add(asteroid)
                } else if (stack.last() == asteroid * -1) {
                    stack.removeLast()
                }
            }
        }
        return stack.toIntArray()
    }
}
