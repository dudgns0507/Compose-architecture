package com.github.dudgns0507.compose.sample.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dudgns0507.compose.sample.data.model.ImageDto
import com.github.dudgns0507.compose.sample.data.repository.ContentRepository
import com.github.dudgns0507.compose.sample.domain.usecase.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
): ViewModel() {
    private val _imageList = MutableStateFlow(listOf<ImageDto>())
    val imageList: StateFlow<List<ImageDto>> = _imageList

    fun getImages() {
        viewModelScope.launch {
            getImagesUseCase()
                .onStart {  }
                .catch {  }
                .collect { result ->
                    _imageList.value = result
                }
        }
    }
}