package neet.code.flashwear.feature_question.domain.use_case

import neet.code.flashwear.R
import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class UpdateQuestion(
    private val repository: QuestionRepository,
    private val wearableUseCases: WearableUseCases
) {
    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(question: Question) {
        if(question.score > 1.0){
            question.score = 1.0
        }
        if(question.score < 0.1){
            question.score = 0.1
        }

        val answerTitle = question.answerTitle
        if(answerTitle != null){
            if(answerTitle.length > 200){
                throw InvalidQuestionException (
                    message = answerTitle.toString(),
                    baseMessage = R.string.question_text_long
                )
            }
            if (answerTitle.lines().size > 3){
                throw InvalidQuestionException (
                    message = answerTitle.toString(),
                    baseMessage = R.string.question_lines_long
                )
            }
        }

        val answerContent = question.answerContent
        if(answerContent != null){
            if (answerContent.length > 200) {
                throw InvalidQuestionException(
                    message = answerContent.toString(),
                    baseMessage = R.string.question_text_long
                )
            }
            if (answerContent.lines().size > 3) {
                throw InvalidQuestionException(
                    message = answerContent.toString(),
                    baseMessage = R.string.question_lines_long
                )
            }
        }

        val answerSub = question.answerSub
        if(answerSub != null){
            if (answerSub.length > 100) {
                throw InvalidQuestionException(
                    message = answerSub.toString(),
                    baseMessage = R.string.question_text_long_sub
                )
            }
            if (answerSub.lines().size > 2) {
                throw InvalidQuestionException(
                    message = answerSub.toString(),
                    baseMessage = R.string.question_lines_long
                )
            }
        }

        val questionTitle = question.questionTitle
        if(questionTitle != null){
            if (questionTitle.length > 200) {
                throw InvalidQuestionException(
                    message = questionTitle.toString(),
                    baseMessage = R.string.question_text_long
                )
            }
            if (questionTitle.lines().size > 3) {
                throw InvalidQuestionException(
                    message = questionTitle.toString(),
                    baseMessage = R.string.question_lines_long
                )
            }

        }

        val questionContent = question.questionContent
        if(questionContent != null){
            if (questionContent.length > 200) {
                throw InvalidQuestionException(
                    message = questionContent.toString(),
                    baseMessage = R.string.question_text_long
                )
            }
            if (questionContent.lines().size > 3) {
                throw InvalidQuestionException(
                    message = questionContent.toString(),
                    baseMessage = R.string.question_lines_long
                )
            }
        }


        repository.updateQuestion(question)
        wearableUseCases.syncQuestion(question)
    }
}