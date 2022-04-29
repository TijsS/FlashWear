package neet.code.flashwear.feature_wearable

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import neet.code.flashwear.feature_deck.domain.model.WearableDeckDTO
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.service.DataLayerListenerService
import java.lang.reflect.Type
import javax.inject.Inject

@AndroidEntryPoint
class SyncDecks @Inject constructor(
    private val deckRepository: DeckRepository,
    private val context: Context
    ) : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(context) }

    /*
    hardSync is false, only insert and update is done
    hardSync is true, also Delete decks
     */
    suspend operator fun invoke(hardSync: Boolean){
        Log.d(TAG, "syncdeck invoke")
        try {
            //convert decks to string
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val type: Type = Types.newParameterizedType(
                List::class.java,
                WearableDeckDTO::class.java
            )
            val jsonAdapter: JsonAdapter<List<WearableDeckDTO>> = moshi.adapter(type)

            val decks = deckRepository.getDecksForWearableByLastPlayed()

            val request = PutDataMapRequest.create(DataLayerListenerService.DECKS_PATH).apply {
                dataMap.putString(DataLayerListenerService.DECKS_KEY, jsonAdapter.toJson(decks))
                dataMap.putBoolean(DataLayerListenerService.HARD_SYNC_KEY, hardSync)
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
