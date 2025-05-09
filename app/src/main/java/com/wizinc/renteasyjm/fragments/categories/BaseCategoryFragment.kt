package com.wizinc.renteasyjm.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.BestRentalsAdapter
import com.wizinc.renteasyjm.databinding.FragmentBaseCategoryBinding
import com.wizinc.renteasyjm.util.showBottomNavigationView

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestRentalsAdapter by lazy { BestRentalsAdapter() }
    protected val bestRentalsAdapter: BestRentalsAdapter by lazy { BestRentalsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOfferRv()
        bestRentalsRv()

        bestRentalsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("rental", it) }
            findNavController().navigate(R.id.action_homeFragment_to_rentalDetailsFragment, b)
        }

        bestRentalsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("rental", it) }
            findNavController().navigate(R.id.action_homeFragment_to_rentalDetailsFragment, b)
        }

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!recyclerView.canScrollHorizontally(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{v, _, scrolly, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrolly){
                onBestRentalsPagingRequest()
            }
        })
    }

    fun showOfferLoading(){
        binding.offerRentalsProgressbar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.offerRentalsProgressbar.visibility = View.GONE
    }

    fun showBestRentalsLoading(){
        binding.baseRentalsProgressbar.visibility = View.VISIBLE
    }

    fun hideBestRentalsLoading(){
        binding.baseRentalsProgressbar.visibility = View.GONE
    }

    open fun onOfferPagingRequest(){

    }

    open fun onBestRentalsPagingRequest(){

    }


    private fun setupOfferRv() {
        binding.rvOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), GridLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
        }
    }

    private fun bestRentalsRv() {
        binding.rvBestDeals.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestRentalsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}