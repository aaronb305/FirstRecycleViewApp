package com.example.recycleviewapp.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recycleviewapp.EventDatabaseSingle
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.R
import com.example.recycleviewapp.adapter.MyEventAdapter
import com.example.recycleviewapp.database.EventDatabase
import com.example.recycleviewapp.databinding.FragmentFirstBinding
import com.example.recycleviewapp.navigate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.properties.Delegates

/**
 * Everytime you create a new fragment using AS, please ensure to delete the code that is unused.
 */
class FirstFragment : Fragment() {

    private val binding by lazy {
        FragmentFirstBinding.inflate(layoutInflater)
    }

    private val eventAdapter by lazy {
        MyEventAdapter()
    }

    private val database by lazy {
        EventDatabaseSingle.getDatabase(requireContext())
    }

    private val disposable by lazy {
        CompositeDisposable()
    }

    private var notification by Delegates.notNull<Boolean>()

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
                    if (selectedItem == stringArray[0]) {

                        // this variable is not used
                        MySingleton.event.sortBy { it.date }
                    }
                    else {
                        // this variable is not used
                        MySingleton.event.sortByDescending { it.date }
                    }


                    eventAdapter.updateEventData(MySingleton.event)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // if this one is not used, removed the missing part adn just mark it as no-op
                    // no-op

                    // if you leave the todo
                    // then you will have an exception if that code is reached
                }
            }

            database.eventDao().getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.d("FirstFragment", "***** Items loaded " + it.size)
                        MySingleton.event = it.toMutableList()
                        eventAdapter.updateEventData(MySingleton.event)

                    },
                    { Log.e("FirstFragment", "***** Error getting database") }
                )
                .apply {
                    disposable.add(this)
                }
        }

        binding.floatingButton.setOnClickListener {
            navigate(supportFragmentManager = requireActivity().supportFragmentManager, SecondFragment.newInstance())
        }

        eventAdapter.setOnItemClickListener(object: MyEventAdapter.OnEventClickListener{
            override fun onEventClick(position: Int) {
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

    override fun onDestroyView() {
        super.onDestroyView()

        disposable.dispose()
    }

    // commented code should be removed as well as unused code

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
         * If arguments are not used right here, just remove them
         */
        fun newInstance() = FirstFragment()
    }
}
