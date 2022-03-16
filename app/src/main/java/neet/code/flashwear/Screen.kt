package neet.code.flashwear

sealed class Screen(val route: String) {
    object DecksScreen: Screen("decks_screen")
    object AddDeckScreen: Screen("add_deck_screen")
    object ViewDeckScreen: Screen("view_deck_screen")

    object AddQuestionScreen: Screen("add_question_screen")

    object LearnSessionScreen: Screen("learn_session_screen")

    object ProgressScreen: Screen("progress_screen")
    object SettingsScreen: Screen("settings_screen")
}