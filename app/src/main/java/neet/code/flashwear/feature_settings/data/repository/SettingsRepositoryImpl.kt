package neet.code.flashwear.feature_settings.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow

import neet.code.flashwear.feature_settings.data.data_source.SettingsDao
import neet.code.flashwear.feature_settings.domain.model.Settings
import neet.code.flashwear.feature_settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val dao: SettingsDao
) : SettingsRepository {


    override fun getSettings(): Flow<Settings> {
        return dao.getSettings()
    }

    override suspend fun changeSettings(settings: Settings) {
        dao.update(settings)
    }

    override suspend fun insertSettings(settings: Settings){
        dao.insert(settings)
    }
}