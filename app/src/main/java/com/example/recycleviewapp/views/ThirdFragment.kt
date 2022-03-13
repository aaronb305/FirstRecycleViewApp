package com.example.recycleviewapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recycleviewapp.EventDatabaseSingle
import com.example.recycleviewapp.MySingleton
import com.example.recycleviewapp.databinding.FragmentThirdBinding
import com.example.recycleviewapp.navigate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.properties.Delegates

/**
 * Remove unused code, commented code and unused imports.
 *
 * Avoid duplication code
 */
class ThirdFragment : Fragment() {

    private val binding by lazy {
        FragmentThirdBinding.inflate(layoutInflater)
    }

    private var position by Delegates.notNull<Int>()

    private lateinit var formattedDate: String

    private val disposable by lazy {
        CompositeDisposable()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Here I changed to use fully kotlin
        // arguments variable (Bundle) is already given by the fragment class
        // use safe call plus let to check nullability
        arguments?.let {
            // here we have the bundle object that is coming as no null
            position = it.getInt("position")
        }

        binding.title.setText(MySingleton.event[position].title)
        binding.category.setText(MySingleton.event[position].category)

        val sdf = SimpleDateFormat("MM/dd/yyyy")
        var date = sdf.parse(MySingleton.event[position].date).time
        val localDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val startDate = LocalDate.parse(LocalDate.now().format(localDateFormatter), localDateFormatter)
        var endDate = LocalDate.parse(sdf.format(date), localDateFormatter)
        var daysBetween = ChronoUnit.DAYS.between(startDate, endDate)

        // the setter method is not used in kotlin
        binding.daysUntil.text = "Days until task: $daysBetween"

        binding.calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val month = i2 + 1

            // Here kotlin allows you to assign the if statement to a varable
            formattedDate = if (month <= 9) {
                if (i3 <= 9) {
                    "0$month/0$i3/$i"
                } else {
                    "0$month/$i3/$i"
                }
            } else {
                "$month/$i3/$i"
            }
            date = sdf.parse(formattedDate).time
            endDate = LocalDate.parse(sdf.format(date),localDateFormatter)
            daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
            binding.daysUntil.text = "Days until task: $daysBetween"
        }.let {
            binding.calendar.date = date
        }

        binding.deleteButton.setOnClickListener {
            EventDatabaseSingle.getDatabase(requireContext())
                .eventDao().delete(MySingleton.event[position])
                .subscribeOn(Schedulers.io())
                .subscribe({
                           Log.d("ThirdFragment", "Event deleted at $position")
                }, {
                    Log.e("ThirdFragment", it.localizedMessage)
                })
                .apply {
                    disposable.add(this)
                }

            MySingleton.removeEvent(MySingleton.event[position])

            // We need to avoid repeated code
            // create a private function for it
            //navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance())

            moveToFirstFragment()
        }

        binding.saveButton.setOnClickListener {
            MySingleton.event[position].title = binding.title.text.toString()
            MySingleton.event[position].category = binding.category.text.toString()
            MySingleton.event[position].date = sdf.format(date)
            moveToFirstFragment()
        }

        binding.backButton.setOnClickListener {
            moveToFirstFragment()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun moveToFirstFragment() {
        navigate(supportFragmentManager = requireActivity().supportFragmentManager, FirstFragment.newInstance())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    companion object {
        fun newInstance(position: Int) =
            ThirdFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
    }
}