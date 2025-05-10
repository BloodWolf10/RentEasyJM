package com.wizinc.renteasyjm.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.BillingRentalsAdapter
import com.wizinc.renteasyjm.databinding.FragmentPaymentDetailBinding
import com.wizinc.renteasyjm.util.VerticalItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentDetailsFragment: Fragment() {

    private lateinit var binding: FragmentPaymentDetailBinding
    private val billingRentalsAdapter by lazy { BillingRentalsAdapter() }
    private val args by navArgs<PaymentDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentDetailBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val payment = args.payment
        setupRecyclerView()

        binding.imageCloseOrder.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.apply {

            tvOrderId.text = "Payment#${payment.paymentId}"

            val stepLabels = listOf(
                view.findViewById<TextView>(R.id.step_label1),
                view.findViewById<TextView>(R.id.step_label2),
                view.findViewById<TextView>(R.id.step_label3)
            )

            val currentStep = 1 // 0-based index

            // Set progress percentage (e.g., 3 steps: 0%, 50%, 100%)
            val progress = ((currentStep + 1) * 100) / stepLabels.size
            stepView.setProgressCompat(progress, true)

            // Update step label colors
            stepLabels.forEachIndexed { index, label ->
                label.setTextColor(
                    if (index == currentStep) ContextCompat.getColor(requireContext(), com.google.firebase.database.collection.R.color.common_google_signin_btn_text_dark_focused)
                    else ContextCompat.getColor(requireContext(), R.color.g_blue100)
                )
            }


            tvFullName.text = payment.address.fullName
            tvAddress.text = "${payment.address.street}, ${payment.address.city}, ${payment.address.parish}"
            tvPhoneNumber.text = payment.address.phone
            tvTotalPrice.text = payment.totalPrice.toString()



        }

        billingRentalsAdapter.differ.submitList(payment.rental)
    }

    private fun setupRecyclerView() {
        binding.rvRentals.apply {
            adapter = billingRentalsAdapter
           layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration())
        }
    }











}
