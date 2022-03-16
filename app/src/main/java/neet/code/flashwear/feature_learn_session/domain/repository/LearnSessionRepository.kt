package neet.code.flashwear.feature_learn_session.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession

interface LearnSessionRepository {

    suspend fun getAllLearnSessions(id: Int): Flow<List<LearnSession>>

    suspend fun startLearnSession(learnSession: LearnSession): Long

    suspend fun deleteLearnSession(learnSession: LearnSession)

    suspend fun updateLearnSession(learnSession: LearnSession)
}