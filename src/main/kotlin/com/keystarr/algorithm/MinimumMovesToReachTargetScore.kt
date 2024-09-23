package com.keystarr.algorithm

/**
 * LC-2139 https://leetcode.com/problems/minimum-moves-to-reach-target-score/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= target <= 10^9;
 *  • 0 <= maxDoubles <= 100.
 *
 * Final notes:
 *  • ⚠️ did [efficient] by myself in 40 mins:
 *   • 🔥 recognized that greedy is worth trying straight away;
 *   • thought I got the approach right design-stage in the first 10 mins => didn't dry run, implemented straight away,
 *    ran a bunch of times of the example test cases and failed a lot of times => ⚠️⚠️ almost blindly (without proof, not going
 *    deep, not dry running) tried 4-5 variations => finally as a combination of dry-running and checking the examples
 *    dry-runs realized that the right approach was to start from the top;
 *  • 🔥 "start from top go bottom" was the core observation here;
 *  • ⚠️ the "try more" approach worked fine here, though in a real interview that wouldn't be allowed, I'd have to dry run
 *   though probably and that would know that would be wrong => so many blind attempts would look bad for sure;
 *  • once found the approach => got the edge cases done myself with only 1 failed run;
 *  • ❓ basically I started from the wrong end. How could I have / can one in general start from the right end (top->bottom)
 *   straight away here?
 *  • ❓ note that the [efficient] is actually a part of the "system 10 to 2" number conversion algorithm. Why?
 *
 *  • why did I whiff so hard (40 mins and many failed initial approaches) on a seemingly easier problem for other people?
 *   (judging by selective comments)
 *
 * Value gained:
 *  • practiced solving an arithmetic "minimum number of moves" type problem using plain greedy and basic arithmetic.
 */
class MinimumMovesToReachTargetScore {

    // TODO: full re-solve in 1-3 weeks

    /**
     *
     * --------------------- WRONG REASONING
     *
     * try greedy:
     *  - minimize number of moves + double is the most valuable operation in terms of the net gain =>
     *   it is most valuable to use the double resource as many times as possible, but if we have less uses of it
     *   then actually required to reach the [target] => increment as much as we need to use the doubling as many times as we can.
     *
     * the answer is always possible, since we always start with 1 and the target is always at least 1 => we can at least
     *  always simply increment to the target
     *
     * edge cases:
     *  - target == 1 => return 0 (the only case when the result == 0);
     *  - maxDoubles == 0 => return target - 1 always.
     *
     *
     * we also want to not overshoot the target, since we can only increase the current number.
     *
     * try:
     *  - target/2 => the ideal amount of twos such that we'd reach the target in the minimum amount of moves (respecting the odd);
     *  - target/2 - 2*
     *
     *
     * val maxDoublesMultiplier = 2^maxDoubles // safe cause if we would have a huge amount of doubling available, such that
     *                                         // the multiplier wouldn't fit into Int => we'd always hit the edge cases with O(1) time;
     * while((currentNumber + 1) * maxDoublesMultiplier >= target):
     *  currentNumber++
     *  operations++
     *
     * operations += maxDoublesMultiplier
     * return operations + (target % 2)
     *
     * ------------------------- CORRECT
     *
     * target=19 mD=2
     * (+3) 4 8 16 (+3)
     * r=8
     *
     * t=19 mD=2
     * (+3) 4 8 (+1) 9 18 (+1)
     * r=7
     *
     * mD=1
     * (+3) 4 8
     *
     *
     * t=19 mD=2
     * 19/2=8,  op=1+1=2  mD=1
     * 8/2=4,   op=1  mD=0
     *
     * Started with the approach above, ran it on the test cases numerous times, like 8-10 and each time got some case
     *  wrong => tried multiple modifications basically by poking without deep understanding them first (like 4-5 significant
     *  approach variations)
     * => finally, by a combination of trial and error, my own dry running and the test examples (which are super useful and detailed)
     *  realized that its better to simply go top->bottom, not bottom->top.
     *
     * => the actual greedy rule: if we can divide by 2 => do it + decrement if the number is odd. When we're out of doubling
     *  operations, return left-target-1 (since we start with 1)
     *
     *  (why divide first? since the earlier we divide by 2, the greater we reduce the remaining amount of steps.
     *   i.o. if we decide to subtract more => we'd always make more moves, since each 1 subtracted on an even number before the division would be -2 if we just
     *  divided, e.g. (8-2)=6/2=(3-1)/2=1 is 2+1+2=5 moves vs 8/2=4/2=2/2=1 3 moves)
     *
     *
     * edge cases:
     *  - target == 1 => return 0 (the only case when the result == 0);
     *  - maxDoubles == 0 => return target - 1 always;
     *  - log2(target).toInt() == log2(target) => return log2(target) but isnt worth it: too narrow, no real gain;
     *  - target is a power of 2 and doublesLeft are greater or equal to the exponent => we simply divide until targetLeft == 1,
     *   each time the remainder is 0 => return operations, that is, the pure exponent.
     *
     *
     * 19
     * 9
     * 4
     * 2
     * 1
     *
     * Time: average/worst O(maxDoubles)
     *  - main loop: we divide [target] by 2 up to [maxDoubles] times. Iteration work is a const, the rest are consts.
     * Space: always O(1)
     *
     * -------- optimization
     *
     * perhaps there is a O(1) time solution though, but I don't see it
     */
    fun efficient(target: Int, maxDoubles: Int): Int {
        if (maxDoubles == 0 || target == 1) return target - 1

        var operations = 0
        var doublesLeft = maxDoubles
        var targetLeft = target
        while (targetLeft > 1 && doublesLeft > 0) {
            operations += 1 + (targetLeft % 2)
            targetLeft /= 2
            doublesLeft--
        }
        return operations + targetLeft - 1 // -1 cause we start with 1 (safe cause even if we only divide by 2, we'd end up min with 1 always)
    }
}

fun main() {
    println(
        MinimumMovesToReachTargetScore().efficient(
            target = 19,
            maxDoubles = 2,
        )
    )
}
