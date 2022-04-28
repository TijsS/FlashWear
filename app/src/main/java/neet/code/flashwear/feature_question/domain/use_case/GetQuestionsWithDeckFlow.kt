package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow

import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository


class GetQuestionsWithDeckFlow(
    private val repository: QuestionRepository
) {

    operator fun invoke(deckId: Int): Flow<List<Question>> {
        return repository.getQuestionsByDeckFlow(deckId)
    }
}
