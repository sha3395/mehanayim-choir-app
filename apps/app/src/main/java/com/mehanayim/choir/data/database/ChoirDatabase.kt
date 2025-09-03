package com.mehanayim.choir.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.mehanayim.choir.data.dao.*
import com.mehanayim.choir.data.model.*

@Database(
    entities = [
        User::class,
        Music::class,
        MusicCategory::class,
        SocialPost::class,
        Comment::class,
        Reply::class,
        News::class,
        NewsFile::class,
        NewsLink::class,
        AppTheme::class,
        UIElement::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChoirDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun musicDao(): MusicDao
    abstract fun musicCategoryDao(): MusicCategoryDao
    abstract fun socialPostDao(): SocialPostDao
    abstract fun commentDao(): CommentDao
    abstract fun replyDao(): ReplyDao
    abstract fun newsDao(): NewsDao
    abstract fun newsFileDao(): NewsFileDao
    abstract fun newsLinkDao(): NewsLinkDao
    abstract fun appThemeDao(): AppThemeDao
    abstract fun uiElementDao(): UIElementDao

    companion object {
        @Volatile
        private var INSTANCE: ChoirDatabase? = null

        fun getDatabase(context: Context): ChoirDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChoirDatabase::class.java,
                    "choir_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
