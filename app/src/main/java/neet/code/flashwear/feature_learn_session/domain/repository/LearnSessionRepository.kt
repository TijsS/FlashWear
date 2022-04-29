package neet.code.flashwear.feature_learn_session.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession

interface LearnSessionRepository {

    suspend fun getAllLearnSessions(id: Int): Flow<List<LearnSession>>

    suspend fun startLearnSession(learnSession: LearnSession): Long

    suspend fun deleteLearnSession(learnSession: LearnSession)

    suspend fun updateLearnSession(learnSession: LearnSession)

    suspend fun getAvgScoreByDeck(deckId: Long): MutableMap<TimeScaleGraph, List<AvgScoreDTO>>

    suspend fun getAvgScoreQuestionsByDeck(deckId: Long): MutableMap<TimeScaleGraph, List<AvgScoreDTO>>

    suspend fun getMinutesLearnedByDeck(deckId: Long): MutableMap<TimeScaleGraph, List<MinutesLearnedDTO>>
}