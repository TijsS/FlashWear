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
package neet.code.wearable

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import neet.code.wearable.feature_deck.domain.Deck
import neet.code.wearable.feature_deck.domain.use_case.DecksUseCases
import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.use_case.QuestionsUseCases
import java.io.InputStream
import java.lang.reflect.Type
import javax.inject.Inject


@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {
    private val dataClient by lazy { Wearable.getDataClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Inject lateinit var decksUseCases: DecksUseCases
    @Inject lateinit var questionsUseCases: QuestionsUseCases


    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri
            Log.i(TAG, "onDataChanged for: ${uri.path}")
            when (uri.path) {
                DECKS_PATH -> {

                    val decksString = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getString(DECKS_KEY)

                    val hardSync = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getBoolean(HARD_SYNC_KEY)

                    Log.i(ContentValues.TAG, "decks received: $decksString")

                    //convert String to decks
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val type: Type = Types.newParameterizedType(
                        List::class.java,
                        Deck::class.java
                    )
                    val jsonAdapter: JsonAdapter<List<Deck>> = moshi.adapter(type)

                    if (!decksString.isNullOrEmpty()){
                        val decks = jsonAdapter.fromJson(decksString)
                        if (decks != null) {
                            scope.launch {
                                decksUseCases.syncDecks(decks, hardSync)
                            }
                        }
                    }
                }

                QUESTION_PATH -> {

                    val questionString = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getString(QUESTION_KEY)
                    Log.i(ContentValues.TAG, "question received: $questionString")

                    //convert String to decks
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val jsonAdapter: JsonAdapter<Question> = moshi.adapter(Question::class.java)

                    if (!questionString.isNullOrEmpty()){
                        val question = jsonAdapter.fromJson(questionString)
                        if (question != null) {
                            scope.launch {
                                questionsUseCases.syncQuestion(question)
                            }
                        }
                    }
                }

                DELETE_DECK_PATH -> {
                    val deckId = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getInt(DECK_ID_KEY)

                    scope.launch {
                        decksUseCases.deleteDeck(deckId)
                    }
                }

                DELETE_QUESTION_PATH -> {
                    val questionId = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getInt(QUESTION_ID_KEY)

                    scope.launch {
                        questionsUseCases.deleteQuestion(questionId)
                    }
                }

                QUESTIONS_PATH -> {
                    try {
                        val questionsString: String

                        questionsString = DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap
                                .getAsset(QUESTIONS_KEY)
                                .let {asset -> loadAsset( asset )}

                        val hardSync = DataMapItem.fromDataItem(dataEvent.dataItem)
                            .dataMap
                            .getBoolean(HARD_SYNC_KEY)

                        //convert String to decks
                        val moshi = Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()

                        val type: Type = Types.newParameterizedType(
                            List::class.java,
                            Question::class.java
                        )

                        val jsonAdapter: JsonAdapter<List<Question>> = moshi.adapter(type)
                        if (questionsString.isNotEmpty()){
                            val questions = jsonAdapter.fromJson(questionsString)
                            if (questions != null) {
                                scope.launch {
                                    questionsUseCases.syncQuestions(questions, hardSync)
                                }
                            } else{
                                Log.i(ContentValues.TAG, "questions received does not contain questions")
                                return
                            }
                        }
                        else{
                            Log.i(ContentValues.TAG, "questions received string is empty")
                            return
                        }
                    }
                    catch (exception: Exception) {
                        Log.i(TAG, "problems with reception of questions: $exception")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun loadAsset(asset: Asset?): String {

        val assetInputStream: InputStream? =
            Tasks.await(dataClient.getFdForAsset(asset!!))
                ?.inputStream


        if (assetInputStream != null) {
            return assetInputStream.readBytes().toString(Charsets.UTF_8)
        }
        return ""
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
