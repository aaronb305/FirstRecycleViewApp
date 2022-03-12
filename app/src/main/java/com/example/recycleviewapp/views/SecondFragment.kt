package com.example.recycleviewapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.room.Room
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.adapter.MyEventAdapter
import com.example.recycleviewapp.database.EventData
import com.example.recycleviewapp.database.EventDatabase
import com.example.recycleviewapp.databinding.FragmentSecondBinding
import com.example.recycleviewapp.model.Event
import com.example.recycleviewapp.navigate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding by lazy {
        FragmentSecondBinding.inflate(layoutInflater)
    }

    private val eventAdapter by lazy {
        MyEventAdapter()
    }

    private lateinit var sdf: SimpleDateFormat

    private lateinit var title: String
    private lateinit var category: String
    private lateinit var formattedDate: String
    private var date by Delegates.notNull<Long>()


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
    ): View? {
        // Inflate the layout for this fragment

        sdf = SimpleDateFormat("MM/dd/yyyy")
        val dataBase = Room.databaseBuilder(requireContext(), EventDatabase::class.java, "EventDatabase")
            .build()
        val eventDao = dataBase.eventDao()
        binding.eventCalendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, i, i2, i3 ->
            val month = i2 + 1
            if (month <= 9) {
                if (i3 <= 9) {
                    formattedDate = "0$month/0$i3/$i"
                }
                else {
                    formattedDate = "0$month/$i3/$i"
                }
            }
            else {
                formattedDate = "$month/$i3/$i"
            }
        }).let {
            formattedDate = sdf.format(binding.eventCalendar.date)
        }

        binding.backBtn.setOnClickListener {
            navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance("", ""))
        }

        binding.doneButton.setOnClickListener {
            if (binding.titleField.text.isNotEmpty() && binding.categoryField.text.isNotEmpty()) {
                title = binding.titleField.text.toString()
                category = binding.categoryField.text.toString()
                val event = Event(title, category, formattedDate)
                MySingleton.addEvent(event)
//                val eventData = EventData(Math.random().toInt(), title, category, formattedDate)
//                eventDao.insertUser(eventData)
//                    .subscribeOn(Schedulers.io())
//                    .subscribe({}, {})
//                    .apply {
//                        CompositeDisposable().add(this)
//                    }
                navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance("", ""))
            }
            else {
                val duration = Toast.LENGTH_LONG
                val errorMsg = "Please enter all required fields"
                Toast.makeText(requireContext(), errorMsg, duration).show()
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        date = if (formattedDate.isNotEmpty()) {
            sdf.parse(formattedDate).time
        } else {
            binding.eventCalendar.date
        }
        outState.putLong("date", date)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val tempDate: Long
        if (savedInstanceState!= null) {
            tempDate = savedInstanceState?.getLong("date")
            binding.eventCalendar.date = tempDate
            formattedDate = sdf.format(tempDate)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

