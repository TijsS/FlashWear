package neet.code.flashwear.feature_deck.domain.use_case

import dagger.hilt.android.components.ActivityComponent
import neet.code.flashwear.feature_deck.domain.model.WearableDeckDTO
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository

class GetDecksForWearable(
    private val repository: DeckRepository
): ActivityComponent {

    suspend operator fun invoke(): List<WearableDeckDTO> {
        return repository.getDecksForWearableByLastPlayed()
    }
}
