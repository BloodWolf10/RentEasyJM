package com.wizinc.renteasyjm.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizinc.renteasyjm.adapters.PaymentsAdapter
import com.wizinc.renteasyjm.databinding.FragmentPaymentsBinding
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.viewmodel.PaymentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class PaymentsFragment : Fragment() {
    private lateinit var binding: FragmentPaymentsBinding
    val viewModel by viewModels<PaymentsViewModel> ()
    val downpaymentAdapter by lazy { PaymentsAdapter()}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageCloseOrders.setOnClickListener{
            findNavController().navigateUp()
        }

        setupRecyclerView()

        lifecycleScope.launchWhenStarted {
            viewModel.allPayments.collectLatest {
                when(it){

                    is Resource.Loading -> {

                        binding.progressbarAllOrders.visibility = View.VISIBLE

                    }

                    is Resource.Success -> {

                        binding.progressbarAllOrders.visibility = View.GONE
                        downpaymentAdapter.differ.submitList(it.data)
                        if (it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }

                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()

                    }

                    else -> Unit


                }
            }

        }

        downpaymentAdapter.onClick = {
         val action = PaymentsFragmentDirections.actionPaymentsFragmentToPaymentDetailsFragment(it)
            findNavController().navigate(action)
        }


    }

    private fun setupRecyclerView() {
        binding.rvAllOrders.apply {
            adapter = downpaymentAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }
}