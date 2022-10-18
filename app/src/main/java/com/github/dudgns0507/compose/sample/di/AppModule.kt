package com.github.dudgns0507.compose.sample.di

import android.content.ContentResolver
import android.content.Context
import com.github.dudgns0507.compose.sample.data.repository.ContentRepository
import com.github.dudgns0507.compose.sample.domain.repository.ContentRepositoryImpl
import com.github.dudgns0507.compose.sample.domain.usecase.GetImagesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideContentRepository(contentResolver: ContentResolver): ContentRepository {
        return ContentRepositoryImpl(contentResolver)
    }
}