package neet.code.flashwear.feature_settings.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_settings.domain.model.Settings

interface SettingsRepository {
    fun getSettings(): Flow<Settings>

    suspend fun changeSettings(settings: Settings)

    suspend fun insertSettings(settings: Settings)

}