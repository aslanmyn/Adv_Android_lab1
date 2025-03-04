package com.example.androidadvancedlab1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidadvancedlab1.model.CalendarEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private var events: List<CalendarEvent> = listOf()

    fun setEvents(events: List<CalendarEvent>) {
        this.events = events
        notifyDataSetChanged()
    }
    fun ser(){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount() = events.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: CalendarEvent) {
            itemView.findViewById<TextView>(android.R.id.text1).text = event.title
            itemView.findViewById<TextView>(android.R.id.text2).text =
                SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(event.startTime))
        }
    }
}
// Это