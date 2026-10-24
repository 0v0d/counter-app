package com.example.counterapp.repository

import com.example.counterapp.datastore.UserPreference
import com.example.counterapp.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserDataRepository {
    val userConfig: Flow<ThemeConfig>
    suspend fun setThemeConfig(themeConfig: ThemeConfig)
}

class UserDataRepositoryImpl @Inject constructor(private val userPreference: UserPreference) : UserDataRepository {
    override val userConfig: Flow<ThemeConfig> = userPreference.userData

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        userPreference.setThemeConfig(themeConfig)
    }
}
