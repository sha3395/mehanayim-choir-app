package com.mehanayim.choir

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.mehanayim.choir.ui.auth.AuthActivity
import com.mehanayim.choir.ui.admin.AdminMainActivity
import com.mehanayim.choir.ui.user.UserMainActivity
import com.mehanayim.choir.ui.theme.MehanayimChoirTheme
import com.mehanayim.choir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MehanayimChoirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen(
                        onNavigateToAuth = { navigateToAuth() },
                        onNavigateToUser = { navigateToUser() },
                        onNavigateToAdmin = { navigateToAdmin() }
                    )
                }
            }
        }
        
        // Observe authentication state
        lifecycleScope.launch {
            authViewModel.authState.collect { authState ->
                when (authState) {
                    is AuthState.Loading -> {
                        // Show loading state
                    }
                    is AuthState.Authenticated -> {
                        when (authState.user.role) {
                            com.mehanayim.choir.data.model.UserRole.USER -> navigateToUser()
                            com.mehanayim.choir.data.model.UserRole.ADMIN -> navigateToAdmin()
                        }
                    }
                    is AuthState.Unauthenticated -> {
                        navigateToAuth()
                    }
                }
            }
        }
    }
    
    private fun navigateToAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
    
    private fun navigateToUser() {
        startActivity(Intent(this, UserMainActivity::class.java))
        finish()
    }
    
    private fun navigateToAdmin() {
        startActivity(Intent(this, AdminMainActivity::class.java))
        finish()
    }
}

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToUser: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mehanayim Choir",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: com.mehanayim.choir.data.model.User) : AuthState()
    object Unauthenticated : AuthState()
}
