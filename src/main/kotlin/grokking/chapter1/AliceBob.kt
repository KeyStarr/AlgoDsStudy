package grokking.chapter1

// https://leetcode.com/problems/fair-candy-swap/description/
fun fairCandySwap(aliceSizes: IntArray, bobSizes: IntArray): IntArray {
    // aliceTotal = Alice - calculate total candies
    // bobTotal = Bob - calculate total candies
    //
    val aliceTotal = aliceSizes.sum()
    val bobTotal = bobSizes.sum()
    for (aliceBoxCandiesCount in aliceSizes) {
        val aliceRaw = aliceTotal - aliceBoxCandiesCount
        for (bobBoxCandiesCount in bobSizes) {
            val bobRaw = bobTotal - bobBoxCandiesCount
            if (aliceRaw + bobBoxCandiesCount == bobRaw + aliceBoxCandiesCount) {
                return intArrayOf(aliceBoxCandiesCount, bobBoxCandiesCount)
            }
        }
    }
    throw IllegalStateException("there's a bug smwh, the answer is guaranteed to exist")
}
