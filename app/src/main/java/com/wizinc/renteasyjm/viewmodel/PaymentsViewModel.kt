package com.wizinc.renteasyjm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.DownPayment
import com.wizinc.renteasyjm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel  @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
)
    : ViewModel(){

        private val _allPayments = MutableStateFlow<Resource<List<DownPayment>>>(Resource.Unspecified())
        val allPayments = _allPayments.asStateFlow()

        init {
            getAllPayments()
        }

        fun getAllPayments()
        {
            viewModelScope.launch {
                _allPayments.emit(Resource.Loading())

            }
            firestore.collection("user").document(auth.uid!!).collection("downpayment").get()
                .addOnSuccessListener {
                    val payments = it.toObjects(DownPayment::class.java)
                    viewModelScope.launch {
                        _allPayments.emit(Resource.Success(payments))
                    }


                }.addOnFailureListener {
                    viewModelScope.launch {
                        _allPayments.emit(Resource.Error(it.message.toString()))
                    }
                }

        }
}






