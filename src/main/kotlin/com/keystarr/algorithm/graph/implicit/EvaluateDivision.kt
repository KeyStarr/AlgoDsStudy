package com.keystarr.algorithm.graph.implicit

/**
 * ⭐️LC-399 https://leetcode.com/problems/evaluate-division/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= equations.length <= 20
 *  • 1 <= queries.length <= 20
 *  • equations\[i].length == 2
 *  • queries\[i].length == 2
 *  • 1 <= the length of a single variable in both equations and queries <= 5
 *  • variable name is always lowercase English and/or digits
 *  • values.length == equations.length
 *  • 0.0 < values\[i] <= 20.0
 *
 * Final notes:
 *  • solved via [efficient] by myself in 1h;
 *  • while practising implicit graphs (so I knew the solution involves a graph, but only that);
 *  • even with a hint that the solution to this problem somehow involves a graph I had some time when I DID NOT UNDERSTAND
 *   HOW THE GRAPH WOULD EVEN BE FEASIBLE to model the input data and achieve the result! But then I just tried to model
 *   it out of the graph definition and think, ahem, what would the nodes be? What would the relations between them be?
 *   And as I drew the graph I still couldn't see it, and when I drew the example 2 equations and tried to compute a query
 *   I could see it clear as day.
 *
 * Value gained:
 *  • wow, problems that, at first, look nothing like graphs and don't conform to the classic graph inputs and mention
 *   nothing of the graph theory may actually be efficiently solved using graph theory by modelling the input as a graph
 *   and applying efficient graph traversal techniques!
 *  • how to know if the problem's solution probably involves a graph? If we're talking about a collection of some elements
 *   that are somehow related, and we want to know the fewest operations possible to solve these?
 */
class EvaluateDivision {

    /**
     * Problem rephrase:
     *  - originally we are given:
     *      - equations: a 2D array where each element is an array of size 2, where each element is a string variable;
     *      - values: an IntArray where each ith value is an answer for the division of equations[i][0]/equations[i][1]
     *      - queries: same structure as equations, but we must calculate, if possible, an answer for each queries[i] pair,
     *       and the resulting array would be the answer;
     *      - entire input is always valid, no division by 0, no contradictions, but there might be undefined variables in queries.
     *
     * Cases:
     *  - queries pair states:
     *      - a variable that is not present in equations => always -1
     *      - same variable twice => always 1.0
     *      - different variables, both present in equations => calculate the answer:
     *          - if same 2 variables are present in a pair of equations:
     *              - if same order => return values[i]
     *              - if reverse order => return 1/values[i]
     *          - if 2 variables each of which was present in different equations =>
     *
     * Idea:
     *  - represent [equations] and [values] as a directed weighted graph (not a tree!):
     *      - we have a set of variables which are related to one another via the result of their respective division
     *       and need to compute divisions of arbitrary relations, which may or not involve the variables from this set
     *      - node=variable name
     *      - edge=equations[i], actually, 2 edges:
     *          - from node equations[i][0] to equations[i][1] with weight values[i]
     *          - and from equations[i][1] to equations[i][0] with weight 1/values[i]
     *      - iterate over equations and convert it (along with values) into node->edges: HashMap
     *  - answer = IntArray(size = queries.size)
     *  - iterate through queries:
     *      - if queries[i][0] == query[i][1] => answer[i] = 1
     *      - if !map.contains(queries[i][0]) || !map.contains(queries[i][0]) => answer[i] = -1
     *          because if a variable was not defined in equations, we cannot give an answer for it's relation
     *      - else => answer[i] = dfs(from=queries[i][0], to=queries[i][1])
     *  - return answer
     *
     * pathWeightDfs(node: String?, target: String, nodeToEdgesMap: Map<String,List<Edge>>): Double
     *  edges = nodeToEdgesMap\[node]
     *  if (edges == null) return -1
     *  for edge in edges:
     *      if (edge.toNode == target) return edge.weight
     *      weightSoFar = dfs(edge.toNode, target, nodeToEdgesMap)
     *      if (weightSoFar != -1) return edge.weight * weightSoFar
     *  return -1
     *
     * Edge(toNode: String, weight: Double)
     *
     * Edge cases:
     *  - there are duplicate equations that require DFS => might do an optimization to store answers in a map where
     *   key is the equation => for new query check if we already have an answer and fill it if we have + convert map
     *   and the end into an array;
     *  - cases for undefined variables and same variable division are already accounted for;
     *  - values are never 0.
     *
     * Time: average/worst (equations.size + queries.size * (variablesAmount + equations.size * 2) =
     *  (equations.size + queries.size*(equations.size + equations.size * 2) = (equations.size + queries.size*equations.size)
     *  = (queries.size*equations.size)
     *  - preprocessing equations+values into a map => always O(equations.size)
     *  - computing answers => average/worst O(queries.size * (variablesAmount + equations.size * 2)
     *      variablesAmount depends on equations.size
     *  - pathWeightDfs => average/worst O(variablesAmount + equations.size * 2)
     * Space: O(equations.size)
     *  - nodeToEdgesMap average/worst space = O(variablesAmount + equations.size*2) = O(equations.size)
     *  - dfs callstack average/worst O(equations.size)
     */
    fun efficient(equations: List<List<String>>, values: DoubleArray, queries: List<List<String>>): DoubleArray {
        val nodeToEdgesMap = mutableMapOf<String, MutableList<Edge>>()
        equations.forEachIndexed { ind, equation ->
            val firstNode = equation[0]
            val secondNode = equation[1]
            val divisionResult = values[ind]
            nodeToEdgesMap.getOrPut(firstNode) { mutableListOf() }.add(Edge(secondNode, divisionResult))
            nodeToEdgesMap.getOrPut(secondNode) { mutableListOf() }.add(Edge(firstNode, 1 / divisionResult))
        }

        val answers = DoubleArray(queries.size)
        val seen = mutableSetOf<String>()
        queries.forEachIndexed { ind, query ->
            val firstNode = query[0]
            val secondNode = query[1]
            answers[ind] = when {
                !nodeToEdgesMap.contains(firstNode) || !nodeToEdgesMap.contains(secondNode) -> -1.0
                firstNode == secondNode -> 1.0
                else -> pathWeightDfs(
                    node = firstNode,
                    targetNode = secondNode,
                    nodeToEdgesMap = nodeToEdgesMap,
                    seen = seen,
                )
            }
            seen.clear()
        }

        return answers
    }

    private fun pathWeightDfs(
        node: String,
        targetNode: String,
        nodeToEdgesMap: Map<String, List<Edge>>,
        seen: MutableSet<String>,
    ): Double {
        val edges = nodeToEdgesMap[node] ?: return -1.0
        edges.forEach { edge ->
            if (seen.contains(edge.toNode)) return@forEach
            if (edge.toNode == targetNode) return edge.weight
            seen.add(edge.toNode)
            val weightSoFar = pathWeightDfs(
                node = edge.toNode,
                targetNode = targetNode,
                nodeToEdgesMap = nodeToEdgesMap,
                seen = seen,
            )
            if (weightSoFar != -1.0) return weightSoFar * edge.weight
        }
        return -1.0
    }

    private class Edge(
        val toNode: String,
        val weight: Double,
    )
}

fun main() {
    println(
        EvaluateDivision().efficient(
            equations = listOf(
                listOf("a", "b"),
                listOf("b", "c"),
            ),
            values = doubleArrayOf(2.0, 3.0),
            queries = listOf(
                listOf("a", "c"),
                listOf("b", "a"),
                listOf("a", "e"),
                listOf("a", "a"),
                listOf("x", "x"),
            )
        ).contentToString()
    )
}
