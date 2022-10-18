package com.github.dudgns0507.compose.sample

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.dudgns0507.compose.sample.ui.theme.ComposeSampleTheme

data class ImageData(
    val id: Long,
    val title: String,
    val bucket: String,
    val date: Long,
    val uri: Uri
)

class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(
        READ_EXTERNAL_STORAGE,
        READ_MEDIA_VIDEO,
        READ_MEDIA_IMAGES
    )
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { resultMap ->
            val isAllGranted = permissions.all { e -> resultMap[e] == true }
            if (isAllGranted) {
                getImages()
            }
        }

    private val _imageList = mutableStateOf(listOf<ImageData>())
    var imageList: State<List<ImageData>> = _imageList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setContent {
            ComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GalleryGridView(imageList.value)
                }
            }
        }
    }

    private fun getImages() {
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
            val list: ArrayList<ImageData> = arrayListOf()
            while (cursor.moveToNext()) {
                val idColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val titleColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)
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

                list.add(ImageData(id, title, album, dateTaken, imageUri))
            }

            _imageList.value = list
        }
    }

    private fun checkPermission() {
        when {
            permissions.any {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            } -> {
                getImages()
            }

            shouldShowRequestPermissionRationale (READ_EXTERNAL_STORAGE) -> {
            }

            else -> {
                requestPermissionLauncher.launch(permissions)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSampleTheme {
    }
}

@Composable
fun GalleryGridView(list: List<ImageData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            items(list.size) { index ->
                AlbumView(list[index], "Test Image")
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AlbumView(imagePath: ImageData, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        GlideImage(
            model = imagePath.uri,
            contentDescription = desc,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = {

                }),
            contentScale = ContentScale.Crop
        )
    }
}