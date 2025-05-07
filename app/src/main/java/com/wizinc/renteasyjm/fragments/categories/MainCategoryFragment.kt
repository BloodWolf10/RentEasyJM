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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.BestDealsAdapter
import com.wizinc.renteasyjm.adapters.BestRentalAdapter
import com.wizinc.renteasyjm.adapters.SpecialRentalsAdapter
import com.wizinc.renteasyjm.databinding.FragmentMainCategoryBinding
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment() { // Removed the redundant layout ID from the class definition
    private var _binding: FragmentMainCategoryBinding? = null  // Use backing property for binding
    private val binding get() = _binding!! //  NotNull delegate for binding
    private lateinit var specialRentalAdapter: SpecialRentalsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestRentalsAdapter: BestRentalAdapter
    private val viewModel: MainCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCategoryBinding.inflate(inflater, container, false) // Inflate in onCreateView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialRentalsRv()
        setupBestRentalsRv()
        setupBestDealsRv()
        observeViewModel() //Extract observation to its own method.
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.specialRentals.collectLatest { resource -> // Rename it to resource
                    when (resource) {
                        is Resource.Loading -> showLoading()
                        is Resource.Success -> {
                            hideLoading() // Hide loading first
                            if (resource.data.isNullOrEmpty()) {
                                Toast.makeText(requireContext(), "No rentals available", Toast.LENGTH_SHORT).show()
                            }
                            specialRentalAdapter.differ.submitList(resource.data)
                        }
                        is Resource.Error -> {
                            hideLoading()  // Hide loading on error, too
                            Log.e(TAG, resource.message.toString())
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestRentals.collectLatest { resource -> // Rename it to resource
                    when (resource) {
                        is Resource.Loading -> showLoading()
                        is Resource.Success -> {
                            hideLoading() // Hide loading first
                            if (resource.data.isNullOrEmpty()) {
                                Toast.makeText(requireContext(), "No rentals available", Toast.LENGTH_SHORT).show()
                            }
                            bestRentalsAdapter.differ.submitList(resource.data)
                        }
                        is Resource.Error -> {
                            hideLoading()  // Hide loading on error, too
                            Log.e(TAG, resource.message.toString())
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestDealRentals.collectLatest { resource -> // Rename it to resource
                    when (resource) {
                        is Resource.Loading -> showLoading()
                        is Resource.Success -> {
                            hideLoading() // Hide loading first
                            if (resource.data.isNullOrEmpty()) {
                                Toast.makeText(requireContext(), "No rentals available", Toast.LENGTH_SHORT).show()
                            }
                            bestDealsAdapter.differ.submitList(resource.data)
                        }
                        is Resource.Error -> {
                            hideLoading()  // Hide loading on error, too
                            Log.e(TAG, resource.message.toString())
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDeals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun setupBestRentalsRv() {
        bestRentalsAdapter = BestRentalAdapter()
        binding.rvBestRentals.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestRentalsAdapter
        }
    }

    private fun setupSpecialRentalsRv() {
        specialRentalAdapter = SpecialRentalsAdapter()
        binding.rvSpecialRentals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialRentalAdapter
        }
    }
    private fun showLoading() {
        binding.MainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.MainCategoryProgressbar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding to prevent memory leaks
    }
}
