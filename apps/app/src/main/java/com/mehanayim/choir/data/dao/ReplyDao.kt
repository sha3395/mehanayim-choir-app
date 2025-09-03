package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.Reply
import kotlinx.coroutines.flow.Flow

@Dao
interface ReplyDao {
    @Query("SELECT * FROM reply WHERE commentId = :commentId ORDER BY createdAt ASC")
    fun getRepliesByComment(commentId: String): Flow<List<Reply>>

    @Query("SELECT * FROM reply WHERE id = :replyId")
    suspend fun getReplyById(replyId: String): Reply?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReply(reply: Reply)

    @Update
    suspend fun updateReply(reply: Reply)

    @Delete
    suspend fun deleteReply(reply: Reply)

    @Query("UPDATE reply SET likes = :likes WHERE id = :replyId")
    suspend fun updateReplyLikes(replyId: String, likes: List<String>)
}
