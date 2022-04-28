package neet.code.flashwear.feature_wearable


import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import neet.code.flashwear.service.DataLayerListenerService
import javax.inject.Inject

@AndroidEntryPoint
class DeleteQuestionOnWearable @Inject constructor(
    private val context: Context
) : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    suspend operator fun invoke(questionId: Int){
        Log.d(ContentValues.TAG, "syncQuestion invoke")
        try {
            val request = PutDataMapRequest.create(DataLayerListenerService.DELETE_QUESTION_PATH).apply {
                dataMap.putInt(DataLayerListenerService.QUESTION_ID_KEY, questionId)
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
