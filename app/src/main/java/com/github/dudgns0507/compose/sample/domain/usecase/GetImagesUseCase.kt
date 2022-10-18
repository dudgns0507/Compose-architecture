package com.github.dudgns0507.compose.sample.domain.usecase

import com.github.dudgns0507.compose.sample.data.model.ImageDto
import com.github.dudgns0507.compose.sample.data.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImagesUseCase(
    private val repository: ContentRepository
) {
    suspend operator fun invoke(): Flow<List<ImageDto>> {
        return repository.getImages()
    }
}
