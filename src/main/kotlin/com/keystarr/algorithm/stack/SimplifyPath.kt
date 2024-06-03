package com.keystarr.algorithm.stack

import java.util.ArrayList
import java.util.LinkedList

/**
 * LC-71 https://leetcode.com/problems/simplify-path/description/
 * difficulty: medium
 * constraints:
 *  • 1 <= path.length <= 3000;
 *  • path consists of english upper/lower, digits, '.', '/' or '_'/.
 *
 * Final notes:
 *  • solved via [efficient] by myself in 60 minutes (so long!!!!);
 *  • live-ran by myself (not dry), realized a fundamental error and fixed it (submission - 1st time).
 *
 * Value gained:
 *  • tried to single-pass, parse input character-by-character and pattern-match commands like '.', '..' and go over multiple slashes,
 *      BUT it was a terrible idea => too much conditions and code;
 *  • split into segments (preprocess), ONLY THEN process commands => 2 passes, but so much cleaner;
 *  • perhaps I should spend less time designing the first idea and get to code quicker to not get stuck on edge cases
 *      and such, and think that over along the way?
 *  • [ArrayDeque] pop removes the FIRST ELEMENT! Not the last! removeLast for last
 *  • [LinkedList] here fits better imho, cause we may have many segments and dont know exactly how many we'll need =>
 *      no resizing => better time const at the cost of more (?) space const for pointers;
 *  • practices LIFO problem type pattern-recognition (as an element, not the entire solution!) and using a Stack.
 */
class SimplifyPath {

    /**
     * Transformations required:
     * - one of the subtasks is to remove the most recent subdirectory when encountering "..", and many such commands
     * might be present once after another => last subdirectory, first sub out => Stack.
     * - upon encountering '.' don't modify the resulting path;
     * - convert all "//" into '/';
     * - the given path always starts with '/', preserve that;
     * - if after processing there is at least one subdirectory => it must not end with a '/';
     * - treat more than 2 dots as a subdirectory name.
     *
     * Idea:
     *  - go through path, break it into segments (substrings between '/' or end of string); (time O(n)
     *  - go through segments and when segment is:
     *      - ".." -> result.pop()
     *      - "." -> continue;
     *      - else -> result.push(segment)
     *  - format result into a string, with "/" as a prefix and "/" as delimiter.
     *
     * Edge cases:
     *  - ".." at root => leaves root ('/');
     *  - more than 2 dots a subdir => handled;
     *  - more than one slash => handled;
     *  - if at least one subdir shouldn't end with '/' => handled;
     *  - no slash in the end of input, but last was a subdir name
     *
     * Time: always O(n), cause:
     *  - we break into segments for O(n). Technically we create m new strings, but their lengths combined add up to path.length,
     *      so creation of each substring is amortized O(1);
     *  - we process segments for O(n), cause removing/adding subdir takes O(1) for Stack based on LinkedList.
     *
     * Space: average/worst O(n) n=path.length, cause the amount of segments depends on path.length.
     */
    fun efficient(path: String): String {
        val segments = ArrayList<String>()
        var currentSegmentStartInd = START_IND_NOT_SET
        path.forEachIndexed { ind, char ->
            if (char == '/') {
                if (currentSegmentStartInd != START_IND_NOT_SET) {
                    segments.add(path.substring(currentSegmentStartInd, ind))
                    currentSegmentStartInd = START_IND_NOT_SET
                }
            } else {
                if (currentSegmentStartInd == START_IND_NOT_SET) currentSegmentStartInd = ind
            }
        }
        // last subdir name didn't end with a '/'
        if (currentSegmentStartInd != START_IND_NOT_SET) segments.add(path.substring(currentSegmentStartInd, path.length))

        val result = LinkedList<String>()
        segments.forEach { segment ->
            when (segment) {
                ".." -> if (result.isNotEmpty()) result.removeLast()
                "." -> return@forEach
                else -> result.addLast(segment)
            }
        }

        return result.joinToString(prefix = "/", separator = "/")
    }
}

private const val START_IND_NOT_SET = -1

fun main() {
    println(SimplifyPath().efficient("/home/user/Documents/../Pictures/../../../../..../"))
}

// preserved for fun’s sake - first implementation router :D
//
//path.forEachIndexed { ind, char ->
//    when (char) {
//        '/' -> if (result.isNotEmpty() && currentCommand.isNotEmpty()) {
//            result.add("/$currentCommand") // were collecting subdir name, append it
//            currentCommand = ""
//        }
//
//        '.' -> if (ind + 1 < path.length && path[ind + 1] == '.') {
//            if (ind + 2 < path.length && path[ind + 2] == '.') {
//                // exactly 3 or more consecutive dots
//                currentCommand += char
//            } else {
//                // exactly 2 consecutive dots
//                if (lastSubdirStartInd == -1) return@forEachIndexed
//                result.removeRange(lastSubdirStartInd, result.length)
//
//
//                // remove last subdir, if there are none, leave root '/'
//            }
//        } else {
//            // exactly 1 consecutive dot
//        }
//
//        else -> currentCommand += char
//    }
//}
//
//if (currentCommand.isNotEmpty()) result.append("/$currentCommand")
