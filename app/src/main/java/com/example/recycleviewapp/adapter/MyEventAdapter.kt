package com.example.recycleviewapp.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.R
import com.example.recycleviewapp.model.Event
import com.example.recycleviewapp.navigate
import com.example.recycleviewapp.views.ThirdFragment
import java.util.zip.Inflater

class MyEventAdapter(
    private val eventList: MutableList<Event> = mutableListOf()
) : RecyclerView.Adapter<EventViewHolder>() {

    private lateinit var mListener: OnEventClickListener

    interface OnEventClickListener{
        fun onEventClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnEventClickListener) {
        mListener = listener
    }

    fun updateEventData(event: Event) {
        eventList.add( event)
        notifyItemInserted(eventList.indexOf(event))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val eventView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(eventView, mListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        holder.bind(event)
    }

    override fun getItemCount(): Int = eventList.size



}


class EventViewHolder(itemView: View, listener: MyEventAdapter.OnEventClickListener) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.eventTitle)
    private val category: TextView  = itemView.findViewById(R.id.eventCategory)
    private val date: TextView = itemView.findViewById(R.id.eventDate)

    init {
        itemView.setOnClickListener {
            listener.onEventClick(adapterPosition)
        }
    }

    fun bind(event: Event) {
        title.text = event.title
        category.text = event.category
        date.text = event.date
    }
}