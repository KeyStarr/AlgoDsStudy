package com.keystarr.algorithm.greedy

import java.util.PriorityQueue

/**
 * ⭐️LC-502: https://leetcode.com/problems/ipo/submissions/1302940735/
 * difficulty: hard
 * constraints:
 *  • 1 <= k <= 10^5
 *  • 0 <= initialCapital <= 10^9
 *  • 1 <= n == profits.length == capital.length <= 10^5
 *  • 0 <= profits\[i] <= 10^4
 *  • 0 <= capital\[i] <= 10^9
 *
 * Final notes:
 *  • basically we run through the sorted projects and try to unlock as many projects as possible by using the least amount of
 *  the highest profit project available => if we run until the end have projects left to do, then we can choose any projects
 *  with the highest profits;
 * • a more succinct general greedy formula here: "at each move, choose the most profitable project of all affordable"
 *  greedy has 2 components here:
 *   • a single move "choose the most profitable project"
 *   • an incomplete picture, a local optimum based on the current data state "out of all affordable"
 * • repeatedly getting maximums => heap! Realized it in runtime by myself. Wow, hanging the tools by 'em use case hooks
 *  in my head really paid off here;
 * • what a beautiful synergy between the greedy approach and a priority queue;
 * • solved by myself, only knowing a hint the there's a greedy solution here;
 * • failed 3 (!!!!!!!!!) submissions:
 *  • 1st: because failed an edge case where decremented currentInd in the inner loop got to •1 (out ouf array bounds);
 *  • 2nd: erroneously sorted the projects by profit and not the capital ('em tricky base test cases ran fine!),
 *   not sure how I missed that, it is in the middle of being a typo and a design•flaw;
 *  • 3rd: fixed the sorting, but finally failed at the major design flaw, as I have not taken into account that, once we
 *   hit the block and start doing projects we must select the maximum profitable ones out of the affordable (seen so far),
 *   and not just backwards by the order of projects being seen! (cause profits have no correlation to capital (by which we sorted)
 *   what•so•ever!)
 *
 * Value gained:
 *  • what a beautiful, almost a quintessential problem for a greedy solution. A lot of nuance yet a great metaphor, so understandable;
 *  • practiced recognizing the greedy solution to a sophisticated problem, practiced reaching an efficient solution via a maxHeap.
 */
class IPO {

    /**
     * Maximum capital + rules for a single move => try greedy?
     *
     * Idea: sort project by its capital, every move: find the first project which capital is greater than our current capital,
     *  then go backwards and do all undone projects prioritizing ones with the highest profit until we finally can do project\[i],
     *  but don't do it, move done.
     *  AND if we've iterated through all projects and projectsDone < k then add up all projects left with maximum profits
     * until we've reached k projects used.
     *
     *
     * Design: (outdated)
     * - iterate through profits and capitals and build the Array<Project>
     * - sort projects by capital ascending
     * - currentCapital = 0, projectsDone = 0
     * - iterate through profits until either projectsDone == k or i == profits.size:
     *   - currentInd = i
     *   - while currentCapital < projects\[i].capital:
     *      - currentInd--
     *      - if (projects[currentInd].isUsed || currentInd == -1) return currentCapital
     *      - currentCapital += projects[currentInd]
     *      - projectsDone++
     * - if projectDone != k:
     *  - iterate through projects from profits.size - 1 to 0 until either we hit 0 or projectDone == k:
     *   - if (!projects\[i].isUsed) currentCapital += projects
     * - return currentCapital
     *
     * Project(profit, minCapital, isDone = false)
     *
     * Edge cases:
     *  - max total capital = (all max) initialCapital + k*10^4 = 10^9 + 10^5 * 10^4 = 10 ^9 + 10^9 = 2 * 10^9 fits into Int,
     *   as the problem description stated.
     *  - we cant do any projects cause min mincapitalrequired is greater than our initial capital => return the initial capital
     *   => early check and return after sorting
     *  - profits.length == 0 => return initialCapital
     *  - k > profits.size => we'll either take all the projects, or stop on a project which requires minCapital that exceeds
     *   our current capital having done all projects prior to that, that we could've;
     *  - all profits == 0 => we'd iterate until the end taking none, and then take k profits iterating backwards, basically
     *   just returning the initialCapital.
     *
     * Time: O(nlogn)
     *  - projects array creation O(n)
     *  - project sort O(nlogn)
     *  - forward loop average/worst O(n*logn)
     *   - despite the inner loop, we take each project at most once forward, and remove it at most once from the heap
     *   - heap add is O(logn) and remove is O(logn)
     *  - backward loop average/worst O(n)
     *
     * Space: O(n) for the projects array and a heap
     */
    fun efficient(maxProjects: Int, initialCapital: Int, profits: IntArray, capital: IntArray): Int {
        val n = profits.size
        val projects = ArrayList<Project>(n)
        for (i in profits.indices) projects.add(Project(minCapital = capital[i], profit = profits[i]))
        projects.sortBy { it.minCapital }

        var currentCapital = initialCapital
        var projectsDone = 0
        var i = 0
        val maxProfitHeap = PriorityQueue<Project> { o1, o2 -> o2.profit - o1.profit }
        while (i < n && projectsDone < maxProjects) {
            // how to find all projects with max profits so far? repeatedly get the max profit
            // => use a heap?
            while (currentCapital < projects[i].minCapital) {
                if (maxProfitHeap.isEmpty()) return currentCapital
                val project = maxProfitHeap.remove()
                currentCapital += project.profit
                projectsDone++
            }
            maxProfitHeap.add(projects[i])
            i++
        }

        while (projectsDone < maxProjects && maxProfitHeap.isNotEmpty()) {
            val project = maxProfitHeap.remove()
            currentCapital += project.profit
            projectsDone++
        }

        return currentCapital
    }

    /**
     * Idea:
     *  - sort the projects by capital same as in [efficient];
     *  - repeat the move until we either have done k projects or we have no affordable projects left:
     *   - while we have projects left, starting from the first project, while the projects are affordable, add them into the maxHeap;
     *   - we either ran out of projects or encountered an unaffordable project => add current max project profit to the
     *    currentCapital (remove it from the heap);
     *  - return currentCapital
     *
     * Edge cases:
     *  - we have run out of projects to consider => add maximum profitable projects that were seen, but only up to [maxProjects].
     *
     * Time:
     *  - sorting: always O(nlogn)
     *  - main loop: average/worst O((k+n)*logn)
     *      - each heap add/remove is O(logn);
     *      - inner while loop would run at most n times, add into the heap all projects, so at most n times;
     *      - heap.remove() would be called at most k times.
     * Space: O(n)
     *
     * ----
     *
     * - in [efficient] the outer loop is for adding and the inner loop is for removing
     *  AND the outer loop goes until we have just seen all projects or done exactly maxProjects;
     *
     * - in [efficientCleaner] the outer loop is for removing and the inner loop is for adding
     *  AND the out loop goes until we have done exactly maxProjects (or early returned having ran out of affordable projects)
     *
     * So here we just save the bother with emptying the heap in case all projects were affordable and with reaching the last
     * we still can do some projects, no need for another loop to do that.
     */
    fun efficientCleaner(maxProjects: Int, initialCapital: Int, profits: IntArray, capital: IntArray): Int {
        val n = profits.size
        val projects = ArrayList<Project>(n)
        for (i in profits.indices) projects.add(Project(minCapital = capital[i], profit = profits[i]))
        projects.sortBy { it.minCapital }

        var currentCapital = initialCapital
        var currentProjectInd = 0
        val maxProfitHeap = PriorityQueue<Project> { o1, o2 -> o2.profit - o1.profit }
        repeat(maxProjects) {
            while (currentProjectInd < n && currentCapital >= projects[currentProjectInd].minCapital) {
                maxProfitHeap.add(projects[currentProjectInd])
                currentProjectInd++
            }
            if (maxProfitHeap.isEmpty()) return currentCapital
            currentCapital += maxProfitHeap.remove().profit
        }
        return currentCapital
    }

    class Project(val minCapital: Int, val profit: Int)
}

fun main() {
    println(
        IPO().efficient(
            maxProjects = 2,
            initialCapital = 0,
            profits = intArrayOf(1, 2, 3),
            capital = intArrayOf(0, 9, 10),
        )
    )
}
