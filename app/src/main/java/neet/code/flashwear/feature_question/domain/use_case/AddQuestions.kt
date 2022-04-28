package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.delay
import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class AddQuestions(
    private val repository: QuestionRepository,
    private val wearableUseCases: WearableUseCases
) {
    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(questions: List<Question>) {
        //save questions in db, get list of question id's returned, get list of questions with those id's, sync questions with wearable
        val questionIds = repository.insertQuestions(questions)
        val questionsWithId = repository.getQuestionsFromIdList(questionIds)
        wearableUseCases.syncQuestions(questions = questionsWithId, hardSync = false)
    }
}