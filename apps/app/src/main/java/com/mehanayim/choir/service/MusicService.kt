package com.mehanayim.choir.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mehanayim.choir.R
import com.mehanayim.choir.data.model.Music
import com.mehanayim.choir.ui.music.MusicPlayerActivity
import kotlinx.coroutines.*

class MusicService : Service() {
    
    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: Music? = null
    private var isPlaying = false
    private var currentPosition = 0L
    private var duration = 0L
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    companion object {
        const val CHANNEL_ID = "music_service_channel"
        const val NOTIFICATION_ID = 1
    }
    
    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> {
                val music = intent.getParcelableExtra<Music>("music")
                music?.let { playMusic(it) }
            }
            "PAUSE" -> pauseMusic()
            "STOP" -> stopMusic()
            "SEEK" -> {
                val position = intent.getLongExtra("position", 0L)
                seekTo(position)
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder = binder
    
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        serviceScope.cancel()
    }
    
    fun playMusic(music: Music) {
        currentMusic = music
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(music.audioUrl)
                setOnPreparedListener { mp ->
                    duration = mp.duration.toLong()
                    mp.start()
                    isPlaying = true
                    startForeground(NOTIFICATION_ID, createNotification())
                    startPositionUpdates()
                }
                setOnCompletionListener {
                    isPlaying = false
                    currentPosition = 0L
                    stopForeground(true)
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun pauseMusic() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
                isPlaying = false
                updateNotification()
            }
        }
    }
    
    fun resumeMusic() {
        mediaPlayer?.let { mp ->
            if (!mp.isPlaying) {
                mp.start()
                isPlaying = true
                startPositionUpdates()
                updateNotification()
            }
        }
    }
    
    fun stopMusic() {
        mediaPlayer?.let { mp ->
            mp.stop()
            mp.release()
        }
        mediaPlayer = null
        isPlaying = false
        currentPosition = 0L
        duration = 0L
        stopForeground(true)
    }
    
    fun seekTo(position: Long) {
        mediaPlayer?.let { mp ->
            mp.seekTo(position.toInt())
            currentPosition = position
        }
    }
    
    fun togglePlayPause() {
        if (isPlaying) {
            pauseMusic()
        } else {
            resumeMusic()
        }
    }
    
    fun getCurrentPosition(): Long = currentPosition
    
    fun getDuration(): Long = duration
    
    fun isMusicPlaying(): Boolean = isPlaying
    
    fun getCurrentMusic(): Music? = currentMusic
    
    private fun startPositionUpdates() {
        serviceScope.launch {
            while (isPlaying && mediaPlayer != null) {
                mediaPlayer?.let { mp ->
                    currentPosition = mp.currentPosition.toLong()
                }
                delay(1000)
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Music playback notification"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MusicPlayerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val playPauseAction = if (isPlaying) {
            NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                "Pause",
                createPendingIntent("PAUSE")
            )
        } else {
            NotificationCompat.Action(
                android.R.drawable.ic_media_play,
                "Play",
                createPendingIntent("PLAY")
            )
        }
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentMusic?.title ?: "Unknown Title")
            .setContentText(currentMusic?.artist ?: "Unknown Artist")
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .addAction(playPauseAction)
            .addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_menu_close_clear_cancel,
                    "Stop",
                    createPendingIntent("STOP")
                )
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1)
            )
            .build()
    }
    
    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this, action.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
