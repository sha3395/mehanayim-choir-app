package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comment WHERE postId = :postId ORDER BY createdAt ASC")
    fun getCommentsByPost(postId: String): Flow<List<Comment>>

    @Query("SELECT * FROM comment WHERE id = :commentId")
    suspend fun getCommentById(commentId: String): Comment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Update
    suspend fun updateComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)

    @Query("UPDATE comment SET likes = :likes WHERE id = :commentId")
    suspend fun updateCommentLikes(commentId: String, likes: List<String>)
}
