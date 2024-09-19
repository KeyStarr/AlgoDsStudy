package com.keystarr.algorithm.deque.queue

/**
 * ⭐️ a cunning example of an efficiently solution based on 2 queues here, a tricky unique problem all around.
 * LC-649 https://leetcode.com/problems/dota2-senate/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= senate.length <= 10^4;
 *  • senate\[i] is either 'R' or 'D'.
 *
 * Final notes:
 *  • ⚠️⚠️ failed to do by myself in 1h, even the brute-force! I was pulling myself apart going back and forth trying to
 *   do the brute force solution and then thinking I've got an idea for an efficient one switching to that, then back again
 *   + brute force being not so trivial to avoid deleting from the middle of an array => failed to even do the brute force
 *   in the max time frame;
 *  • 🔥 what I could've done better: ⚠️ should've at least finished the brute, just focused on that one I guess, even though
 *   it would've significantly been harder than the efficient one to implement (but not to design!). Would've seen possibly
 *   the path to the efficient optimization then;
 *  • estimated time wrong at first for [efficient]: noticed that we reduce the size of the list in half every round if we
 *   don't terminate earlier => reasoned we have log(n) rounds and each about ~n iterations, BUT THATS WRONG! We have
 *   indeed log(n) rounds but the total number of iterations across all these, cause of the size always being decreased
 *   /2 sums up to n! n/2+n/8+n/16+.. ~= n
 *  • ❓❓ how would one even see the FIFO here to think of 2 queues???? what would they needed to have solved / learned
 *   beforehand, which concepts in their head?
 *
 * Value gained:
 *  • solved a game simulation type problem efficiently using 2 queues ("recycling" elements with these also) and a greedy
 *   rule (for the most optimal strategy).
 */
class Dota2Senate {

    // TODO: full resolve in 1-3 weeks

    /**
     * Observations:
     *  1. the order matters, since the earlier senator may remove the latter one;
     *  2. greedy: we're tasked to simulate each player playing optimally => the optimal strategy for each senator is to remove
     *   the first senator of the opposing party in the array. Why? Cause of #1, if they remove another one, they may
     *   needlessly lose an ally. Consider RDRDRD, should the first R remove the second D => the first D removes the second R,
     *   but if each R removes the closest D, then all R's survive.
     *
     * Is there a way to avoid simulating multiple rounds, but just do it in a single pass? Don't see it now.
     *
     * OK, how to at least efficiently simulate the multiple round system?
     *
     * (META - it was kinda hard to phrase these rules such to see FIFO in here!)
     *
     * But if we were to simulate, consider using two queues, one for each party:
     *  - we need to compare the first indexed element of each party to decide which one to remove;
     *  - the other element goes to the end of the queue for the next round of the simulation. Since we store indices,
     *   we need to also set its index with an offset, like if we'd actually remove it and add to the end of the [senate].
     *
     * Do the simulation until one of the queues is empty => return the winner as the other queue.
     *
     * There is always a winner since basically given 2 elements of the opposite parties we always remove 1, and do that
     * until one of the parties doesn't exist => we can't remove all elements of both parties, there would be at least 1 left
     * no matter what the starting size was, if it was >0.
     *
     * Time: always O(n)
     *  - initializing both queues costs O(n);
     *  - running the simulation:
     *   - each step we remove exactly 1 element, so at the end of the first round if we hadn't terminated earlier
     *    we have n/2 elements, then n/4, then n/8
     *    => we have iterations count across all rounds: n/2 + n/4 + n/8 +.. worst all these sum up to n.
     *
     * Space: always O(n)
     *  - we always have max exactly n elements across both queues at the start, then we have only less elements each round.
     *
     * RDDR RDDR
     *
     * round 1
     * 0 3 4 7
     * 1 2 5 6
     *
     * round 2
     * 8 10
     * 9 11
     *
     * round 3
     * 12 13
     */
    fun efficient(senate: String): String {
        val firstIndQueue = ArrayDeque<Int>()
        val secondIndQueue = ArrayDeque<Int>()
        senate.forEachIndexed { ind, senator ->
            if (senator == 'R') firstIndQueue.addLast(ind) else secondIndQueue.addLast(ind)
        }

        var maxInd = senate.length
        while (firstIndQueue.isNotEmpty() && secondIndQueue.isNotEmpty()) {
            val firstInd = firstIndQueue.removeFirst()
            val secondInd = secondIndQueue.removeFirst()
            if (firstInd < secondInd) firstIndQueue.addLast(maxInd) else secondIndQueue.addLast(maxInd)
            maxInd++
        }
        return if (firstIndQueue.isNotEmpty()) "Radiant" else "Dire"
    }

    /**
     * game simulation:
     *  we simulate until the victory is declared:
     *   single simulation: go through [senate] always one senator at a time:
     *    each senator can either "disable" a non-disabled senator from another party or declare victory if no senators
     *    from another party are left.
     *
     * Since all players act optimally -> the optimal strategy for any would be to simply ban another party's senator
     *  if there's one left.
     *
     *
     * RRDD
     *
     *
     * can there be a Tie?
     *  R - R wins
     *  D - D wins
     *  RD - R wins
     *  DR - D wins
     *  RDR - R wins
     *  DRD - D wins
     *  DDR - D wins
     *  RRD - R wins
     *
     * DDDRRRRR - R wins
     * 1st round DDDRRRRR, up until first R: DDDRR, other elements: DRR
     *  2nd round: DRR -> up until R DR -> R
     *  R wins
     *
     *
     * DDDRRRR - D wins
     *
     * ok, count of both senators matters certainly, does relative positioning matter?
     *
     * D R D R D R R
     *
     * D D D R
     * D D R
     * D D
     *
     *
     * D R R D
     *
     *
     *
     * R D R D R D R
     * R R R R
     *
     *
     * R D D R D R D R D
     * D D D D
     *
     * so the positioning actually does matter since when we encounter a new senator, it is either destroyed by the other
     * party's senator who was to act first and was in order previously, or gain votes for the current round for their own party
     *
     *
     * there can't be a tie. why though?
     *
     *
     * Brute:
     *  - until either party's count == 0:
     *   - go through [senate]:
     *    - if senate[i] is marked as deleted (deleted[i]==true) => skip, continue;
     *    - else:
     *     - if otherParty > 0:
     *      - deleted[i]=true
     *      - otherParty--
     *     - else:
     *      - currentParty++
     *
     * Time:
     * Space:
     */
    fun brute(senate: String): String {
        val subsequences = mutableListOf<Pair<Char, Int>>()
        var prevSenator = senate.first()
        var currentCount = 0

        var firstCount = 0
        var secondCount = 0
        senate.forEach { senator ->
            if (senator != prevSenator) {
                subsequences.add(senator to currentCount)
                currentCount = 0
                prevSenator = senator
            } else currentCount++

            if (senator == FIRST_PARTY_KEY) firstCount++ else secondCount++
        }

        while (true) {
            val (prevParty, prevCount) = subsequences[0]
            for (i in 1 until subsequences.size) { }
        }
    }

    private fun Char.otherPartyKey() = if (this == FIRST_PARTY_KEY) SECOND_PARTY_KEY else FIRST_PARTY_KEY
}

private const val FIRST_PARTY_KEY = 'R'
private const val SECOND_PARTY_KEY = 'D'

fun main() {
    println(
        Dota2Senate().efficient(
            "DDDRRRRR"
        )
    )
}
