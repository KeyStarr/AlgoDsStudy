package algos.divideandconquer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SortAnArrayTest {

    private val sut = SortAnArray()

    // list

    @Test
    fun `array itself on empty array`() {
        val input = intArrayOf()

        val output = sut.quicksort(input)

        assertEquals(input, output)
    }

    @Test
    fun `array itself on single number`() {
        val input = intArrayOf(99)

        val output = sut.quicksort(input)

        assertEquals(input, output)
    }

    @Test
    fun `swap numbers on two numbers with lowest second`() {
        val output = sut.quicksort(intArrayOf(5, 3))

        assertTrue(intArrayOf(3, 5).contentEquals(output))
    }

    @Test
    fun `array itself on two numbers with lowest first`() {
        val input = intArrayOf(3, 7)

        val output = sut.quicksort(input)

        assertEquals(input, output)
    }

    @Test
    fun `TODO`(){
        TODO("finish when ill come back for the problem")
    }

    // array


}
