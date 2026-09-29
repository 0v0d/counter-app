package com.example.counterapp.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
class CounterViewModelUnitTest {

    private val viewModel = CounterViewModel()

    @Test
    fun 起動時に0を公開する() {
        assertEquals(0, viewModel.counter.value.value)
    }

    @Test
    fun 増減操作が公開状態に反映される() {
        val counter = viewModel.counter

        repeat(3) { viewModel.increment() }
        assertEquals(3, counter.value.value)

        viewModel.decrement()
        assertEquals(2, counter.value.value)
    }

    @Test
    fun リセットで0に戻り繰り返しても0のまま() {
        repeat(5) { viewModel.increment() }

        viewModel.reset()
        assertEquals(0, viewModel.counter.value.value)

        viewModel.reset()
        assertEquals(0, viewModel.counter.value.value)
    }

    @Test
    fun リセット後も0からカウントを再開できる() {
        repeat(5) { viewModel.increment() }
        viewModel.reset()

        viewModel.decrement()
        assertEquals(0, viewModel.counter.value.value)

        viewModel.increment()
        assertEquals(1, viewModel.counter.value.value)
    }
}
