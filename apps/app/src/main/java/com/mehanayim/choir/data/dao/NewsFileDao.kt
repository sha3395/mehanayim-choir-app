package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.NewsFile
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsFileDao {
    @Query("SELECT * FROM newsfile WHERE newsId = :newsId")
    fun getFilesByNews(newsId: String): Flow<List<NewsFile>>

    @Query("SELECT * FROM newsfile WHERE id = :fileId")
    suspend fun getFileById(fileId: String): NewsFile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: NewsFile)

    @Update
    suspend fun updateFile(file: NewsFile)

    @Delete
    suspend fun deleteFile(file: NewsFile)
}
