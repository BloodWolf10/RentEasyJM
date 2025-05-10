package com.wizinc.renteasyjm.fragments.rental

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.CartRentalAdapter
import com.wizinc.renteasyjm.databinding.FragmentCartBinding
import com.wizinc.renteasyjm.firebase.FirebaseCommon
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.util.VerticalItemDecoration
import com.wizinc.renteasyjm.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest

class PaymentFragment: Fragment(R.layout.fragment_payment)  {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy {CartRentalAdapter()}
    private val viewModel by activityViewModels<CartViewModel> ()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupCartRv()

        binding.imageCloseCart.setOnClickListener()
        {
            findNavController().navigateUp()
        }

        var totalPrice = 0f

        lifecycleScope.launchWhenStarted {
            viewModel.rentalPrice.collectLatest { price ->
                price?.let {
                    totalPrice =it
                    binding.tvTotalPrice.text = "$ $price"
                }
            }
        }

        cartAdapter.onRentalClick = {
            val b = Bundle().apply { putParcelable("rental", it.rental) }
            findNavController().navigate(R.id.action_paymentFragment_to_rentalDetailsFragment,b)
        }

        cartAdapter.onPlusClick = {

            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASE)

        }

        cartAdapter.onMinusClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.DECREASE)
        }

        binding.buttonCheckout.setOnClickListener {
           val action = PaymentFragmentDirections.actionPaymentFragmentToBillingFragment(totalPrice, cartAdapter.differ.currentList.toTypedArray(),true)
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item from your cart?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes") { dialog, _ ->
                        viewModel.deleteCartRentalItem(it)
                        dialog.dismiss()
                    }

                }

                alertDialog.create()
                alertDialog.show()
            }
        }



        lifecycleScope.launchWhenStarted {
            viewModel.cartData.collectLatest{
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarCart.visibility = View.VISIBLE
                    }

                    is Resource.Success ->{
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherViews()
                        }else{
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }

                    is Resource.Error ->{
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showOtherViews() {
       binding.apply {
           rvCart.visibility = View.VISIBLE
           totalBoxContainer.visibility = View.VISIBLE
           buttonCheckout.visibility = View.VISIBLE
       }
    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCart() {
       binding.apply {
           layoutCartEmpty.visibility = View.GONE
       }
    }

    private fun showEmptyCart() {
        binding.apply{layoutCartEmpty.visibility = View.VISIBLE}
    }

    private fun setupCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}