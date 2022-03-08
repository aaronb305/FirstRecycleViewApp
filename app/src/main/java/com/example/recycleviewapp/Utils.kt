package com.example.recycleviewapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun navigate(supportFragmentManager: FragmentManager, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.mainFragContainer, fragment)
        .addToBackStack(fragment.id.toString())
        .commit()
}