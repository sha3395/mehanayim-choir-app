package com.mehanayim.choir.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mehanayim.choir.data.dao.NewsDao
import com.mehanayim.choir.data.dao.NewsFileDao
import com.mehanayim.choir.data.dao.NewsLinkDao
import com.mehanayim.choir.data.model.News
import com.mehanayim.choir.data.model.NewsFile
import com.mehanayim.choir.data.model.NewsLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val newsFileDao: NewsFileDao,
    private val newsLinkDao: NewsLinkDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    
    fun getAllPublishedNews(): Flow<List<News>> = newsDao.getAllPublishedNews()
    
    fun getNewsByCategory(category: String): Flow<List<News>> = newsDao.getNewsByCategory(category)
    
    suspend fun getNewsById(newsId: String): News? = newsDao.getNewsById(newsId)
    
    fun getAllNews(): Flow<List<News>> = newsDao.getAllNews()
    
    fun getDraftNews(): Flow<List<News>> = newsDao.getDraftNews()
    
    fun getFilesByNews(newsId: String): Flow<List<NewsFile>> = newsFileDao.getFilesByNews(newsId)
    
    fun getLinksByNews(newsId: String): Flow<List<NewsLink>> = newsLinkDao.getLinksByNews(newsId)
    
    suspend fun createNews(news: News): Result<Unit> {
        return try {
            firestore.collection("news").document(news.id).set(news).await()
            newsDao.insertNews(news)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateNews(news: News): Result<Unit> {
        return try {
            firestore.collection("news").document(news.id).set(news).await()
            newsDao.updateNews(news)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteNews(newsId: String): Result<Unit> {
        return try {
            firestore.collection("news").document(newsId).delete().await()
            val news = newsDao.getNewsById(newsId)
            if (news != null) {
                newsDao.deleteNews(news)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun publishNews(newsId: String): Result<Unit> {
        return try {
            val news = newsDao.getNewsById(newsId)
            if (news != null) {
                val updatedNews = news.copy(
                    isPublished = true,
                    publishedAt = Date()
                )
                firestore.collection("news").document(newsId).set(updatedNews).await()
                newsDao.updateNews(updatedNews)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun unpublishNews(newsId: String): Result<Unit> {
        return try {
            val news = newsDao.getNewsById(newsId)
            if (news != null) {
                val updatedNews = news.copy(isPublished = false)
                firestore.collection("news").document(newsId).set(updatedNews).await()
                newsDao.updateNews(updatedNews)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addFileToNews(file: NewsFile): Result<Unit> {
        return try {
            firestore.collection("news_files").document(file.id).set(file).await()
            newsFileDao.insertFile(file)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateFile(file: NewsFile): Result<Unit> {
        return try {
            firestore.collection("news_files").document(file.id).set(file).await()
            newsFileDao.updateFile(file)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteFile(fileId: String): Result<Unit> {
        return try {
            firestore.collection("news_files").document(fileId).delete().await()
            val file = newsFileDao.getFileById(fileId)
            if (file != null) {
                newsFileDao.deleteFile(file)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addLinkToNews(link: NewsLink): Result<Unit> {
        return try {
            firestore.collection("news_links").document(link.id).set(link).await()
            newsLinkDao.insertLink(link)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateLink(link: NewsLink): Result<Unit> {
        return try {
            firestore.collection("news_links").document(link.id).set(link).await()
            newsLinkDao.updateLink(link)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteLink(linkId: String): Result<Unit> {
        return try {
            firestore.collection("news_links").document(linkId).delete().await()
            val link = newsLinkDao.getLinkById(linkId)
            if (link != null) {
                newsLinkDao.deleteLink(link)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadNewsImage(filePath: String, fileName: String): Result<String> {
        return try {
            val storageRef = storage.reference.child("news_images/$fileName")
            val uploadTask = storageRef.putFile(android.net.Uri.parse(filePath)).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadNewsFile(filePath: String, fileName: String): Result<String> {
        return try {
            val storageRef = storage.reference.child("news_files/$fileName")
            val uploadTask = storageRef.putFile(android.net.Uri.parse(filePath)).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
