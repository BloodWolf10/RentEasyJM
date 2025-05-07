package com.wizinc.renteasyjm.fragments.rental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.adapters.HomeViewpagerAdapter
import com.wizinc.renteasyjm.databinding.FragmentHomeBinding
import com.wizinc.renteasyjm.fragments.categories.ApartmentFragment
import com.wizinc.renteasyjm.fragments.categories.BusinessFragment
import com.wizinc.renteasyjm.fragments.categories.IndustrialFragment
import com.wizinc.renteasyjm.fragments.categories.MainCategoryFragment
import com.wizinc.renteasyjm.fragments.categories.OtherFragment
import com.wizinc.renteasyjm.fragments.categories.UnfinishedFragment

class HomeFragment: Fragment(R.layout.fragment_home)  {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            BusinessFragment(),
            ApartmentFragment(),
            IndustrialFragment(),
            OtherFragment(),
            UnfinishedFragment()


        )

        //Cancel the slide for the different tabe
        binding.viewPagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Homes"
                1 -> tab.text = "Apartment"
                2 -> tab.text = "Business"
                3 -> tab.text = "Industrial"
                4 -> tab.text = "Unfinished"
                5 -> tab.text = "Other"
            }


        }.attach()
    }



}