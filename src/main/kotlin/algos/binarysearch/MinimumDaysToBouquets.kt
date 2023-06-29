package algos.binarysearch

import java.lang.Math.abs
import java.lang.Math.min

// https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/description/
// TODO: couldn't figure out the solution in reasonable time (like, 3 hours), gonna leav it all messy in here
//  and come back when I'm smarter.

fun minDaysForBouquets(bloomDays: IntArray, targetBouquets: Int, flowersInABouquet: Int): Int {
    val targetFlowers = targetBouquets * flowersInABouquet.toLong()
    if (targetFlowers > bloomDays.size) return -1
    if (targetFlowers == bloomDays.size.toLong()) return bloomDays.maxOf { it }

//    val usedFlowers = BooleanArray(size = bloomDays.size) REWRITE intarray[i] = -1 instead?
    var currentAmountOfBouquets = 0
    val unbreakableThreshold =
        (targetBouquets - 1) * flowersInABouquet + (targetBouquets - 1) * (flowersInABouquet - 1) + flowersInABouquet
    var maxBloomInAllBouquets = Int.MIN_VALUE

    if (bloomDays.size >= unbreakableThreshold) {
        while (currentAmountOfBouquets < targetBouquets) {
            println(bloomDays.joinToString(separator = ", "))

            // find the min bloom days value
            var minBloomInd = -1
            var minBloom = Int.MAX_VALUE
            for (i in bloomDays.indices) {
                if (bloomDays[i] != FLOWER_NOT_AVAILABLE && bloomDays[i] < minBloom) {
                    minBloom = bloomDays[i]
                    minBloomInd = i
                }
            }
            if (minBloomInd == -1) {
                // no value to use (either inaccessible or used in a bouquet already)
                return -1
            }

            // find the interval with a minimum maximum value
            var minMaxBloomInInterval = Int.MAX_VALUE
            var minMaxBloomIntervalStartInd = -1
            val intervalCalcIndStart: Int = min(abs(minBloomInd - (flowersInABouquet - 1)), 0)
            val outerBoundEdge: Int = (bloomDays.lastIndex) - (minBloomInd + (flowersInABouquet - 1))
            val intervalCalcIndEnd: Int = if (outerBoundEdge < 0) bloomDays.size - flowersInABouquet else minBloomInd

//            if (intervalCalcIndEnd - intervalCalcIndStart < flowersInABouquet) {
//                bloomDays[minBloomInd] = FLOWER_NOT_AVAILABLE // no valid intervals at all
//                continue
//            }

            rootLoop@ for (i in intervalCalcIndStart..intervalCalcIndEnd) {
                var maxBloom = Int.MIN_VALUE
                for (j in i until i + flowersInABouquet) {
                    if (bloomDays[j] == -1) {
                        // can't take interval - a flower is already in use
                        continue@rootLoop
                    }

                    if (bloomDays[j] > maxBloom) {
                        maxBloom = bloomDays[j]
                    }
                }

                if (maxBloom < minMaxBloomInInterval) {
                    minMaxBloomInInterval = maxBloom
                    minMaxBloomIntervalStartInd = i
                }
            }

            if (minMaxBloomIntervalStartInd == -1) {
                bloomDays[minBloomInd] = FLOWER_NOT_AVAILABLE // no interval with all usable flowers
                println("ind $minBloomInd not available - no intervals with all usable flowers")
                continue
            }

            // a bouquet is done
            for (i in minMaxBloomIntervalStartInd until minMaxBloomIntervalStartInd + flowersInABouquet) {
                bloomDays[i] = FLOWER_NOT_AVAILABLE
                println("ind $i not available - taken into a bouquet")
            }
            currentAmountOfBouquets++
            if (minMaxBloomInInterval > maxBloomInAllBouquets) maxBloomInAllBouquets = minMaxBloomInInterval
        }

        println(bloomDays.joinToString(separator = ", "))
        return maxBloomInAllBouquets
    } else {
        // a general case which will include the if branch above as a special "happy" case




        // possible flower count:
        // min = targetFlowers
        // max = unbreakableThreshold - 1
        // interval size = flowersInABouquet * targetBouquets - flowersInABouquet - targetBouquets + 1


        TODO("how to select intervals with the min max number inside such that we don't break the possibility to even pick the required amount of intervals? coz of the values on edges")
    }
}


private const val FLOWER_NOT_AVAILABLE = -1


fun main() {
//    println(minDaysForBouquets(bloomDays = intArrayOf(1,10,3,10,2), targetBouquets = 3, flowersInABouquet = 1))
    println(minDaysForBouquets(bloomDays = intArrayOf(3, 1, 2, 8, 1, 7, 6), targetBouquets = 3, flowersInABouquet = 2))
}


/*
1. find current "the minimum amount of days to bloom" unused flower
2. make a bouquet with it, take any amount of flowers to the left and/or to the right, mark it as used
repeat p.1 until:
    a) we have enough flowers - return the min number of [bloomDays]
    b) we're out of flowers



6 (1 2 4) 1 8 (7 4 5) 9, targetBouquets = 3, flowersInABouquet = 3

    can we break it?
        yes IF we can make (targetBouquets - 1) splits and the (targetBouquets) remaining intervals may not contain EACH flowersInABouque

        (targetBouquets - 1) * flowersInABouquet + (targetBouquets) * flowersInABouquet >= n

        (3 - 1) * 2 + (3) * 2 = 10


6 (1 2) 8 (1 4) 1 1, targetB = 3, flowersB = 2

    n >= (targetB - 1) * flowersB + (targetB - 1) * (flowersB - 1) + 1 * flowersB

    (3 - 1) * 2 + (3 - 1) * (2 - 1) + 1 * 2 = 4 + 2 + 2 = 8



edge cases, unusable minimums
7 6 (1 2) 8 (1 4) 3, targetB = 3, flowersB = 2


3 (1 2) 8 (1 4) 7 6, targetB = 3, flowersB = 2















6 1 2 4 1 7 8, targetBouquets = 2, flowersInABouquet = 3
    => fail on step 2, isolated 6 to the left with the very first step, can't do a composition


2 3 1 8 3 2 1 4 5 1 6 2 9 3 1, targetBouquets = 2, flowersInABouquet = 2
    a minimum amount of days: max ((2, 3) & (3, 1)) = 3



1 8 9 2 3 8 8 1 9 9, targetBouquets = 2, flowersInABouquet = 2


algorithm idea? INTUITION - find the interval (a bouquet) with the minimum maximum amount of bloomDay IN A WAY
    that doesn't break the possibility of making a FULL collection (fulfilling the amount of target bouquets)


    - break: is there a threshold amount of N relative to targetBouquets and flowersInABouquet such that
            we can't break the data in we just follow the first rule? (ofc just not taking EDGE flowers)





a) find local minimums in a shrinking collection;
b) make a selection in a way that it doesn't ruin the set. Not only that - make it in an optimal way?

 */