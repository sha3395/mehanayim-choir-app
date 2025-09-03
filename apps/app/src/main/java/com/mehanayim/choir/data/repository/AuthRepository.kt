package com.mehanayim.choir.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mehanayim.choir.data.model.User
import com.mehanayim.choir.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
    
    fun getCurrentUserFlow(): Flow<User?> = flow {
        firebaseAuth.addAuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                val user = getUserFromFirestore(firebaseUser.uid)
                emit(user)
            } else {
                emit(null)
            }
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val user = getUserFromFirestore(firebaseUser.uid)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User data not found"))
                }
            } else {
                Result.failure(Exception("Authentication failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUp(email: String, password: String, name: String, role: UserRole = UserRole.USER): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
                    email = email,
                    name = name,
                    role = role,
                    createdAt = Date(),
                    lastLoginAt = Date()
                )
                saveUserToFirestore(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut() {
        firebaseAuth.signOut()
    }
    
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun getUserFromFirestore(userId: String): User? {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            if (document.exists()) {
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private suspend fun saveUserToFirestore(user: User) {
        try {
            firestore.collection("users").document(user.id).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }
    
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            firestore.collection("users").document(user.id).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
