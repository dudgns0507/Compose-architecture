package com.github.dudgns0507.compose.sample.domain.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.github.dudgns0507.compose.sample.data.model.ImageDto
import com.github.dudgns0507.compose.sample.data.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ContentRepositoryImpl(
    private val contentResolver: ContentResolver
) : ContentRepository {
    override suspend fun getImages(): Flow<List<ImageDto>> {
        return flow {

            val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN
            )
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
            )?.use { cursor ->
                val list: ArrayList<ImageDto> = arrayListOf()
                while (cursor.moveToNext()) {
                    val idColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                    val titleColNum =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)
                    val dateTakenColNum =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
                    val albumNum =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

                    val id = cursor.getLong(idColNum)
                    val title = cursor.getString(titleColNum)
                    val album = cursor.getString(albumNum)
                    val dateTaken = cursor.getLong(dateTakenColNum)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    list.add(
                        ImageDto(
                            title = title,
                            imageUri = imageUri
                        )
                    )
                }

                emit(list)
            } ?: kotlin.run {
                emit(listOf())
            }
        }
    }
}