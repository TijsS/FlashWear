package neet.code.wearable

sealed class Screen(val route: String) {
    object DecksScreen: Screen("decks_screen")
    object QuestionScreen: Screen("question_screen")
}