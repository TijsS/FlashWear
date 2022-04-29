package neet.code.wearable.feature_question.domain.use_case

import neet.code.wearable.feature_deck.presentation.LearnStyle
import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.repository.QuestionRepository

class GetQuestionsWithDeck (
    private val repository: QuestionRepository,
    ) {

        suspend operator fun invoke(deckId: Int, learnStyle: LearnStyle): List<Question> {
            when (learnStyle){
                LearnStyle.Revise -> {
                    return repository.getOldQuestionsByDeck(deckId).shuffled()
                }
                LearnStyle.Random -> {
                    return repository.getQuestionsByDeck(deckId).shuffled()
                }
                LearnStyle.New -> {
                    return repository.getNewQuestionsByDeck(deckId).shuffled()
                }
            }
        }
    }
