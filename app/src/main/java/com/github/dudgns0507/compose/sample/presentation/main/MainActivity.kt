package com.github.dudgns0507.compose.sample.presentation.main

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.dudgns0507.compose.sample.data.model.ImageDto
import com.github.dudgns0507.compose.sample.presentation.ui.theme.ComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint

data class ImageData(
    val id: Long, val title: String, val bucket: String, val date: Long, val uri: Uri
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()
    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            READ_EXTERNAL_STORAGE, READ_MEDIA_VIDEO, READ_MEDIA_IMAGES
        )
    } else {
        arrayOf(
            READ_EXTERNAL_STORAGE
        )
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val isAllGranted = permissions.all { e -> resultMap[e] == true }
        if (isAllGranted) {
            vm.getImages()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setContent {
            ComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    GalleryGridView(vm.imageList.value)
                }
            }
        }
    }

    private fun checkPermission() {
        when {
            permissions.any {
                ContextCompat.checkSelfPermission(
                    applicationContext, it
                ) == PackageManager.PERMISSION_GRANTED
            } -> {
                vm.getImages()
            }

            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
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
    ComposeSampleTheme {}
}

@Composable
fun GalleryGridView(list: List<ImageDto>) {
    val state = rememberLazyGridState()
    LazyVerticalGrid(state = state,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            items(list.size) { index ->
                AlbumView(list[index])
            }
        })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AlbumView(image: ImageDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        GlideImage(
            model = image.imageUri,
            contentDescription = image.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = {

                }),
            contentScale = ContentScale.Crop
        )
    }
}