package neet.code.wearable.feature_question.domain.use_case


import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.repository.QuestionRepository

class SyncQuestion(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(question: Question){
        //insert question, if already exist Conflict.ignore returns -1. in this case update question
        val result = repository.insertQuestionOnConflictIgnore(question)
        if(result == -1L){
            repository.updateQuestion(question)
        }
    }
}
