package com.wizinc.renteasyjm.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.activities.RentalActivity

fun Fragment.hideBottomNavigationView() {
    val bottomNavigationView = (activity as RentalActivity).findViewById<BottomNavigationView>(
        R.id.bottom_navigation)
    bottomNavigationView.visibility = View.GONE
}


fun Fragment.showBottomNavigationView() {
    val bottomNavigationView = (activity as RentalActivity).findViewById<BottomNavigationView>(
        R.id.bottom_navigation)
    bottomNavigationView.visibility = View.VISIBLE
}