package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.AppTheme
import kotlinx.coroutines.flow.Flow

@Dao
interface AppThemeDao {
    @Query("SELECT * FROM apptheme WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveTheme(): AppTheme?

    @Query("SELECT * FROM apptheme")
    fun getAllThemes(): Flow<List<AppTheme>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: AppTheme)

    @Update
    suspend fun updateTheme(theme: AppTheme)

    @Delete
    suspend fun deleteTheme(theme: AppTheme)

    @Query("UPDATE apptheme SET isActive = 0")
    suspend fun deactivateAllThemes()

    @Query("UPDATE apptheme SET isActive = 1 WHERE id = :themeId")
    suspend fun activateTheme(themeId: String)
}
