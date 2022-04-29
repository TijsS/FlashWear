package neet.code.wearable.feature_question.domain.use_case


import android.util.Log
import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.repository.QuestionRepository

class SyncQuestions(
    private val repository: QuestionRepository,
) {
    suspend operator fun invoke(questions: List<Question>, hardSync: Boolean){
        //update question, if it does not exist yet, insert

        if(hardSync){
            questions.first().deckId?.let { repository.deleteQuestionsWithDeck(it) }
        }

        repository.updateQuestionBatch(questions)
    }
}
