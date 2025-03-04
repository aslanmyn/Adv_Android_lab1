package com.example.androidadvancedlab1.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.androidadvancedlab1.R

class BroadcastFragment : Fragment(R.layout.fragment_broadcast) {
    private var text: String = "Airplane Mode Disabled"
    private val receiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                text = if (isAirplaneModeOn) "Airplane Mode Enabled" else "Airplane Mode Disabled"
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_broadcast, container, false)
        view.findViewById<TextView>(R.id.textView).text = text
        view.findViewById<Button>(R.id.button).setOnClickListener {
            view.findViewById<TextView>(R.id.textView).text = text
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(receiver)
    }

}