package neet.code.wearable.feature_question.domain.use_case

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import neet.code.wearable.DataLayerListenerService
import neet.code.wearable.DataLayerListenerService.Companion.QUESTION_ID_KEY
import neet.code.wearable.DataLayerListenerService.Companion.QUESTION_RESULT_KEY

import javax.inject.Inject


@AndroidEntryPoint
class SendQuestionResult @Inject constructor(
    private val context: Context
) : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    suspend operator fun invoke(result: String, questionId: Int){
        Log.d(ContentValues.TAG, "syncQuestion invoke")
        try {
            val request = PutDataMapRequest.create(DataLayerListenerService.QUESTION_RESULT_PATH).apply {
                dataMap.putString(QUESTION_RESULT_KEY, result)
                dataMap.putInt(QUESTION_ID_KEY, questionId)
                dataMap.putLong("when", System.currentTimeMillis() )
            }
                .asPutDataRequest()
                .setUrgent()

            val putResult = dataClient.putDataItem(request).await()
            Log.d(ContentValues.TAG, "DataItem saved: $putResult")
            return

        } catch (exception: Exception) {
            Log.d(ContentValues.TAG, "Saving DataItem failed: $exception")
        }
    }
}