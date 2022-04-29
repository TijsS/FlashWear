package neet.code.flashwear.feature_settings.presentation

import neet.code.flashwear.feature_settings.domain.model.LearnStyle
import neet.code.flashwear.feature_settings.domain.model.Settings

data class SettingsState (
    val settings: Settings,
    val currentLearnStyle: LearnStyle
    )