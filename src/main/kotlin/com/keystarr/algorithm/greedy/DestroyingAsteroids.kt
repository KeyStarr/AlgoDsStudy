package com.keystarr.algorithm.greedy

/**
 * LC-2126 https://leetcode.com/problems/destroying-asteroids/description/
 * difficulty: medium (more like leet-easy)
 * constraints:
 *  • 1 <= mass <= 10^5
 *  • 1 <= asteroids.length <= 10^5
 *  • 1 <= asteroids\[i] <= 10^5
 *  
 * Final notes:
 *  • how is that a leet•medium? No advanced data structures, no edge cases besides sum exceeding int, no advanced algorithmic patterns;
 *  • I was confident there's a more efficient solution, but the DSA course gave none and in submissions there's no other also.
 *   So weird. Why?
 *  
 * Value gained:
 *  • practiced recognizing and implementing a greedy algorithm.
 */
class DestroyingAsteroids {

    /**
     * Problem rephrase:
     *  - we are given an integer `mass` and int array `asteroids`
     *  - 1 move is:
     *      - if asteroids\[i] <= mass => mass+=asteroids\[i]
     *      - else => return false (cant continue)
     *  - return true if it's possible to do the move with integers in asteroids
     *
     * Naive idea: each move consume the min remaining asteroid, even there is none and there are still asteroids left => return false
     *  otherwise if all asteroids are consumed return true (greedy)
     *
     *  but why??????
     *
     * Implementation:
     *  - sort all asteroids ascending; O(nlogn)
     *  - iterate through sorted asteroids: O(n)
     *      - if (mass < asteroid[i]) return false
     *      - else mass += asteroid[i]
     *  - return true
     *
     * Edge case:
     *  - lastAsteroid > mass => is the asteroid destroyed too if its larger than the planet's mass? Only that the planet
     *   is destroyed is stated. No one to ask, assume not;
     *  - summing => max sum is 10^5 * 10^5 + 10^5 = ~10^10 => exceeds int => use long for sum
     *
     * Time: O(nlogn)
     * Space: O(n)
     */
    fun efficient(originalPlanet: Int, asteroids: IntArray): Boolean {
        // we could use in-place sorting to speed things up, but its against general best practices for real prod
        val sortedAsteroids = asteroids.sorted()
        var mutatedPlanet = originalPlanet.toLong()
        sortedAsteroids.forEach { asteroid ->
            if (asteroid > mutatedPlanet) return false else mutatedPlanet += asteroid
        }
        return true
    }
}
