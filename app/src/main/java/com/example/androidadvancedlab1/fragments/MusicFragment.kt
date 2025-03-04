package com.example.androidadvancedlab1.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.androidadvancedlab1.service.MusicPlayerService
import com.example.androidadvancedlab1.R

class MusicFragment : Fragment() {

    private val STARTFOREGROUND_ACTION = "com.example.musicplayer.action.START_FOREGROUND"
    private val STOPFOREGROUND_ACTION = "com.example.musicplayer.action.STOP_FOREGROUND"
    private val NOTIFICATION_CHANNEL_ID = "musicplayer_channel"
    private val PAUSE_ACTION = "com.example.musicplayer.action.PAUSE"
    private val PLAY_ACTION = "com.example.musicplayer.action.PLAY"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createNotificationChannel()

        val view = inflater.inflate(R.layout.fragment_music, container, false)
        val stop = view.findViewById<Button>(R.id.stop)
        val start = view.findViewById<Button>(R.id.play)
        start.setOnClickListener {
            val serviceIntent = Intent(context, MusicPlayerService::class.java)
            when (start.tag){
                "play" -> {
                    serviceIntent.action = PLAY_ACTION
                    start.tag = "pause"
                    start.text = "Pause"
                }
                "pause" -> {
                    serviceIntent.action = PAUSE_ACTION
                    start.tag = "play"
                    start.text = "Play"
                }
            }
            activity?.startService(serviceIntent)
        }
        stop.setOnClickListener {
            val serviceIntent = Intent(context, MusicPlayerService::class.java)
            serviceIntent.action = STOPFOREGROUND_ACTION
            activity?.stopService(serviceIntent)
            start.text = "Play"
        }


        return view
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Music Player1",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context?.getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }
    }
}