package neet.code.flashwear.feature_wearable

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.wearable.Asset
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.service.DataLayerListenerService
import neet.code.flashwear.service.DataLayerListenerService.Companion.HARD_SYNC_KEY
import neet.code.flashwear.service.DataLayerListenerService.Companion.QUESTIONS_KEY
import neet.code.flashwear.service.DataLayerListenerService.Companion.QUESTIONS_PATH
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import javax.inject.Inject


@AndroidEntryPoint
class SyncQuestions @Inject constructor(
    private val context: Context
) : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(context) }

    /*
    questions: questions that will get send to the wearable
    hardsync:   if true, all questions related to this deck will first be deleted.
                if false, questions will only be updated and inserted
     */
    suspend operator fun invoke(questions: List<Question>, hardSync: Boolean){
        Log.d(TAG, "syncQuestions invoke")
        try {
            //convert question to string
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val type: Type = Types.newParameterizedType(
                List::class.java,
                Question::class.java
            )
            
            val jsonAdapter: JsonAdapter<List<Question>> = moshi.adapter(type)
            val questionsByteArray: ByteArray = jsonAdapter.toJson(questions).toByteArray(Charsets.UTF_8)
            val questionsAsset = Asset.createFromBytes(questionsByteArray)

            val request = PutDataMapRequest.create(QUESTIONS_PATH).apply {
                dataMap.putAsset(QUESTIONS_KEY, questionsAsset)
                dataMap.putBoolean(HARD_SYNC_KEY, hardSync)
                dataMap.putLong("when", System.currentTimeMillis() )
            }
                .asPutDataRequest()
                .setUrgent()

            val result = dataClient.putDataItem(request).await()
            Log.d(TAG, "DataItem saved: $result")
            return

        } catch (exception: Exception) {
            Log.d(TAG, "Saving DataItem failed: $exception")
        }
    }
}
