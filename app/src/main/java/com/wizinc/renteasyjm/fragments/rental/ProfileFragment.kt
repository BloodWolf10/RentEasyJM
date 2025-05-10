package com.wizinc.renteasyjm.fragments.rental

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.wizinc.renteasyjm.BuildConfig
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.activities.LoginRegisterActivity
import com.wizinc.renteasyjm.databinding.FragmentProfileBinding
import com.wizinc.renteasyjm.util.Resource
import com.wizinc.renteasyjm.util.showBottomNavigationView
import com.wizinc.renteasyjm.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment: Fragment()  {

    private lateinit var binding: FragmentProfileBinding
   private val viewModel by viewModels<ProfileViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.tvAllpayments.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_PaymentsFragment)

        }

        binding.tvBilling.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(0f, emptyArray(),payment = false)
            findNavController().navigate(action)

        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        binding.tvVersion.text = "Version ${BuildConfig.VERSION_CODE}"

        lifecycleScope.launchWhenStarted {

            viewModel.user.collectLatest {
                when(it){
                    is Resource.Loading ->{

                        binding.progressbarSettings.visibility = View.VISIBLE

                    }

                    is Resource.Success ->{

                        binding.progressbarSettings.visibility = View.GONE

                        Glide.with(requireView()).load(it.data!!.imagePath).error(
                            ColorDrawable(Color.BLACK)).into(binding.imageUser)

                        binding.tvUserName.text = it.data.firstName
                    }

                    is Resource.Error ->{

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                    else -> Unit
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }


}