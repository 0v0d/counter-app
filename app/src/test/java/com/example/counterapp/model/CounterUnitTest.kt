package com.example.counterapp.model

import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class CounterUnitTest {

    @Test
    fun 初期値は0() {
        assertEquals(0, Counter.INITIAL.value)
    }

    @Test
    fun カウントアップで1増える() {
        assertEquals(43, Counter(42).increment().value)
    }

    @Test
    fun カウントダウンで1減る() {
        assertEquals(41, Counter(42).decrement().value)
    }

    @Test
    fun カウントダウンしても0より小さくならない() {
        assertEquals(0, Counter(0).decrement().value)
    }

    @Test
    fun カウントアップしても1000より大きくならない() {
        assertEquals(1000, Counter(1000).increment().value)
    }

    @Test
    fun 上限と下限に到達した後も逆方向に操作できる() {
        val maximum = Counter(999).increment()
        val minimum = Counter(1).decrement()

        assertEquals(1000, maximum.value)
        assertEquals(999, maximum.decrement().value)
        assertEquals(0, minimum.value)
        assertEquals(1, minimum.increment().value)
    }
}
