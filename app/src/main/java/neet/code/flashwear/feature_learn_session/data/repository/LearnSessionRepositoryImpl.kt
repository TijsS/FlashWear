package neet.code.flashwear.feature_learn_session.data.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_learn_session.data.data_source.LearnSessionDao
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph

class LearnSessionRepositoryImpl(
    private val dao: LearnSessionDao
    ) : LearnSessionRepository {

    override suspend fun getAllLearnSessions(id: Int): Flow<List<LearnSession>> {
        return dao.getAll()
    }

    override suspend fun startLearnSession(learnSession: LearnSession): Long {
        return dao.insert(learnSession)
    }

    override suspend fun updateLearnSession(learnSession: LearnSession) {
        return dao.update(learnSession)
    }

    override suspend fun deleteLearnSession(learnSession: LearnSession) {
        return dao.delete(learnSession)
    }

    override suspend fun deleteLearnSessionWithDeckId(deckId: Int) {
        return dao.deleteByDeckId(deckId)
    }

    override suspend fun getAvgScoreByDeck(deckId: Long): MutableMap<TimeScaleGraph, List<AvgScoreDTO>>  {
        val map = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()
        map[TimeScaleGraph.Day] = dao.getAvgScoreByDeck(deckId, "%Y-%m-%d")
        map[TimeScaleGraph.Week] = dao.getAvgScoreByDeck(deckId, "%Y-%W")
        map[TimeScaleGraph.Month] = dao.getAvgScoreByDeck(deckId, "%Y-%m")
        return map
    }

    override suspend fun getMinutesLearnedByDeck(deckId: Long): MutableMap<TimeScaleGraph, List<MinutesLearnedDTO>> {
        val map = mutableMapOf<TimeScaleGraph, List<MinutesLearnedDTO>>()
        map[TimeScaleGraph.Day] = dao.getMinutesLearnedByDeck(deckId, "%Y-%m-%d")
        map[TimeScaleGraph.Week] = dao.getMinutesLearnedByDeck(deckId, "%Y-%W")
        map[TimeScaleGraph.Month] = dao.getMinutesLearnedByDeck(deckId, "%Y-%m")
        return map
    }

    override suspend fun getAvgScoreQuestionsByDeck(deckId: Long): MutableMap<TimeScaleGraph, List<AvgScoreDTO>> {
        val map = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()
        map[TimeScaleGraph.Day] = dao.getAvgScoreQuestionsByDeck(deckId, "%Y-%m-%d")
        map[TimeScaleGraph.Week] = dao.getAvgScoreQuestionsByDeck(deckId, "%Y-%W")
        map[TimeScaleGraph.Month] = dao.getAvgScoreQuestionsByDeck(deckId, "%Y-%m")
        return map
    }

    override suspend fun getMinutesLearnedTotal(): MutableMap<TimeScaleGraph, List<MinutesLearnedDTO>> {
        val map = mutableMapOf<TimeScaleGraph, List<MinutesLearnedDTO>>()
        map[TimeScaleGraph.Day] = dao.getMinutesLearnedTotal("%Y-%m-%d")
        map[TimeScaleGraph.Week] = dao.getMinutesLearnedTotal("%Y-%W")
        map[TimeScaleGraph.Month] = dao.getMinutesLearnedTotal("%Y-%m")
        return map
    }
}