package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.UIElement
import kotlinx.coroutines.flow.Flow

@Dao
interface UIElementDao {
    @Query("SELECT * FROM uielement WHERE isVisible = 1 ORDER BY position_y ASC")
    fun getAllVisibleElements(): Flow<List<UIElement>>

    @Query("SELECT * FROM uielement WHERE type = :type AND isVisible = 1")
    fun getElementsByType(type: String): Flow<List<UIElement>>

    @Query("SELECT * FROM uielement WHERE id = :elementId")
    suspend fun getElementById(elementId: String): UIElement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: UIElement)

    @Update
    suspend fun updateElement(element: UIElement)

    @Delete
    suspend fun deleteElement(element: UIElement)

    @Query("UPDATE uielement SET isVisible = :isVisible WHERE id = :elementId")
    suspend fun updateElementVisibility(elementId: String, isVisible: Boolean)
}
