package com.example.recycleviewapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.adapter.MyEventAdapter
import com.example.recycleviewapp.databinding.FragmentSecondBinding
import com.example.recycleviewapp.model.Event
import com.example.recycleviewapp.navigate
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

    private lateinit var title: String
    private lateinit var category: String
    private var date by Delegates.notNull<Long>()
//    private lateinit var date: String
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
        // Inflate the layout for this fragment

        binding.eventCalendar.setOnDateChangeListener { calendarView, i, i2, i3 ->

        }

        binding.doneButton.setOnClickListener {
            if (binding.titleField.text.isNotEmpty() && binding.categoryField.text.isNotEmpty()) {
                title = binding.titleField.text.toString()
                category = binding.categoryField.text.toString()
                date = binding.eventCalendar.date
                var sdf = SimpleDateFormat("MM/dd/yyyy")
                formattedDate = sdf.format(date)
                MySingleton.addEvent(Event(title, category, formattedDate))
                navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance("", ""))
            }
            else {
                val duration = Toast.LENGTH_LONG
                val errorMsg = "Please enter a title and/or category"
                Toast.makeText(requireContext(), errorMsg, duration).show()
            }
        }
        return binding.root
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

