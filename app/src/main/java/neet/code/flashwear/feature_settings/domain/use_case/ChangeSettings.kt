package neet.code.flashwear.feature_settings.domain.use_case

import android.content.ContentValues
import android.util.Log
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_settings.domain.model.Settings
import neet.code.flashwear.feature_settings.domain.repository.SettingsRepository

class ChangeSettings (
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(settings: Settings){
        repository.changeSettings(settings)
    }
}
