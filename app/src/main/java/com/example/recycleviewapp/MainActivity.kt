package com.example.recycleviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.recycleviewapp.adapter.MyEventAdapter
import com.example.recycleviewapp.views.FirstFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigate(supportFragmentManager, FirstFragment.newInstance("", ""))
    }

}
