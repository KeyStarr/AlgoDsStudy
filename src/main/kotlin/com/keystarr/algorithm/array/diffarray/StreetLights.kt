package com.keystarr.algorithm.array.diffarray

/**
 * custom example from the leet DSA course
 * constraints:
 *  • position <= 10^18;
 *  • radius and lights.size seem to be unconstrained.
 *
 * Final notes:
 *  • went for the first diff array approach and failed with array size 10^18 exceeding the allowed max array size (~int max);
 *  • technically don't know if the solution is fully correct, cause there were no constraints for 2 variables and no
 *   official test cases. But it's good enough for the concept;
 *  • missed the +1 edge case on the interval's right bound, cause due to the prefix sum logic we need to do that to
 *   count that rightmost position as a part of that interval => don't subtract that diff just yet.
 *
 * Value gained:
 *  • 🏆 solved my first problem with efficient solution being diff array with sorting, no number line array;
 *  • reinforced the knowledge about JVM max array size, which depends on the JVM implementation, but the reference is Int.MAX-8,
 *   never exceeds INT.MAX;
 *  • learnt that the diff array no number line pattern basically is the same idea as the original diff array, just, ahem,
 *   with sorting and no number line.
 */


//скачай книгу АЛИСЫ!!!!!!!!!!

class StreetLights {

    /**
     * approach 1
     * diff array with the entire numbers line length array
     * => WRONG, array size exceeds maximum allowed (10^18 max position, with no clear constraints for the radius, and
     * java's max array size is ~10^9)
     *
     * --------------
     *
     * Original problem statement:
     * "You are on a street with street lights, represented by an array lights. Each light is given as [position, radius],
     * which means that the light is located at position, and shines to the left and right at a distance of radius.
     * Let's say the brightest spot on the street is the spot where the most lights are shining. Return any such position."
     *
     * problem rephrase:
     *  - goal: return any position (a single number => a coordinate on the number line) with the maximum lights intervals
     *   intersection;
     *  - given: an array of intervals `lights`, each light is \[position, radius]. So the lights\[i] interval spans on
     *   \[position-radius;position+radius] (inclusive).
     *
     * goal is the value of the max intersection of intervals, intervals are unsorted, and the total line length is
     * very long => try diff array for linear time complexity
     *
     * design:
     * - find the farthest lit position `farthest=lights.maxOf { it.position + it.radius }`
     * - allocate `diffArray(size=farthest+1)`
     * - iterate through `lights`, apply the diffs to the diff array:
     *  - diffArray\[light.position-light.radius]+=1
     *  - diffArray\[light.position+light.radius]-=1
     * - iterate through the diff array, find the max value and record it's position
     * - return the position of any max value
     *
     * position <= 10^18, use Long for the distance array, and for input, and for answer
     *
     * Edge cases:
     *  - lights.size == 0 => can it be?
     *  - lights.size == 1 => correct.
     *
     * Time: O(n+m), where:
     *  - n = lights.size
     *  - m = farthest + 1
     * Space: O(m)
     */
    // not implemented due to being wrong

    /**
     * approach 2, diff array with an array of diffs and sorting
     *
     * same idea as in the approach1, but:
     * 1. don't look for the farthest, just iterate through the lights and build an array of diffs entirely straight away,
     *  unsorted, just as they come;
     * 2. sort the array of diffs;
     * 3. walk through it just as we walked through the array of the number line, perform the same logic.
     * return the same
     *
     * Time: O(nlogn)
     *  - lights sorting O(nlogn)
     *  - walking through sorted lights O(n), cause const work at each step
     * Space: O(n), where n=lights.size
     */
    fun diffArraySorted(lights: Array<Light>): Long {
        val changes = mutableListOf<Change>()
        lights.forEach { light ->
            changes.add(Change(light.position - light.radius, 1))
            changes.add(Change(light.position + light.radius + 1, -1))
        }
        changes.sortWith { o1, o2 -> (o1.position - o2.position).toInt() }

        var currentTotalDiff = 0L
        var maxDiff = Long.MIN_VALUE
        var maxDiffPosition = -1L
        changes.forEach { change ->
            currentTotalDiff += change.diff
            if (currentTotalDiff > maxDiff) {
                maxDiff = currentTotalDiff
                maxDiffPosition = change.position
            }
        }

        return maxDiffPosition
    }

    class Light(val position: Long, val radius: Long)

    class Change(val position: Long, val diff: Byte)
}

fun main() {
    println(
        StreetLights().diffArraySorted(
            lights = arrayOf(
                StreetLights.Light(3, 2),
                StreetLights.Light(1000, 100),
                StreetLights.Light(100_000, 1000),
                StreetLights.Light(10_000_000_000_000_000, 3),
                StreetLights.Light(10_000_000_000_000_000 - 1, 5),
                StreetLights.Light(10_000_000_000_000_000 + 2, 10),
            )
        )
    )
}
