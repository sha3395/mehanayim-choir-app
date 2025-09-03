package com.mehanayim.choir.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mehanayim.choir.data.dao.MusicDao
import com.mehanayim.choir.data.dao.MusicCategoryDao
import com.mehanayim.choir.data.model.Music
import com.mehanayim.choir.data.model.MusicCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val musicDao: MusicDao,
    private val musicCategoryDao: MusicCategoryDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    
    fun getAllMusic(): Flow<List<Music>> = musicDao.getAllActiveMusic()
    
    fun getMusicByCategory(category: String): Flow<List<Music>> = musicDao.getMusicByCategory(category)
    
    fun getMusicByMonth(month: String, year: Int): Flow<List<Music>> = musicDao.getMusicByMonth(month, year)
    
    suspend fun getMusicById(musicId: String): Music? = musicDao.getMusicById(musicId)
    
    fun getAllCategories(): Flow<List<String>> = musicDao.getAllCategories()
    
    fun getMonthsByYear(year: Int): Flow<List<String>> = musicDao.getMonthsByYear(year)
    
    fun getAllYears(): Flow<List<Int>> = musicDao.getAllYears()
    
    fun getAllMusicCategories(): Flow<List<MusicCategory>> = musicCategoryDao.getAllCategories()
    
    suspend fun insertMusic(music: Music): Result<Unit> {
        return try {
            // Save to Firestore
            firestore.collection("music").document(music.id).set(music).await()
            // Save to local database
            musicDao.insertMusic(music)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateMusic(music: Music): Result<Unit> {
        return try {
            firestore.collection("music").document(music.id).set(music).await()
            musicDao.updateMusic(music)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteMusic(musicId: String): Result<Unit> {
        return try {
            firestore.collection("music").document(musicId).delete().await()
            val music = musicDao.getMusicById(musicId)
            if (music != null) {
                musicDao.deleteMusic(music)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun insertCategory(category: MusicCategory): Result<Unit> {
        return try {
            firestore.collection("music_categories").document(category.id).set(category).await()
            musicCategoryDao.insertCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateCategory(category: MusicCategory): Result<Unit> {
        return try {
            firestore.collection("music_categories").document(category.id).set(category).await()
            musicCategoryDao.updateCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            firestore.collection("music_categories").document(categoryId).delete().await()
            val category = musicCategoryDao.getCategoryById(categoryId)
            if (category != null) {
                musicCategoryDao.deleteCategory(category)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadAudioFile(filePath: String, fileName: String): Result<String> {
        return try {
            val storageRef = storage.reference.child("music/audio/$fileName")
            val uploadTask = storageRef.putFile(android.net.Uri.parse(filePath)).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadThumbnailImage(filePath: String, fileName: String): Result<String> {
        return try {
            val storageRef = storage.reference.child("music/thumbnails/$fileName")
            val uploadTask = storageRef.putFile(android.net.Uri.parse(filePath)).await()
            val downloadUrl = storageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
