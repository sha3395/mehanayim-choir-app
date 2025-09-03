package com.mehanayim.choir.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val role: UserRole = UserRole.USER,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val lastLoginAt: Date = Date(),
    val uploadedImages: List<String> = emptyList(),
    val uploadedTexts: List<String> = emptyList()
) : Parcelable

enum class UserRole {
    USER, ADMIN
}
