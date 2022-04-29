package neet.code.flashwear.feature_settings.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.core.data.data_source.BaseDao
import neet.code.flashwear.feature_settings.domain.model.Settings

@Dao
interface SettingsDao : BaseDao<Settings> {

    @Transaction
    @Query("SELECT * FROM settings")
    fun getSettings(): Flow<Settings>
}