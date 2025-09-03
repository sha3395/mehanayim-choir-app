package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.Music
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("SELECT * FROM music WHERE isActive = 1 ORDER BY uploadedAt DESC")
    fun getAllActiveMusic(): Flow<List<Music>>

    @Query("SELECT * FROM music WHERE category = :category AND isActive = 1 ORDER BY uploadedAt DESC")
    fun getMusicByCategory(category: String): Flow<List<Music>>

    @Query("SELECT * FROM music WHERE month = :month AND year = :year AND isActive = 1 ORDER BY uploadedAt DESC")
    fun getMusicByMonth(month: String, year: Int): Flow<List<Music>>

    @Query("SELECT * FROM music WHERE id = :musicId")
    suspend fun getMusicById(musicId: String): Music?

    @Query("SELECT DISTINCT category FROM music WHERE isActive = 1")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT DISTINCT month FROM music WHERE year = :year AND isActive = 1")
    fun getMonthsByYear(year: Int): Flow<List<String>>

    @Query("SELECT DISTINCT year FROM music WHERE isActive = 1 ORDER BY year DESC")
    fun getAllYears(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: Music)

    @Update
    suspend fun updateMusic(music: Music)

    @Delete
    suspend fun deleteMusic(music: Music)

    @Query("UPDATE music SET isActive = :isActive WHERE id = :musicId")
    suspend fun updateMusicStatus(musicId: String, isActive: Boolean)
}
