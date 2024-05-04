package algos.other

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

// only for my original implementation of the algo, after checking with solution I've had to reverse it to binary representation
// from left-to-right (originally I've written right-to-left) => decided to not do same for test cases due to time constraints.
class IntBinaryArraySumCLRSTest {

    private val sut = IntBinaryArraySumCLRS()

    @Test
    fun `single 0 bit on empty arrays`() {
        val output = sut.naive(
            a = intArrayOf(),
            b = intArrayOf(),
            arraySize = 0,
        )

        assertArrayEquals(intArrayOf(0), output)
    }

    @Test
    fun `single 1 on both arrays with 1 element, both summing to 1`() {
        val output = sut.naive(
            a = intArrayOf(1),
            b = intArrayOf(0),
            arraySize = 1,
        )

        assertArrayEquals(intArrayOf(0, 1), output)
    }

    @Test
    fun `regular sum on no bit transfer multiple elements`() {
        val output = sut.naive(
            a = intArrayOf(1, 0, 0, 1),
            b = intArrayOf(0, 1, 1, 0),
            arraySize = 4,
        )

        assertArrayEquals(intArrayOf(0, 1, 1, 1, 1), output)
    }

    @Test
    fun `correct sum on multiple interleaving single bit transfers`() {
        val output = sut.naive(
            a = intArrayOf(0, 1, 0, 0, 1),
            b = intArrayOf(0, 1, 0, 0, 1),
            arraySize = 5,
        )

        assertArrayEquals(intArrayOf(0, 1, 0, 0, 1, 0), output)
    }

    @Test
    fun `correct sum on multiple consecutive bit transfers`() {
        val output = sut.naive(
            a = intArrayOf(0, 0, 1, 1, 0, 1),
            b = intArrayOf(0, 1, 1, 1, 1, 1),
            arraySize = 6,
        )

        assertArrayEquals(intArrayOf(0, 1, 0, 1, 1, 0, 0), output)
    }

    @Test
    fun `correct sum on bit transfer exceeding arraySize`() {
        val output = sut.naive(
            a = intArrayOf(1, 0),
            b = intArrayOf(1, 0),
            arraySize = 2
        )

        assertArrayEquals(intArrayOf(1, 0, 0), output)
    }

    @Test
    fun `correct sum on maximum bit count with all transfers`() {
        val output = sut.naive(
            a = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
            b = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
            arraySize = 15,
        )

        assertArrayEquals(
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0),
            output,
        )
    }

    @Test
    fun `error on incorrect a size`() {
        assertThrows(IllegalArgumentException::class.java) {
            sut.naive(
                a = intArrayOf(1, 0),
                b = intArrayOf(0, 1, 0),
                arraySize = 3,
            )
        }
    }

    @Test
    fun `error on incorrect b size`() {
        assertThrows(IllegalArgumentException::class.java) {
            sut.naive(
                a = intArrayOf(0),
                b = intArrayOf(1, 1, 1, 1),
                arraySize = 1,
            )
        }
    }

    @Test
    fun `error on exceeding 15 bits`() {
        assertThrows(IllegalArgumentException::class.java) {
            sut.naive(
                a = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0),
                b = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0),
                arraySize = 16,
            )
        }
    }
}
