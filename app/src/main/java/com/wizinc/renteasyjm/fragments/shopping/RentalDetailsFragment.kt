package com.wizinc.renteasyjm.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.activities.RentalActivity
import com.wizinc.renteasyjm.adapters.ColorsAdapter
import com.wizinc.renteasyjm.adapters.SizesAdapter
import com.wizinc.renteasyjm.adapters.ViewPager2Images
import com.wizinc.renteasyjm.data.CartRental
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.databinding.FragmentRentalDetailsBinding
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.util.hideBottomNavigationView
import com.wizinc.renteasyjm.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RentalDetailsFragment: Fragment() {

    private val args by navArgs<RentalDetailsFragmentArgs>()
    private lateinit var binding: FragmentRentalDetailsBinding
    private val viewpagerAdapter by lazy {ViewPager2Images()}
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentRentalDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rental = args.rental

        setupSizesRv()
        setupColorsRv()
        setupViewpager()

        binding.closeIcon.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddToCart.setOnClickListener {
            viewModel.addUpdatePropertyInCart(CartRental(rental, 1))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonAddToCart.revertAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {
                        binding.buttonAddToCart.stopAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    else -> Unit

                }

            }
        }


        binding.apply {
            tvRentalName.text = rental.name
            tvRentalPrice.text = "$ ${rental.price}"
            tvDescription.text = rental.description
            tvRentalSize.text = "Size: ${rental.sizes}"

//            if (rental.colors.isNullOrEmpty())
//                tvColor.visibility = View.INVISIBLE
//            if (rental.sizes.isNullOrEmpty())
//                tvSize.visibility = View.INVISIBLE
        }

        viewpagerAdapter.differ.submitList(rental.imageUrls)
        //rental.colors?.let { colorsAdapter.differ.submitList(it) }
        // rental.colors?.let { sizesAdapter.differ.submitList(it) }

    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerRentalImages.adapter = viewpagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColor.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}