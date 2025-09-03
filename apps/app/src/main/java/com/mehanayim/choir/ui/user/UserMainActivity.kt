package com.mehanayim.choir.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.mehanayim.choir.ui.auth.AuthActivity
import com.mehanayim.choir.ui.music.MusicPlayerActivity
import com.mehanayim.choir.ui.theme.MehanayimChoirTheme
import com.mehanayim.choir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserMainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MehanayimChoirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserDashboard(
                        authViewModel = authViewModel,
                        onSignOut = { signOut() },
                        onNavigateToMusicPlayer = { musicId -> navigateToMusicPlayer(musicId) }
                    )
                }
            }
        }
        
        // Check if user is still authenticated
        lifecycleScope.launch {
            authViewModel.authState.collect { authState ->
                when (authState) {
                    is com.mehanayim.choir.MainActivity.AuthState.Unauthenticated -> {
                        navigateToAuth()
                    }
                    else -> { /* Stay on user dashboard */ }
                }
            }
        }
    }
    
    private fun signOut() {
        authViewModel.signOut()
    }
    
    private fun navigateToAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
    
    private fun navigateToMusicPlayer(musicId: String) {
        val intent = Intent(this, MusicPlayerActivity::class.java)
        intent.putExtra("music_id", musicId)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard(
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit,
    onNavigateToMusicPlayer: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentUser by authViewModel.currentUser.collectAsState()
    
    val tabs = listOf(
        "Home" to Icons.Default.Home,
        "Music" to Icons.Default.MusicNote,
        "Social" to Icons.Default.People,
        "News" to Icons.Default.Article,
        "Profile" to Icons.Default.Person
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Mehanayim Choir",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, (title, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> UserHomePage()
                1 -> UserMusicPage(onNavigateToMusicPlayer = onNavigateToMusicPlayer)
                2 -> UserSocialPage()
                3 -> UserNewsPage()
                4 -> UserProfilePage(
                    user = currentUser,
                    onUpdateProfile = { user -> authViewModel.updateUserProfile(user) }
                )
            }
        }
    }
}

@Composable
fun UserHomePage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Welcome to Mehanayim Choir",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Join us in creating beautiful music together. Explore our latest songs, connect with fellow choir members, and stay updated with choir news.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        item {
            Text(
                text = "Recent Activities",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(5) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (index % 3) {
                            0 -> Icons.Default.MusicNote
                            1 -> Icons.Default.Article
                            else -> Icons.Default.People
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Activity ${index + 1}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Description of recent activity...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserMusicPage(onNavigateToMusicPlayer: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Music Library",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        items(10) { index ->
            MusicItemCard(
                title = "Song ${index + 1}",
                artist = "Choir Artist",
                category = "Category ${index % 3 + 1}",
                onPlayClick = { onNavigateToMusicPlayer("music_$index") }
            )
        }
    }
}

@Composable
fun MusicItemCard(
    title: String,
    artist: String,
    category: String,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onPlayClick) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }
    }
}

@Composable
fun UserSocialPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Social Feed",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        items(8) { index ->
            SocialPostCard(
                userName = "User ${index + 1}",
                content = "This is a sample social post content. Users can share their thoughts and experiences with the choir community.",
                likes = (index * 3 + 5),
                comments = (index * 2 + 1)
            )
        }
    }
}

@Composable
fun SocialPostCard(
    userName: String,
    content: String,
    likes: Int,
    comments: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = likes.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = comments.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun UserNewsPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Choir News",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        items(6) { index ->
            NewsCard(
                title = "News Title ${index + 1}",
                summary = "This is a summary of the choir news. Important updates and announcements will be posted here.",
                date = "Dec ${index + 1}, 2024"
            )
        }
    }
}

@Composable
fun NewsCard(
    title: String,
    summary: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UserProfilePage(
    user: com.mehanayim.choir.data.model.User?,
    onUpdateProfile: (com.mehanayim.choir.data.model.User) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var bio by remember { mutableStateOf(user?.bio ?: "") }
    var isEditing by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (isEditing) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    user?.let { currentUser ->
                                        val updatedUser = currentUser.copy(name = name, bio = bio)
                                        onUpdateProfile(updatedUser)
                                        isEditing = false
                                    }
                                }
                            ) {
                                Text("Save")
                            }
                            OutlinedButton(
                                onClick = {
                                    name = user?.name ?: ""
                                    bio = user?.bio ?: ""
                                    isEditing = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    } else {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = bio,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { isEditing = true }) {
                            Text("Edit Profile")
                        }
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Account Information",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Email")
                        Text(
                            text = user?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Role")
                        Text(
                            text = user?.role?.name ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Member Since")
                        Text(
                            text = "Dec 2024",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
