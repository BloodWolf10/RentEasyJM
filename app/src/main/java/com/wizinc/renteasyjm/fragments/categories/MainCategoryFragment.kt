package com.wizinc.renteasyjm.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.SpecialRentalsAdapter
import com.wizinc.renteasyjm.databinding.FragmentMainCategoryBinding
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialRentalAdapter: SpecialRentalsAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBestDealsRv()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.specialRentals.collectLatest {
                    when (it) {
                        is Resource.Loading -> showLoading()
                        is Resource.Success -> {
                            if (it.data.isNullOrEmpty()) {
                                Toast.makeText(requireContext(), "No rentals available", Toast.LENGTH_SHORT).show()
                            }
                            specialRentalAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                        is Resource.Error -> {
                            hideLoading()
                            Log.e(TAG, it.message.toString())
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun hideLoading() {
        binding.MainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.MainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupBestDealsRv() {
        specialRentalAdapter = SpecialRentalsAdapter()
        binding.rvSpecialRentals.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialRentalAdapter
        }
    }
}
