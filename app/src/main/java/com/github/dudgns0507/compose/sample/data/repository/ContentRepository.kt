package com.github.dudgns0507.compose.sample.data.repository

import com.github.dudgns0507.compose.sample.data.model.ImageDto
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    suspend fun getImages(): Flow<List<ImageDto>>
}