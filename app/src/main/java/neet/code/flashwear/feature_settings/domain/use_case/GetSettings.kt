package neet.code.flashwear.feature_settings.domain.use_case

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_settings.domain.model.Settings
import neet.code.flashwear.feature_settings.domain.repository.SettingsRepository

class GetSettings(
    private val repository: SettingsRepository
) {

    operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}
