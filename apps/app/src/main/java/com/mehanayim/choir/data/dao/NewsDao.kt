package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE isPublished = 1 AND isActive = 1 ORDER BY publishedAt DESC")
    fun getAllPublishedNews(): Flow<List<News>>

    @Query("SELECT * FROM news WHERE category = :category AND isPublished = 1 AND isActive = 1 ORDER BY publishedAt DESC")
    fun getNewsByCategory(category: String): Flow<List<News>>

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: String): News?

    @Query("SELECT * FROM news ORDER BY createdAt DESC")
    fun getAllNews(): Flow<List<News>>

    @Query("SELECT * FROM news WHERE isPublished = 0 ORDER BY createdAt DESC")
    fun getDraftNews(): Flow<List<News>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: News)

    @Update
    suspend fun updateNews(news: News)

    @Delete
    suspend fun deleteNews(news: News)

    @Query("UPDATE news SET isPublished = :isPublished WHERE id = :newsId")
    suspend fun updateNewsPublishStatus(newsId: String, isPublished: Boolean)

    @Query("UPDATE news SET isActive = :isActive WHERE id = :newsId")
    suspend fun updateNewsStatus(newsId: String, isActive: Boolean)
}
