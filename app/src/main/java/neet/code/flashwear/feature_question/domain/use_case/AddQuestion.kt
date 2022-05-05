package neet.code.flashwear.feature_question.domain.use_case

import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases


class AddQuestion(
    private val repository: QuestionRepository,
    private val wearableUseCases: WearableUseCases
) {
    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(question: Question) {

        if(question.answerTitle != null){
            if(question.answerTitle.isNullOrBlank()){
                question.answerTitle = null
            }else{
                if(question.answerTitle?.length!! > 200){
                    throw InvalidQuestionException ("too long, max 200 chars, ${question.answerTitle}")
                }
                if (question.answerTitle?.lines()?.size!! > 3){
                    throw InvalidQuestionException ("too many lines, max 3 lines, ${question.answerTitle}")
                }
            }
        }

        if(question.answerContent != null){
            if(question.answerTitle.isNullOrBlank()){
                question.answerTitle = null
            }else {
                if (question.answerContent?.length!! > 200) {
                    throw InvalidQuestionException("too long, max 200 chars, ${question.answerContent}")
                }
                if (question.answerContent?.lines()?.size!! > 3) {
                    throw InvalidQuestionException("too many lines, max 3 lines, ${question.answerContent}")
                }
            }
        }

        if(question.answerSub != null){
            if(question.answerTitle.isNullOrBlank()){
                question.answerTitle = null
            }else {
                if (question.answerSub?.length!! > 100) {
                    throw InvalidQuestionException("too long, max 100 chars, ${question.answerSub}")
                }
                if (question.answerSub?.lines()?.size!! > 2) {
                    throw InvalidQuestionException("too many lines, max 2 lines, ${question.answerSub}")
                }
            }
        }

        if(question.questionTitle != null){
            if(question.answerTitle.isNullOrBlank()){
                question.answerTitle = null
            }else {
                if (question.questionTitle?.length!! > 200) {
                    throw InvalidQuestionException("too long, max 200 chars, ${question.questionTitle}")
                }
                if (question.questionTitle?.lines()?.size!! > 3) {
                    throw InvalidQuestionException("too many lines, max 3 lines, ${question.questionTitle}")
                }
            }
        }

        if(question.questionContent != null){
            if(question.answerTitle.isNullOrBlank()){
                question.answerTitle = null
            }else {
                if (question.questionContent?.length!! > 200) {
                    throw InvalidQuestionException("too long, max 200 chars, ${question.questionContent}")
                }
                if (question.questionContent?.lines()?.size!! > 3) {
                    throw InvalidQuestionException("too many lines, max 3 lines, ${question.questionContent}")
                }
            }
        }

        repository.insertQuestion(question)
        wearableUseCases.syncQuestion(question)
    }
}