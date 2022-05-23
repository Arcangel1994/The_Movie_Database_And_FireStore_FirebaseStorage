package com.example.themoviedbandfirebase.di

import android.content.Context
import androidx.room.Room
import com.example.themoviedbandfirebase.data.database.TheMovieDBDatabase
import com.example.themoviedbandfirebase.util.Constants.Companion.DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, TheMovieDBDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun mostPopularDAO(database: TheMovieDBDatabase) = database.mostPopularDAO()

    @Singleton
    @Provides
    fun myLocationDAO(database: TheMovieDBDatabase) = database.myLocationDAO()

    @Singleton
    @Provides
    fun imagesDAO(database: TheMovieDBDatabase) = database.imagesDAO()


}