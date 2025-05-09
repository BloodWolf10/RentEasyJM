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
class DownPaymentViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

):ViewModel() {

    private val _downPayment = MutableStateFlow<Resource<DownPayment>>(Resource.Unspecified())
    val downPayment = _downPayment.asStateFlow()

    fun placeOrder(DownPayment: DownPayment) {
        viewModelScope.launch {
            _downPayment.emit(Resource.Loading())

        }

        firestore.runBatch { batch ->
            //TODO: Add the downpayment into user-orders collection
            //TODO: Add the order into downpayment collection
            //TODO: Delete the cart items

            firestore.collection("user")
                .document(auth.uid!!)
                .collection("downpayment")
                .document()
                .set(DownPayment)

            firestore.collection("downpayment")
                .document()
                .set(DownPayment)


            firestore.collection("user")
                .document(auth.uid!!)
                .collection("cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()

                    }


                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _downPayment.emit(Resource.Success(DownPayment))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _downPayment.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}