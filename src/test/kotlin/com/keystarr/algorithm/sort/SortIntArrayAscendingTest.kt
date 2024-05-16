package com.keystarr.algorithm.sort

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

// incomplete, but most major test cases.
// don't impose a requirement on that the input array itself must be sorted, don't check references but only contents.
class SortIntArrayAscendingTest {

    // crude tests re-usage, works fine since we need tests in this project only while we work through
    // a single current algorithm
    private val sut = object : IntArraySorter {
        override fun invoke(nums: IntArray): IntArray = InsertionSort().ascendingClassicRecursive(nums)
    }

    @Test
    fun `no change on empty array`() {
        val input = intArrayOf()

        val output = sut(input)

        val expected = intArrayOf()
        assertContentEquals(expected, output)
    }

    @Test
    fun `no change on single number`() {
        val input = intArrayOf(99)

        val output = sut(input)

        val expected = intArrayOf(99)
        assertArrayEquals(expected, output)
    }

    @Test
    fun `no change on multiple same numbers`() {
        val input = intArrayOf(-33333, -33333, -33333, -33333, -33333)

        val output = sut(input)

        val expected = intArrayOf(-33333, -33333, -33333, -33333, -33333)
        assertArrayEquals(expected, output)
    }

    @Test
    fun `reversed on two numbers with lowest second`() {
        val input = intArrayOf(5, 3)

        val output = sut(input)

        val expected = intArrayOf(3, 5)
        assertArrayEquals(expected, (output))
    }

    @Test
    fun `no change on two numbers with lowest first`() {
        val input = intArrayOf(3, 7)

        val output = sut(input)

        val expected = intArrayOf(3, 7)
        assertContentEquals(expected, output)
    }

    @Test
    fun `reversed on all descending multiple numbers`() {
        val input = intArrayOf(99, 55, 44, 22, 11, 6, 2, 1, 0)

        val output = sut(input)

        val expected = intArrayOf(0, 1, 2, 6, 11, 22, 44, 55, 99)
        assertArrayEquals(expected, output)
    }

    @Test
    fun `all ascending on random order multiple numbers`() {
        val input = intArrayOf(
            4234, 4, 342, 543543645, 543543645, 999999999,
            0, -1, -999999999, 55555, -456545, 543543645, -4323, -3154, 9,
        )

        val output = sut(input)

        val expected = intArrayOf(
            -999999999, -456545, -4323, -3154, -1, 0, 4, 9, 342, 4234,
            55555, 543543645, 543543645, 543543645, 999999999,
        )
        assertArrayEquals(expected, output)
    }

    @Test
    fun `same order on already all ascending multiple numbers`() {
        val input = intArrayOf(-765, -3, 0, 1, 99, 1000, 1001, 999999999)

        val output = sut(input)

        val expected = intArrayOf(-765, -3, 0, 1, 99, 1000, 1001, 999999999)
        assertArrayEquals(expected, output)
    }

    @Test
    fun `swap only 1 number on all same numbers but one`() {
        val input = intArrayOf(100, 100, -1, 100, 100)

        val output = sut(input)

        val expected = intArrayOf(-1, 100, 100, 100, 100)
        assertArrayEquals(expected, output)
    }
}

interface IntArraySorter {
    operator fun invoke(nums: IntArray): IntArray
}
