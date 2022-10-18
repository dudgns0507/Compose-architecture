package com.github.dudgns0507.compose.sample.di

import com.github.dudgns0507.compose.sample.data.repository.ContentRepository
import com.github.dudgns0507.compose.sample.domain.usecase.GetImagesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetImagesUseCase(repository: ContentRepository): GetImagesUseCase {
        return GetImagesUseCase(repository)
    }
}