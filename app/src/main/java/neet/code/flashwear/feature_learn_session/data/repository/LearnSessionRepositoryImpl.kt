package neet.code.flashwear.feature_learn_session.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.data.data_source.DeckDao
import neet.code.flashwear.feature_deck.domain.model.Deck

import neet.code.flashwear.feature_learn_session.data.data_source.LearnSessionDao
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository

class LearnSessionRepositoryImpl(
    private val dao: LearnSessionDao
    ) : LearnSessionRepository {


    override suspend fun getAllLearnSessions(id: Int): Flow<List<LearnSession>> {
        return dao.getAll()
    }

    override suspend fun startLearnSession(learnSession: LearnSession): Long {
        return dao.insertLearnSession(learnSession)
    }

    override suspend fun updateLearnSession(learnSession: LearnSession) {
        return dao.update(learnSession)
    }

    override suspend fun deleteLearnSession(learnSession: LearnSession) {
        return dao.delete(learnSession)
    }
}