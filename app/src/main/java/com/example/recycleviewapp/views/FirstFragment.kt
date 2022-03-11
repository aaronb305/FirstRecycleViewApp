package com.example.recycleviewapp.views

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.R
import com.example.recycleviewapp.adapter.MyEventAdapter
import com.example.recycleviewapp.database.EventData
import com.example.recycleviewapp.database.EventDatabase
import com.example.recycleviewapp.databinding.FragmentFirstBinding
import com.example.recycleviewapp.navigate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding by lazy {
        FragmentFirstBinding.inflate(layoutInflater)
    }

    private val eventAdapter by lazy {
        MyEventAdapter()
    }

    private val bundle by lazy {
        Bundle()
    }

    private lateinit var myFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.myRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = eventAdapter

            binding.dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedItem = p0?.getItemAtPosition(p2).toString()
                    val stringArray = resources.getStringArray(R.array.howToSort)
                    if (selectedItem.equals(stringArray[0])) {
                        val ascending = MySingleton.event.sortBy {
                            it.date
                        }
                    }
                    else {
                        var descending = MySingleton.event.sortByDescending {
                           it.date
                        }
                    }

//
                    eventAdapter.updateEventData(MySingleton.event)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            val database = Room.databaseBuilder(requireContext(), EventDatabase::class.java, "EventDatabase")
                .build()
            val eventDao = database.eventDao()
//            eventDao.getAll()
        }

        binding.floatingButton.setOnClickListener {
            navigate(supportFragmentManager = requireActivity().supportFragmentManager, SecondFragment.newInstance("", ""))
        }

        eventAdapter.setOnItemClickListener(object: MyEventAdapter.OnEventClickListener{
            override fun onEventClick(position: Int) {
                bundle.putInt("position", position)
                navigate(supportFragmentManager = requireActivity().supportFragmentManager, ThirdFragment.newInstance(position))
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val channelId = "myChannel"
        val myChannel = NotificationChannel(channelId, "Events Today", NotificationManager.IMPORTANCE_HIGH)

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val localDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val startDate = LocalDate.parse(LocalDate.now().format(localDateFormatter), localDateFormatter)
        for (event in MySingleton.event) {
            val date = sdf.parse(event.date).time
            val endDate = LocalDate.parse(sdf.format(date), localDateFormatter)
            val zero: Long = 0
            if (ChronoUnit.DAYS.between(startDate, endDate) == zero) {
                val msg = "${event.title} is today!"
                val myNotification = NotificationCompat.Builder(requireContext(), channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("You have an event today!")
                    .setContentText(msg)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
                notificationManager.createNotificationChannel(myChannel)
                notificationManager.notify(123, myNotification)
            }
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        requireActivity().supportFragmentManager.putFragment(outState, "First Fragment", myFragment)
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        var item2 = savedInstanceState?.getInt("position")
//
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
