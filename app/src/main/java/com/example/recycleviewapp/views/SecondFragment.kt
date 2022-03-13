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
import com.example.recycleviewapp.EventDatabaseSingle
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.database.EventDatabase
import com.example.recycleviewapp.databinding.FragmentSecondBinding
import com.example.recycleviewapp.model.Event
import com.example.recycleviewapp.navigate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class SecondFragment : Fragment() {

    private val binding by lazy {
        FragmentSecondBinding.inflate(layoutInflater)
    }

    private lateinit var sdf: SimpleDateFormat

    private lateinit var title: String
    private lateinit var category: String
    private var formattedDate: String? = null
    private var date by Delegates.notNull<Long>()

    private val eventDatabase by lazy {
        EventDatabaseSingle.getDatabase(requireContext())
    }

    private val disposable by lazy {
        CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sdf = SimpleDateFormat("MM/dd/yyyy")
        binding.eventCalendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { calendarView, i, i2, i3 ->
            val month = i2 + 1
            formattedDate = if (month <= 9) {
                if (i3 <= 9) {
                    "0$month/0$i3/$i"
                } else {
                    "0$month/$i3/$i"
                }
            } else {
                "$month/$i3/$i"
            }
        }).let {
            formattedDate = sdf.format(binding.eventCalendar.date)
        }

        binding.backBtn.setOnClickListener {
            navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance())
        }

        binding.doneButton.setOnClickListener {
            if (binding.titleField.text.isNotEmpty() && binding.categoryField.text.isNotEmpty()) {
                title = binding.titleField.text.toString()
                category = binding.categoryField.text.toString()

                formattedDate?.let {
                    val event = Event(title, category, formattedDate!!)
                    MySingleton.addEvent(event)

                    eventDatabase.eventDao().insertEvent(event)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d("SecondFragment", "Event inserted $event")
                        }, {
                            Log.e("SecondFragment", "Event not inserted $event")
                        }).apply {
                            disposable.add(this)
                        }
                }

                navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance())
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

        date = if (!formattedDate.isNullOrEmpty()) {
            sdf.parse(formattedDate).time
        } else {
            binding.eventCalendar.date
        }
        outState.putLong("date", date)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val tempDate: Long

        savedInstanceState?.let {
            tempDate = it.getLong("date")
            binding.eventCalendar.date = tempDate
            formattedDate = sdf.format(tempDate)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    companion object {
        fun newInstance() = SecondFragment()
    }
}

