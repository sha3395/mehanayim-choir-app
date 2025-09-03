package com.mehanayim.choir.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppTheme(
    val id: String = "",
    val primaryColor: String = "#6200EE",
    val secondaryColor: String = "#03DAC6",
    val backgroundColor: String = "#FFFFFF",
    val surfaceColor: String = "#F5F5F5",
    val textColor: String = "#000000",
    val accentColor: String = "#FF6B6B",
    val fontFamily: String = "Roboto",
    val logoUrl: String = "",
    val backgroundImageUrl: String = "",
    val isActive: Boolean = true
) : Parcelable

@Parcelize
data class UIElement(
    val id: String = "",
    val type: UIElementType = UIElementType.TEXT,
    val content: String = "",
    val position: UIPosition = UIPosition(),
    val style: UIStyle = UIStyle(),
    val isVisible: Boolean = true,
    val isEditable: Boolean = true
) : Parcelable

@Parcelize
data class UIPosition(
    val x: Float = 0f,
    val y: Float = 0f,
    val width: Float = 100f,
    val height: Float = 50f
) : Parcelable

@Parcelize
data class UIStyle(
    val fontSize: Float = 16f,
    val fontColor: String = "#000000",
    val backgroundColor: String = "#FFFFFF",
    val borderRadius: Float = 8f,
    val padding: Float = 16f,
    val margin: Float = 8f
) : Parcelable

enum class UIElementType {
    TEXT, IMAGE, BUTTON, CATEGORY, PAGE
}
