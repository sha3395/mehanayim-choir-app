package com.mehanayim.choir.data.database

import androidx.room.TypeConverter
import com.mehanayim.choir.data.model.*
import java.util.Date

class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.name
    }

    @TypeConverter
    fun toUserRole(role: String): UserRole {
        return UserRole.valueOf(role)
    }

    @TypeConverter
    fun fromNewsPriority(priority: NewsPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toNewsPriority(priority: String): NewsPriority {
        return NewsPriority.valueOf(priority)
    }

    @TypeConverter
    fun fromUIElementType(type: UIElementType): String {
        return type.name
    }

    @TypeConverter
    fun toUIElementType(type: String): UIElementType {
        return UIElementType.valueOf(type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}
