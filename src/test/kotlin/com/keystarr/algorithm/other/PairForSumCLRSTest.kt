package com.keystarr.algorithm.other

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PairForSumCLRSTest {

    private val sut = PairForSumCLRS()

    @Test
    fun `false on empty set`() {
        runTest(
            numbers = emptySet(),
            targetSum = 13,
            expectedDoesContainPair = false,
        )
    }

    @Test
    fun `false on single number in the set`() {
        runTest(
            numbers = setOf(3),
            targetSum = 241332,
            expectedDoesContainPair = false,
        )
    }

    @Test
    fun `true on two numbers summing up to target`() {
        runTest(
            numbers = setOf(999, 1),
            targetSum = 1000,
            expectedDoesContainPair = true,
        )
    }

    @Test
    fun `false on multiple numbers none summing up to target`() {
        runTest(
            numbers = setOf(6, 423, 7, 54, 87978, 455, 213),
            targetSum = 33,
            expectedDoesContainPair = false,
        )
    }

    @Test
    fun `true on multiple numbers one pair summing up to target`() {
        runTest(
            numbers = setOf(6, 423, 7, 87978, 455, 213, 23),
            targetSum = 30,
            expectedDoesContainPair = true,
        )
    }

    @Test
    fun `true on multiple numbers multiple pairs summing up to target`() {
        runTest(
            numbers = setOf(6, 14, 7, 24, 455, 23, 16),
            targetSum = 30,
            expectedDoesContainPair = true,
        )
    }

    private fun runTest(
        numbers: Set<Int>,
        targetSum: Int,
        expectedDoesContainPair: Boolean,
    ) {
        val output = sut.fast(numbers, targetSum)
        assertEquals(expectedDoesContainPair, output)
    }
}