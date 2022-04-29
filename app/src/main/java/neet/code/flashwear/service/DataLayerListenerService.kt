/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neet.code.flashwear.service

import android.util.Log
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_wearable.WearableUseCases
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Inject lateinit var decksUseCases: DecksUseCases
    @Inject lateinit var wearableUseCases: WearableUseCases
    @Inject lateinit var questionsUseCases: QuestionsUseCases


    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri

            Log.i(TAG, "onDataChanged: we recieved a data change ${uri.path}")

            when (uri.path) {
                QUESTION_RESULT_PATH -> {
                    val questionResult = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getString(QUESTION_RESULT_KEY)

                    val questionId = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getInt(QUESTION_ID_KEY)

                    Log.i(TAG, "onDataChanged: questionresult with questionid of: ${questionId} and result of $questionResult")

                    if (!questionResult.isNullOrEmpty() && questionId != 0){
                        scope.launch {
                            questionsUseCases.updateWearableScore(questionResult, questionId)
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


    companion object {
        private const val TAG = "DataLayerService"

        const val DECKS_PATH = "/decks"
        const val DECKS_KEY = "decks"

        const val DELETE_DECK_PATH = "/delete-deck"
        const val DECK_ID_KEY = "deck-id"

        const val QUESTIONS_PATH = "/questions"
        const val QUESTIONS_KEY = "questions"
        const val HARD_SYNC_KEY = "hardSync"

        const val QUESTION_PATH = "/question"
        const val QUESTION_KEY = "question"

        const val DELETE_QUESTION_PATH = "/delete-question"

        const val QUESTION_RESULT_PATH = "/question-result"
        const val QUESTION_RESULT_KEY = "question-result"
        const val QUESTION_ID_KEY = "question-id"
    }
}
