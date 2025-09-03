package com.mehanayim.choir.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class News(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val summary: String = "",
    val images: List<String> = emptyList(),
    val files: List<NewsFile> = emptyList(),
    val links: List<NewsLink> = emptyList(),
    val category: String = "General",
    val priority: NewsPriority = NewsPriority.NORMAL,
    val publishedBy: String = "",
    val publishedAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isPublished: Boolean = false,
    val isActive: Boolean = true
) : Parcelable

@Parcelize
data class NewsFile(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val type: String = "",
    val size: Long = 0
) : Parcelable

@Parcelize
data class NewsLink(
    val id: String = "",
    val title: String = "",
    val url: String = "",
    val description: String = ""
) : Parcelable

enum class NewsPriority {
    LOW, NORMAL, HIGH, URGENT
}
