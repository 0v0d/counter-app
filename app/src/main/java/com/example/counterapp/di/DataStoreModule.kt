package com.example.counterapp.di
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.counterapp.datastore.UserPreference
import com.example.counterapp.repository.UserDataRepository
import com.example.counterapp.repository.UserDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_config")

    @Provides
    fun provideDataStore(
        @ApplicationContext
        context: Context,
    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference = UserPreference(dataStore)

    @Provides
    @Singleton
    fun provideUserDataRepository(userPreference: UserPreference): UserDataRepository =
        UserDataRepositoryImpl(userPreference)
}
