package com.wizinc.renteasyjm.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.databinding.ActivityRentalBinding
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RentalActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityRentalBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.rentalHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewModel.cartData.collectLatest {
               when(it){
                   is Resource.Success ->{
                       val count = it.data?.size?: 0
                       val bottomNavigation = binding.bottomNavigation
                       bottomNavigation.getOrCreateBadge(R.id.paymentFragment).apply {
                           number = count
                           backgroundColor = resources.getColor(R.color.g_blue)
                       }
                   }else -> Unit
               }
            }
        }

    }

}