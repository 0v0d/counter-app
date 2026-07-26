package com.example.counterapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.counterapp.model.Counter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor() : ViewModel() {
    private var _counter = MutableStateFlow(Counter.INITIAL)
    val counter = _counter.asStateFlow()

    fun increment() = _counter.update { it.increment() }

    fun decrement() = _counter.update { it.decrement() }
    fun reset() {
        _counter.value = Counter.INITIAL
    }
}
