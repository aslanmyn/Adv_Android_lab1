package com.example.androidadvancedlab1.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.androidadvancedlab1.R

class MusicPlayerService : Service() {

    private val STARTFOREGROUND_ACTION = "com.example.musicplayer.action.START_FOREGROUND"
    private val STOPFOREGROUND_ACTION = "com.example.musicplayer.action.STOP_FOREGROUND"
    private val NOTIFICATION_CHANNEL_ID = "musicplayer_channel"
    private val PAUSE_ACTION = "com.example.musicplayer.action.PAUSE"
    private val PLAY_ACTION = "com.example.musicplayer.action.PLAY"
    private val NOTIFICATION_ID = 1
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(assets.openFd("kanye.mp3"))
            prepareAsync()
            isLooping = true
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        when(intent.action){
            STARTFOREGROUND_ACTION -> {
                playMusic()
                isPlaying = true
                updateNotification()
            }
            STOPFOREGROUND_ACTION -> {
                stopMusic()
                stopForeground(true)
                stopSelf()
            }
            PAUSE_ACTION -> {
                pauseMusic()
                isPlaying = false
                updateNotification()
            }
            PLAY_ACTION -> {
                playMusic()
                isPlaying = true
                updateNotification()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlaying) {
            stopMusic()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val pauseIntent = Intent(this, MusicPlayerService::class.java).apply {
            action = PAUSE_ACTION
        }
        val pendingPauseIntent = PendingIntent.getService(
            this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val playPauseIntent = Intent(this, MusicPlayerService::class.java).apply {
            action = if (isPlaying) PAUSE_ACTION else PLAY_ACTION
        }
        val pendingPlayPauseIntent = PendingIntent.getService(
            this, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, MusicPlayerService::class.java)
        stopIntent.action = STOPFOREGROUND_ACTION
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText(if(isPlaying) "Playing Music" else "Paused")
            .setSmallIcon(R.drawable.music)
            .addAction(R.drawable.stop, "Stop", pendingStopIntent)
            .addAction(if(isPlaying) R.drawable.pause else R.drawable.play, if(isPlaying) "Pause" else "Play", pendingPlayPauseIntent)
            .build()

        return builder
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager

        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    private fun playMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
            updateNotification() // Update notification when paused
        }
    }

    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }
    }

}