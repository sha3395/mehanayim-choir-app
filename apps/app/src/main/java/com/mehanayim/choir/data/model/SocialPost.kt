package com.mehanayim.choir.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class SocialPost(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val likes: List<String> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val isActive: Boolean = true
) : Parcelable

@Parcelize
data class Comment(
    val id: String = "",
    val postId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val content: String = "",
    val createdAt: Date = Date(),
    val replies: List<Reply> = emptyList(),
    val likes: List<String> = emptyList()
) : Parcelable

@Parcelize
data class Reply(
    val id: String = "",
    val commentId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val content: String = "",
    val createdAt: Date = Date(),
    val likes: List<String> = emptyList()
) : Parcelable
