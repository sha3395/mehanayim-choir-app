package com.mehanayim.choir.data.dao

import androidx.room.*
import com.mehanayim.choir.data.model.User
import com.mehanayim.choir.data.model.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE role = :role")
    fun getUsersByRole(role: UserRole): Flow<List<User>>

    @Query("SELECT * FROM user WHERE isActive = 1")
    fun getAllActiveUsers(): Flow<List<User>>

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE user SET isActive = :isActive WHERE id = :userId")
    suspend fun updateUserStatus(userId: String, isActive: Boolean)

    @Query("UPDATE user SET lastLoginAt = :loginTime WHERE id = :userId")
    suspend fun updateLastLogin(userId: String, loginTime: Long)
}
