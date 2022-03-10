package com.example.recycleviewapp.views

import android.annotation.SuppressLint
import android.icu.util.TimeUnit
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.R
import com.example.recycleviewapp.adapter.EventViewHolder
import com.example.recycleviewapp.databinding.FragmentThirdBinding
import com.example.recycleviewapp.model.Event
import com.example.recycleviewapp.navigate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ThirdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ThirdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding by lazy {
        FragmentThirdBinding.inflate(layoutInflater)
    }

    private var position by Delegates.notNull<Int>()

    private lateinit var formattedDate: String

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
        val bundle = this.arguments
        if (bundle != null) {
            position = bundle.getInt("position")
            Log.d("****",position.toString())
        }
        binding.title.setText(MySingleton.event[position].title)
        binding.category.setText(MySingleton.event[position].category)
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        var date = sdf.parse(MySingleton.event[position].date).time
        val localDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val startDate = LocalDate.parse(LocalDate.now().format(localDateFormatter), localDateFormatter)
        var endDate = LocalDate.parse(sdf.format(date), localDateFormatter)
        var daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
        binding.daysUntil.setText("Days until task: $daysBetween")

        binding.calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->
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
            date = sdf.parse(formattedDate).time
            endDate = LocalDate.parse(sdf.format(date),localDateFormatter)
            daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
            binding.daysUntil.setText("Days until task: $daysBetween")
        }.let {
            binding.calendar.date = date

        }
        binding.saveButton.setOnClickListener {
            MySingleton.event[position].title = binding.title.text.toString()
            MySingleton.event[position].category = binding.category.text.toString()
            MySingleton.event[position].date = sdf.format(date)
            navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance("", ""))
        }
        binding.backButton.setOnClickListener {
            navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance("", ""))
        }
        // Inflate the layout for this fragment
        return binding.root
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ThirdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}