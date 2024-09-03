package com.keystarr.algorithm.graph

/**
 * LC-990 https://leetcode.com/problems/satisfiability-of-equality-equations/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= equations.size <= 500;
 *  • equations[i].length == 4;
 *  • equations[i][0] is a lowercase letter;
 *  • equations[i][1] is either '=' or '!';
 *  • equations[i][2] is '=';
 *  • equations[i][3] is a lowercase letter.
 *
 * Final notes:
 *  • done [brute] by myself in 30 mins, thought was going for O(n) time, but on the 2nd failed submission realized
 *   I missed a major case => actual time is O(n^2);
 *
 * Value gained:
 *  • practiced implementing an optimized brute-force via a map and sets;
 *  but the real value is to be gained still, since Union Find is the main tool here
 */
class SatisfiabilityOfEqualityEquations {

    // TODO: solve via a union find for O(n) time, after learning the concept

    /**
     * observe:
     *  - only two types of equations: equal or not equal;
     *  - rephrased goal: return true if there are no contradicting equations;
     *  - the catch is that there may be a deep chain of equalities / non-equalities between variables;
     *  - when does a contradiction arise?
     *   when either we have already recorded a set of relations such that we may conclude, that X == Z, but we encounter
     *   a new relation that implies otherwise. And vice versa.
     *   e.g. X == Y, Y == Z, T != Z, X != T
     *    the fourth equation is a contradiction, since first two imply that X == Z, and last two that X != Z
     *
     * => how to catch such contradictions efficiently?
     *
     * trivial approach:
     *  - 1st pass: check only equality equations and save all equal variables into a single set
     *   (=> there may be multiple sets, different sets of equal variables);
     *  - 2nd pass: check only non-equality equations and, if both variables are in the same set => return false;
     *  - if terminated => return true.
     *
     * worst case is when each equality equation defines two distinct variables =>
     *  - we have to create up to n sets, all holding up to 2*n variables in total;
     *  - through 2nd pass worst is we have to for each of the n/2 non-equality equations iterate through all sets.
     *   since sets amount is up to n => time is O(n^2)
     *
     * -------------------------------- can we do faster?
     *
     * we could use a map to associate each variable with a set of all variables that it is equal to => sets creations and
     *  non-equality 2nd pass would be both O(n) => O(n) time
     *
     * edge cases:
     *  - equations.size == 1 => never a contradiction => always return true, correct as-is;
     *  - all equations are equalities or non-equalities => never a contradiction => always return true, correct as-is;
     *  - ⚠️ MISSED A SPECIAL CASE OF THE SAME VAR ON BOTH SIDES OF THE EQUATION!!!!
     *   if equality => never a contradiction
     *   if non-equality => ALWAYS A CONTRADICTION (missed it on the stage of reasoning about possible contradictions!!!)
     *
     * "a==b","e==c","b==c","a!=e"
     * variableToEquals = { a: (a,b) b: (a,b) e: (e,c) c: (c,e)    }
     *
     * Time: average/worst O(n)
     * Space: average/worst O(n)
     *
     * ------ ⚠️⚠️ MISSED A SECOND EDGE CASE, MAJOR ONE:
     * when we encounter equation X, we may have already processed many equations for both variables => now then we need
     * to merge two sets together, e.g. add all of second into first, and re-associate all second set variables to the combined (first) set.
     * =>
     * worst is we have many such operations, and the amount of variables two add from one set to another is up to O(n)
     * => Time is actually O(n^2) still!
     *
     * e.g. ["a==b","e==c","b==c","a!=e"], expected=false
     *
     * -------------------------------- can we do an actual time O(n) or at least O(nlogn)?
     * probably there is a O(n) time efficient solution, maybe even single pass
     */
    fun brute(equations: Array<String>): Boolean {
        val variableToEquals = mutableMapOf<Char, MutableSet<Char>>()
        equations.forEach { equation ->
            val firstVar = equation[0]
            val secondVar = equation[3]
            if (equation[1] == '!') {
                if (firstVar == secondVar) return false // edge case, a trivial contradiction
                return@forEach // non-equality
            }

            if (!variableToEquals.contains(firstVar) && !variableToEquals.contains(secondVar)) {
                val equalitySet = mutableSetOf(firstVar, secondVar)
                variableToEquals[firstVar] = equalitySet
                variableToEquals[secondVar] = equalitySet
            } else if (!variableToEquals.contains(firstVar)) {
                val equalitySet = variableToEquals.getValue(secondVar).apply { add(firstVar) }
                variableToEquals[firstVar] = equalitySet
            } else if (!variableToEquals.contains(secondVar)) {
                val equalitySet = variableToEquals.getValue(firstVar).apply { add(secondVar) }
                variableToEquals[secondVar] = equalitySet
            } else {
                val firstEqualitySet = variableToEquals.getValue(firstVar)
                val secondEqualitySet = variableToEquals.getValue(secondVar)
                if (firstEqualitySet === secondEqualitySet) return@forEach
                // seen equality equations for both variables already => need to merge the sets, keep just one
                firstEqualitySet.addAll(secondEqualitySet)
                secondEqualitySet.forEach { variable -> variableToEquals[variable] = firstEqualitySet }
            }
        }

        equations.forEach { equation ->
            if (equation[1] == '=') return@forEach

            val firstVar = equation[0]
            val secondVar = equation[3]
            // check only firstVar, cause if firstVar and secondVar are equal, both would be associated with the same set in the map
            if (variableToEquals[firstVar]?.contains(secondVar) == true) return false
        }

        return true
    }
}

fun main() {
    println(
        SatisfiabilityOfEqualityEquations().brute(
            equations = arrayOf("a==b", "e==c", "b==c", "a!=e"),
        )
    )
}
