package com.wizinc.renteasyjm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.databinding.ActivityRentalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RentalActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityRentalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.rentalHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

    }

}