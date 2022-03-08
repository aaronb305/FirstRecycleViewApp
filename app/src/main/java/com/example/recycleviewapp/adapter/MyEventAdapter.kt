package com.example.recycleviewapp.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewapp.R
import com.example.recycleviewapp.model.Event
import com.example.recycleviewapp.navigate
import com.example.recycleviewapp.views.ThirdFragment
import java.util.zip.Inflater

class MyEventAdapter(
    private val eventList: MutableList<Event> = mutableListOf(),
//    private val listener: (Event) -> Unit
) : RecyclerView.Adapter<EventViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    fun updateEventData(event: Event) {
        eventList.add(0, event)
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

//    class onClickListener(val clickListener: (event: Event) -> Unit) {
//        fun onClick(event: Event) = clickListener(event)
//    }

}


class EventViewHolder(itemView: View, listener: MyEventAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.eventTitle)
    private val category: TextView  = itemView.findViewById(R.id.eventCategory)
    private val date: TextView = itemView.findViewById(R.id.eventDate)

    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bind(event: Event) {
        title.text = event.title
        category.text = event.category
        date.text = event.date
    }
}