package com.keystarr.algorithm.search

import com.keystarr.algorithm.search.binarysearch.BinarySearch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

// don't check for target int or collection elements overflow, cause task specifies all are valid integers
internal class BinarySearchArrayTest {

    private val sut = BinarySearch()

    @Test
    fun `-1 on an empty collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(),
            target = 435324,
            expectedTargetIndex = -1,
        )
    }

    @Test
    fun `0 on a single matching element`() {
        runTest(
            sortedElementsAscending = intArrayOf(1),
            target = 1,
            expectedTargetIndex = 0,
        )
    }

    @Test
    fun `-1 on a single non matching element`() {
        runTest(
            sortedElementsAscending = intArrayOf(2432454),
            target = 46453635,
            expectedTargetIndex = -1,
        )
    }

    @Test
    fun `valid index on target within odd collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(1, 2, 3, 4, 5),
            target = 4,
            expectedTargetIndex = 3,
        )
    }

    @Test
    fun `valid index on target within even collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(1, 2, 3, 4),
            target = 4,
            expectedTargetIndex = 3,
        )
    }

    @Test
    fun `-1 on target not within even collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(-3, 40, 1005, 33333),
            target = 33335,
            expectedTargetIndex = -1,
        )
    }

    @Test
    fun `-1 on target not within odd collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(-3, 40, 1005, 33333, 56545),
            target = 33335,
            expectedTargetIndex = -1,
        )
    }

    @Test
    fun `any valid index on multiple target values present`() {
        runTest(
            sortedElementsAscending = intArrayOf(1, 30, 30, 30, 3523, 5767, 57476),
            target = 30,
            expectedPossibleTargetIndices = intArrayOf(1, 2, 3, 4),
        )
    }

    @Test
    fun `return null on target below the minimum element of the collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(7, 20, 50, 90, 1000),
            target = -1,
            expectedTargetIndex = -1,
        )
    }

    @Test
    fun `return null on target above the maximum element of the collection`() {
        runTest(
            sortedElementsAscending = intArrayOf(455654, 3445320, 65645665, 321314435),
            target = 321314439,
            expectedTargetIndex = -1,
        )
    }

    private fun runTest(
        sortedElementsAscending: IntArray,
        target: Int,
        expectedTargetIndex: Int,
    ) {
        val targetIndex = searchImpl(
            sortedElementsAscending = sortedElementsAscending,
            target = target,
        )

        assertEquals(expectedTargetIndex, targetIndex)
    }

    private fun runTest(
        sortedElementsAscending: IntArray,
        target: Int,
        expectedPossibleTargetIndices: IntArray,
    ) {
        val targetIndex = searchImpl(
            sortedElementsAscending = sortedElementsAscending,
            target = target,
        )

        assertTrue(expectedPossibleTargetIndices.contains(targetIndex))
    }

    private fun searchImpl(sortedElementsAscending: IntArray, target: Int): Int =
        sut.recursive(
            nums = sortedElementsAscending,
            target = target,
        )
}
