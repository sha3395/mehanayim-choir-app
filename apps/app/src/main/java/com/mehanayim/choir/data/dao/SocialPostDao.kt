package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.SocialPost
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialPostDao {
    @Query("SELECT * FROM socialpost WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActivePosts(): Flow<List<SocialPost>>

    @Query("SELECT * FROM socialpost WHERE userId = :userId AND isActive = 1 ORDER BY createdAt DESC")
    fun getPostsByUser(userId: String): Flow<List<SocialPost>>

    @Query("SELECT * FROM socialpost WHERE id = :postId")
    suspend fun getPostById(postId: String): SocialPost?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: SocialPost)

    @Update
    suspend fun updatePost(post: SocialPost)

    @Delete
    suspend fun deletePost(post: SocialPost)

    @Query("UPDATE socialpost SET isActive = :isActive WHERE id = :postId")
    suspend fun updatePostStatus(postId: String, isActive: Boolean)

    @Query("UPDATE socialpost SET likes = :likes WHERE id = :postId")
    suspend fun updatePostLikes(postId: String, likes: List<String>)
}
