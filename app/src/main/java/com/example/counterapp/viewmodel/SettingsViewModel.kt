package com.example.counterapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counterapp.model.ThemeConfig
import com.example.counterapp.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val userDataRepository: UserDataRepository) : ViewModel() {
    private val _userConfig = MutableStateFlow(ThemeConfig.DEFAULT)
    val userConfig: StateFlow<ThemeConfig> = _userConfig.asStateFlow()

    private val _initialLoadCompleted = MutableStateFlow(false)
    val initialLoadCompleted = _initialLoadCompleted.asStateFlow()

    init {
        viewModelScope.launch {
            userDataRepository.userConfig.collect { userConfig ->
                _userConfig.value = userConfig
                _initialLoadCompleted.value = false
            }
        }
    }

    fun setThemeConfig(themeConfig: ThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setThemeConfig(themeConfig)
        }
    }
}
