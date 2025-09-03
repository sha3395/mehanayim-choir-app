package com.mehanayim.choir.ui.admin

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
import com.mehanayim.choir.ui.theme.MehanayimChoirTheme
import com.mehanayim.choir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminMainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MehanayimChoirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdminDashboard(
                        authViewModel = authViewModel,
                        onSignOut = { signOut() }
                    )
                }
            }
        }
        
        // Check if user is still authenticated and is admin
        lifecycleScope.launch {
            authViewModel.authState.collect { authState ->
                when (authState) {
                    is com.mehanayim.choir.MainActivity.AuthState.Unauthenticated -> {
                        navigateToAuth()
                    }
                    is com.mehanayim.choir.MainActivity.AuthState.Authenticated -> {
                        if (authState.user.role != com.mehanayim.choir.data.model.UserRole.ADMIN) {
                            navigateToAuth()
                        }
                    }
                    else -> { /* Stay on admin dashboard */ }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentUser by authViewModel.currentUser.collectAsState()
    
    val tabs = listOf(
        "UI Editor" to Icons.Default.Edit,
        "Upload" to Icons.Default.Upload,
        "Management" to Icons.Default.People,
        "News" to Icons.Default.Article
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Admin Panel",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    Text(
                        text = currentUser?.name ?: "Admin",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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
                0 -> AdminUIEditorPage()
                1 -> AdminUploadPage()
                2 -> AdminManagementPage()
                3 -> AdminNewsPage()
            }
        }
    }
}

@Composable
fun AdminUIEditorPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "UI Editor",
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "No-Code Interface Editor",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Drag and drop elements to customize the app interface. Modify colors, fonts, layouts, and content without coding.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO: Open UI editor */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open UI Editor")
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        items(listOf(
            "Change App Colors" to Icons.Default.Palette,
            "Edit Home Page Content" to Icons.Default.Edit,
            "Add New Categories" to Icons.Default.Add,
            "Modify Navigation" to Icons.Default.Navigation,
            "Update App Logo" to Icons.Default.Image
        )) { (title, icon) ->
            UIEditorActionCard(
                title = title,
                icon = icon,
                onClick = { /* TODO: Implement action */ }
            )
        }
    }
}

@Composable
fun UIEditorActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AdminUploadPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Content Upload",
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Upload Music & Lyrics",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Upload new music files, lyrics, and organize them by category and month.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO: Open music upload */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.MusicNote, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload Music")
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Upload Options",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        items(listOf(
            "Music Files" to Icons.Default.AudioFile,
            "Lyrics Text" to Icons.Default.TextFields,
            "Thumbnail Images" to Icons.Default.Image,
            "Create Categories" to Icons.Default.Category,
            "Bulk Upload" to Icons.Default.Upload
        )) { (title, icon) ->
            UploadOptionCard(
                title = title,
                icon = icon,
                onClick = { /* TODO: Implement upload option */ }
            )
        }
    }
}

@Composable
fun UploadOptionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AdminManagementPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "User Management",
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Manage Users",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "View, edit, block, or delete user accounts. Monitor user activity and content.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO: Open user management */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.People, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Manage Users")
                    }
                }
            }
        }
        
        item {
            Text(
                text = "User Statistics",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Total Users",
                    value = "156",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Active Users",
                    value = "142",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Text(
                text = "Recent Users",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        items(8) { index ->
            UserManagementCard(
                userName = "User ${index + 1}",
                email = "user${index + 1}@example.com",
                role = if (index == 0) "Admin" else "User",
                isActive = index < 6,
                onEdit = { /* TODO: Edit user */ },
                onBlock = { /* TODO: Block user */ },
                onDelete = { /* TODO: Delete user */ }
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UserManagementCard(
    userName: String,
    email: String,
    role: String,
    isActive: Boolean,
    onEdit: () -> Unit,
    onBlock: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (role == "Admin") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isActive) Icons.Default.CheckCircle else Icons.Default.Block,
                        contentDescription = null,
                        tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }
                OutlinedButton(
                    onClick = onBlock,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isActive) "Block" else "Unblock")
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun AdminNewsPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "News Management",
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Publish News",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Create and publish news articles for choir members. Support text, images, files, and links.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO: Create new news */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create News")
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Recent News",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        items(6) { index ->
            NewsManagementCard(
                title = "News Article ${index + 1}",
                summary = "This is a sample news article that can be published to choir members.",
                isPublished = index < 4,
                publishDate = "Dec ${index + 1}, 2024",
                onEdit = { /* TODO: Edit news */ },
                onPublish = { /* TODO: Publish news */ },
                onDelete = { /* TODO: Delete news */ }
            )
        }
    }
}

@Composable
fun NewsManagementCard(
    title: String,
    summary: String,
    isPublished: Boolean,
    publishDate: String,
    onEdit: () -> Unit,
    onPublish: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPublished) Icons.Default.PublishedWithChanges else Icons.Default.Draft,
                        contentDescription = null,
                        tint = if (isPublished) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPublished) "Published" else "Draft",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isPublished) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = publishDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit")
                }
                OutlinedButton(
                    onClick = onPublish,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isPublished) "Unpublish" else "Publish")
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
