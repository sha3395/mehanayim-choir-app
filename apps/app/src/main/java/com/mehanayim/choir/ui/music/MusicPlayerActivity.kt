package com.mehanayim.choir.ui.music

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mehanayim.choir.ui.theme.MehanayimChoirTheme
import com.mehanayim.choir.viewmodel.MusicPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlayerActivity : ComponentActivity() {
    
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val musicId = intent.getStringExtra("music_id") ?: ""
        
        setContent {
            MehanayimChoirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayerScreen(
                        musicId = musicId,
                        musicPlayerViewModel = musicPlayerViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    musicId: String,
    musicPlayerViewModel: MusicPlayerViewModel
) {
    val music by musicPlayerViewModel.currentMusic.collectAsState()
    val isPlaying by musicPlayerViewModel.isPlaying.collectAsState()
    val currentPosition by musicPlayerViewModel.currentPosition.collectAsState()
    val duration by musicPlayerViewModel.duration.collectAsState()
    val showLyrics by musicPlayerViewModel.showLyrics.collectAsState()
    
    LaunchedEffect(musicId) {
        musicPlayerViewModel.loadMusic(musicId)
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Blurred background image
        music?.thumbnailUrl?.let { imageUrl ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 20.dp),
                contentScale = ContentScale.Crop
            )
        }
        
        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* TODO: Navigate back */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Text(
                    text = "Now Playing",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                
                IconButton(
                    onClick = { musicPlayerViewModel.toggleLyrics() }
                ) {
                    Icon(
                        imageVector = if (showLyrics) Icons.Default.Lyrics else Icons.Default.TextFields,
                        contentDescription = "Toggle Lyrics",
                        tint = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Album art
            music?.thumbnailUrl?.let { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Song info
            Text(
                text = music?.title ?: "Unknown Title",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = music?.artist ?: "Unknown Artist",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Progress bar
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = currentPosition.toFloat(),
                    onValueChange = { musicPlayerViewModel.seekTo(it.toLong()) },
                    valueRange = 0f..duration.toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatTime(duration),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Control buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { musicPlayerViewModel.previous() }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                IconButton(
                    onClick = { musicPlayerViewModel.togglePlayPause() }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                IconButton(
                    onClick = { musicPlayerViewModel.next() }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Lyrics section
            if (showLyrics && !music?.lyrics.isNullOrEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.5f)
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        music?.lyrics?.split("\n")?.forEach { line ->
                            item {
                                Text(
                                    text = line,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
