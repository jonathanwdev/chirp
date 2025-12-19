package com.plcoding.core.domain.preferences

import kotlinx.coroutines.flow.Flow

interface ThemePreferences {
    fun observeThemePreferences(): Flow<ThemePreference>
    suspend fun updateThemePreferences(theme: ThemePreference)
}

