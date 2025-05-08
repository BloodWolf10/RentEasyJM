package com.wizinc.renteasyjm.fragments.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.Category
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.viewmodel.CategoryViewModel
import com.wizinc.renteasyjm.viewmodel.factory.BaseCategoryViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ApartmentFragment :BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Apartment)
    }
    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.offerRentals.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        showOfferLoading()
                    }
                    is Resource.Success ->{
                        offerAdapter.differ.submitList(it.data)
                        hideOfferLoading()
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hideOfferLoading()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestRentals.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        showBestRentalsLoading()
                    }
                    is Resource.Success ->{
                        bestRentalsAdapter.differ.submitList(it.data)
                        hideBestRentalsLoading()                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hideBestRentalsLoading()
                    }
                    else -> Unit
                }
            }
        }
    }
}