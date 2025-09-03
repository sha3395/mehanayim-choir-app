package com.mehanayim.choir.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mehanayim.choir.data.dao.SocialPostDao
import com.mehanayim.choir.data.dao.CommentDao
import com.mehanayim.choir.data.dao.ReplyDao
import com.mehanayim.choir.data.model.SocialPost
import com.mehanayim.choir.data.model.Comment
import com.mehanayim.choir.data.model.Reply
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRepository @Inject constructor(
    private val socialPostDao: SocialPostDao,
    private val commentDao: CommentDao,
    private val replyDao: ReplyDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    
    fun getAllPosts(): Flow<List<SocialPost>> = socialPostDao.getAllActivePosts()
    
    fun getPostsByUser(userId: String): Flow<List<SocialPost>> = socialPostDao.getPostsByUser(userId)
    
    suspend fun getPostById(postId: String): SocialPost? = socialPostDao.getPostById(postId)
    
    fun getCommentsByPost(postId: String): Flow<List<Comment>> = commentDao.getCommentsByPost(postId)
    
    fun getRepliesByComment(commentId: String): Flow<List<Reply>> = replyDao.getRepliesByComment(commentId)
    
    suspend fun createPost(post: SocialPost): Result<Unit> {
        return try {
            firestore.collection("social_posts").document(post.id).set(post).await()
            socialPostDao.insertPost(post)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updatePost(post: SocialPost): Result<Unit> {
        return try {
            firestore.collection("social_posts").document(post.id).set(post).await()
            socialPostDao.updatePost(post)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deletePost(postId: String): Result<Unit> {
        return try {
            firestore.collection("social_posts").document(postId).delete().await()
            val post = socialPostDao.getPostById(postId)
            if (post != null) {
                socialPostDao.deletePost(post)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun likePost(postId: String, userId: String): Result<Unit> {
        return try {
            val post = socialPostDao.getPostById(postId)
            if (post != null) {
                val updatedLikes = if (post.likes.contains(userId)) {
                    post.likes - userId
                } else {
                    post.likes + userId
                }
                val updatedPost = post.copy(likes = updatedLikes)
                firestore.collection("social_posts").document(postId).set(updatedPost).await()
                socialPostDao.updatePost(updatedPost)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addComment(comment: Comment): Result<Unit> {
        return try {
            firestore.collection("comments").document(comment.id).set(comment).await()
            commentDao.insertComment(comment)
            
            // Update post with new comment
            val post = socialPostDao.getPostById(comment.postId)
            if (post != null) {
                val updatedComments = post.comments + comment
                val updatedPost = post.copy(comments = updatedComments)
                firestore.collection("social_posts").document(comment.postId).set(updatedPost).await()
                socialPostDao.updatePost(updatedPost)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateComment(comment: Comment): Result<Unit> {
        return try {
            firestore.collection("comments").document(comment.id).set(comment).await()
            commentDao.updateComment(comment)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteComment(commentId: String): Result<Unit> {
        return try {
            val comment = commentDao.getCommentById(commentId)
            if (comment != null) {
                firestore.collection("comments").document(commentId).delete().await()
                commentDao.deleteComment(comment)
                
                // Update post to remove comment
                val post = socialPostDao.getPostById(comment.postId)
                if (post != null) {
                    val updatedComments = post.comments.filter { it.id != commentId }
                    val updatedPost = post.copy(comments = updatedComments)
                    firestore.collection("social_posts").document(comment.postId).set(updatedPost).await()
                    socialPostDao.updatePost(updatedPost)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun likeComment(commentId: String, userId: String): Result<Unit> {
        return try {
            val comment = commentDao.getCommentById(commentId)
            if (comment != null) {
                val updatedLikes = if (comment.likes.contains(userId)) {
                    comment.likes - userId
                } else {
                    comment.likes + userId
                }
                val updatedComment = comment.copy(likes = updatedLikes)
                firestore.collection("comments").document(commentId).set(updatedComment).await()
                commentDao.updateComment(updatedComment)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addReply(reply: Reply): Result<Unit> {
        return try {
            firestore.collection("replies").document(reply.id).set(reply).await()
            replyDao.insertReply(reply)
            
            // Update comment with new reply
            val comment = commentDao.getCommentById(reply.commentId)
            if (comment != null) {
                val updatedReplies = comment.replies + reply
                val updatedComment = comment.copy(replies = updatedReplies)
                firestore.collection("comments").document(reply.commentId).set(updatedComment).await()
                commentDao.updateComment(updatedComment)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateReply(reply: Reply): Result<Unit> {
        return try {
            firestore.collection("replies").document(reply.id).set(reply).await()
            replyDao.updateReply(reply)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteReply(replyId: String): Result<Unit> {
        return try {
            val reply = replyDao.getReplyById(replyId)
            if (reply != null) {
                firestore.collection("replies").document(replyId).delete().await()
                replyDao.deleteReply(reply)
                
                // Update comment to remove reply
                val comment = commentDao.getCommentById(reply.commentId)
                if (comment != null) {
                    val updatedReplies = comment.replies.filter { it.id != replyId }
                    val updatedComment = comment.copy(replies = updatedReplies)
                    firestore.collection("comments").document(reply.commentId).set(updatedComment).await()
                    commentDao.updateComment(updatedComment)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun likeReply(replyId: String, userId: String): Result<Unit> {
        return try {
            val reply = replyDao.getReplyById(replyId)
            if (reply != null) {
                val updatedLikes = if (reply.likes.contains(userId)) {
                    reply.likes - userId
                } else {
                    reply.likes + userId
                }
                val updatedReply = reply.copy(likes = updatedLikes)
                firestore.collection("replies").document(replyId).set(updatedReply).await()
                replyDao.updateReply(updatedReply)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadImage(filePath: String, fileName: String): Result<String> {
        return try {
            val storageRef = storage.reference.child("social_images/$fileName")
            val uploadTask = storageRef.putFile(android.net.Uri.parse(filePath)).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
