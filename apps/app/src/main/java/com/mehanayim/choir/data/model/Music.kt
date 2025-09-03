package com.mehanayim.choir.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Music(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val category: String = "",
    val month: String = "",
    val year: Int = 0,
    val thumbnailUrl: String = "",
    val audioUrl: String = "",
    val lyrics: String = "",
    val duration: Long = 0,
    val uploadedBy: String = "",
    val uploadedAt: Date = Date(),
    val isActive: Boolean = true
) : Parcelable

@Parcelize
data class MusicCategory(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val color: String = "#6200EE",
    val icon: String = "music_note",
    val createdAt: Date = Date()
) : Parcelable
