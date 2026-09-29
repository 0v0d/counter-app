package com.example.counterapp.model

import kotlinx.serialization.Serializable

@Serializable
enum class ThemeConfig {
    System,
    Light,
    Dark;

    companion object {
        val DEFAULT = System
    }
}
