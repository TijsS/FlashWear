package neet.code.flashwear.feature_settings.presentation

import androidx.compose.ui.focus.FocusState
import neet.code.flashwear.feature_settings.domain.model.LearnStyle


sealed class SettingsEvent {
    data class ChangeLearnStyle(val learnStyle: LearnStyle): SettingsEvent()
}