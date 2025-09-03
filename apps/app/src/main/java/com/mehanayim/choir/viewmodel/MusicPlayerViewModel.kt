package com.mehanayim.choir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehanayim.choir.data.model.Music
import com.mehanayim.choir.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {
    
    private val _currentMusic = MutableStateFlow<Music?>(null)
    val currentMusic: StateFlow<Music?> = _currentMusic.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()
    
    private val _showLyrics = MutableStateFlow(false)
    val showLyrics: StateFlow<Boolean> = _showLyrics.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun loadMusic(musicId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            musicRepository.getMusicById(musicId)
                .onSuccess { music ->
                    _currentMusic.value = music
                    _duration.value = music.duration
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to load music"
                }
            
            _isLoading.value = false
        }
    }
    
    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
        // TODO: Implement actual media player controls
    }
    
    fun play() {
        _isPlaying.value = true
        // TODO: Implement play functionality
    }
    
    fun pause() {
        _isPlaying.value = false
        // TODO: Implement pause functionality
    }
    
    fun stop() {
        _isPlaying.value = false
        _currentPosition.value = 0L
        // TODO: Implement stop functionality
    }
    
    fun seekTo(position: Long) {
        _currentPosition.value = position
        // TODO: Implement seek functionality
    }
    
    fun previous() {
        // TODO: Implement previous track functionality
    }
    
    fun next() {
        // TODO: Implement next track functionality
    }
    
    fun toggleLyrics() {
        _showLyrics.value = !_showLyrics.value
    }
    
    fun showLyrics() {
        _showLyrics.value = true
    }
    
    fun hideLyrics() {
        _showLyrics.value = false
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
