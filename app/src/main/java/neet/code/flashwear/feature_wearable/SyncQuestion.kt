package neet.code.flashwear.feature_wearable

import android.content.ContentValues
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
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.service.DataLayerListenerService
import neet.code.flashwear.service.DataLayerListenerService.Companion.QUESTION_KEY
import java.io.ByteArrayOutputStream
import javax.inject.Inject



@AndroidEntryPoint
class SyncQuestion @Inject constructor(
    private val context: Context
) : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    suspend operator fun invoke(question: Question){
        Log.d(ContentValues.TAG, "syncQuestion invoke")
        try {
            //convert question to string
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val jsonAdapter: JsonAdapter<Question> = moshi.adapter(Question::class.java)

            val request = PutDataMapRequest.create(DataLayerListenerService.QUESTION_PATH).apply {
                dataMap.putString(QUESTION_KEY, jsonAdapter.toJson(question))
                dataMap.putLong("when", System.currentTimeMillis() )
            }
                .asPutDataRequest()
                .setUrgent()

            val result = dataClient.putDataItem(request).await()
            Log.d(ContentValues.TAG, "DataItem saved: $result")
            return

        } catch (exception: Exception) {
            Log.d(ContentValues.TAG, "Saving DataItem failed: $exception")
        }
    }
}