package com.keystarr.algorithm.divideandconquer

// chapter 3 grocking algos
class SimpleFactorialFromGrockingBook {

    // formatting - for debug
    fun recursive(number: Int): Int =
        if (number > 0) {
            val intermediateResult = recursive(number - 1)
            intermediateResult * number
        } else 1
}

fun main() {
    println(SimpleFactorialFromGrockingBook().recursive(12))
}
