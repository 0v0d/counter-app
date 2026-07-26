package com.example.counterapp.model

data class Counter(val value: Int) {
    fun increment() = Counter((value + 1).coerceIn(VALID_RANGE))
    fun decrement() = Counter((value - 1).coerceIn(VALID_RANGE))

    companion object {
        val VALID_RANGE = 0..1000
        val INITIAL = Counter(0)
    }
}