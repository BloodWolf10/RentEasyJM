package com.wizinc.renteasyjm.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.AddressAdapter
import com.wizinc.renteasyjm.adapters.BillingRentalsAdapter
import com.wizinc.renteasyjm.data.CartRental
import com.wizinc.renteasyjm.databinding.FragmentBillingBinding
import com.wizinc.renteasyjm.util.HorizantalItemDecoration
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class BillingFragment : Fragment(){
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingRentalsAdapter by lazy { BillingRentalsAdapter() }
    private val  viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var totalPrice = 0f
    private var rentals = emptyList<CartRental>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rentals = args.rental.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupAddressRv()
        setupBillingRentalsRv()

        binding.imageCloseBilling.setOnClickListener() {
            findNavController().navigateUp()
        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it){

                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE
                        addressAdapter.differ.submitList(it.data)
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                       Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit


                }
            }
        }

        billingRentalsAdapter.differ.submitList(rentals)
        binding.tvTotalPrice.text = "$ $totalPrice"
    }

    private fun setupBillingRentalsRv() {
        binding.rvRentals.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingRentalsAdapter
            addItemDecoration(HorizantalItemDecoration())
        }
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizantalItemDecoration())
        }
    }
}