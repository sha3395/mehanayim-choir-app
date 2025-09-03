package com.mehanayim.choir.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.mehanayim.choir.ui.admin.AdminMainActivity
import com.mehanayim.choir.ui.theme.MehanayimChoirTheme
import com.mehanayim.choir.ui.user.UserMainActivity
import com.mehanayim.choir.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MehanayimChoirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(
                        authViewModel = authViewModel,
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
                    is com.mehanayim.choir.MainActivity.AuthState.Authenticated -> {
                        when (authState.user.role) {
                            com.mehanayim.choir.data.model.UserRole.USER -> navigateToUser()
                            com.mehanayim.choir.data.model.UserRole.ADMIN -> navigateToAdmin()
                        }
                    }
                    else -> { /* Stay on auth screen */ }
                }
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onNavigateToUser: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Title
        Text(
            text = "Mehanayim Choir",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Auth Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isSignUp) "Create Account" else "Sign In",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (isSignUp) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    )
                }
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Button(
                    onClick = {
                        if (isSignUp) {
                            authViewModel.signUp(email, password, name)
                        } else {
                            authViewModel.signIn(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank() && (!isSignUp || name.isNotBlank())
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(if (isSignUp) "Sign Up" else "Sign In")
                    }
                }
                
                if (!isSignUp) {
                    TextButton(
                        onClick = { /* TODO: Implement forgot password */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Forgot Password?")
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUp) "Already have an account?" else "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = { 
                            isSignUp = !isSignUp
                            authViewModel.clearError()
                        }
                    ) {
                        Text(if (isSignUp) "Sign In" else "Sign Up")
                    }
                }
            }
        }
    }
}
