package com.wizinc.renteasyjm.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.databinding.ActivityRentalBinding

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