package algos.hashtable

// from grocking book, chapter 5 p.64
// optional fun experiment, ignoring uppercase though
fun main() {
    println(listOf("Esther", "Ben", "Bob", "Dan").map { hashBasedOnPrimes(it, 10) })
    println(listOf("A", "AA", "AAA", "AAAA").map { hashBasedOnPrimes(it, 10) })
    println(listOf("Maus", "Fun House", "Watchmen").map { hashBasedOnPrimes(it, 10) })
}

private fun hashBasedOnPrimes(input: String, slotsAmount: Int) =
    input.lowercase().sumOf { alphabetToPrimes[it] ?: 0 } % slotsAmount

// a super naive extremely inefficient approach (though appropriate for the task)
private fun generatePrimes(amount: Int): IntArray {
    var primeCount = 0
    var currentNumber = 2
    val primes = IntArray(size = amount)
    while (primeCount < amount) {
        val allItemsBeforeNum = 2 until currentNumber
        val isPrime = allItemsBeforeNum.none { currentNumber % it == 0 }
        if (isPrime) primes[primeCount++] = currentNumber
        currentNumber++
    }
    return primes
}

private val alphabetToPrimes: Map<Char, Int> by lazy {
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    val primes = generatePrimes(alphabet.length)
    (alphabet.indices).associateBy(
        keySelector = { alphabet[it] },
        valueTransform = { primes[it] },
    )
}
