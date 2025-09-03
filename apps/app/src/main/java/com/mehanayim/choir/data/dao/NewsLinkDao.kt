package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.NewsLink
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsLinkDao {
    @Query("SELECT * FROM newslink WHERE newsId = :newsId")
    fun getLinksByNews(newsId: String): Flow<List<NewsLink>>

    @Query("SELECT * FROM newslink WHERE id = :linkId")
    suspend fun getLinkById(linkId: String): NewsLink?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: NewsLink)

    @Update
    suspend fun updateLink(link: NewsLink)

    @Delete
    suspend fun deleteLink(link: NewsLink)
}
