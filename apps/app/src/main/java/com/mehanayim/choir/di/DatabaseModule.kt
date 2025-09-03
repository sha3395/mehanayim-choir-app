package com.mehanayim.choir.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mehanayim.choir.data.database.ChoirDatabase
import com.mehanayim.choir.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideChoirDatabase(@ApplicationContext context: Context): ChoirDatabase {
        return ChoirDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideUserDao(database: ChoirDatabase): UserDao = database.userDao()
    
    @Provides
    fun provideMusicDao(database: ChoirDatabase): MusicDao = database.musicDao()
    
    @Provides
    fun provideMusicCategoryDao(database: ChoirDatabase): MusicCategoryDao = database.musicCategoryDao()
    
    @Provides
    fun provideSocialPostDao(database: ChoirDatabase): SocialPostDao = database.socialPostDao()
    
    @Provides
    fun provideCommentDao(database: ChoirDatabase): CommentDao = database.commentDao()
    
    @Provides
    fun provideReplyDao(database: ChoirDatabase): ReplyDao = database.replyDao()
    
    @Provides
    fun provideNewsDao(database: ChoirDatabase): NewsDao = database.newsDao()
    
    @Provides
    fun provideNewsFileDao(database: ChoirDatabase): NewsFileDao = database.newsFileDao()
    
    @Provides
    fun provideNewsLinkDao(database: ChoirDatabase): NewsLinkDao = database.newsLinkDao()
    
    @Provides
    fun provideAppThemeDao(database: ChoirDatabase): AppThemeDao = database.appThemeDao()
    
    @Provides
    fun provideUIElementDao(database: ChoirDatabase): UIElementDao = database.uiElementDao()
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}
