package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.MusicCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicCategoryDao {
    @Query("SELECT * FROM musiccategory ORDER BY name ASC")
    fun getAllCategories(): Flow<List<MusicCategory>>

    @Query("SELECT * FROM musiccategory WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): MusicCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: MusicCategory)

    @Update
    suspend fun updateCategory(category: MusicCategory)

    @Delete
    suspend fun deleteCategory(category: MusicCategory)
}
