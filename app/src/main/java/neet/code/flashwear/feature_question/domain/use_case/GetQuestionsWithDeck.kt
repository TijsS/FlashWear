package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_settings.domain.model.LearnStyle
import neet.code.flashwear.feature_settings.domain.repository.SettingsRepository


class GetQuestionsWithDeck(
    private val repository: QuestionRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(deckId: Int, givenLearnStyle: LearnStyle? = null): List<Question> {
        val settings = settingsRepository.getSettings().first()

        return when (givenLearnStyle ?: settings.learnStyle){
            LearnStyle.Revise -> {
                repository.getOldQuestionsByDeck(deckId).shuffled()
            }
            LearnStyle.Random -> {
                repository.getQuestionsByDeck(deckId).shuffled()
            }
            LearnStyle.New -> {
                repository.getNewQuestionsByDeck(deckId).shuffled()
            }
        }
    }
}
