package com.wizinc.renteasyjm.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.CartRental
import com.wizinc.renteasyjm.firebase.FirebaseCommon
import com.wizinc.renteasyjm.helper.getRentalPrice
import com.wizinc.renteasyjm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _cartData = MutableStateFlow<Resource<List<CartRental>>>(Resource.Unspecified())
    val cartData = _cartData.asStateFlow()



    private var cartRentalData = emptyList<DocumentSnapshot>()

    val rentalPrice = cartData.map{
        when(it){
            is Resource.Success ->{
                calculateRentalPrice(it.data!!)
            }
            else -> null

        }
    }


    private val _deleteDialog = MutableSharedFlow<CartRental>()
    val deleteDialog = _deleteDialog.asSharedFlow()


  fun deleteCartRentalItem(cartRental: CartRental){

      val index = cartData.value.data?.indexOf(cartRental)
      val documentId = cartRentalData[index!!].id

      firestore.collection("user").document(auth.uid!!).collection("cart").document(documentId).delete()

  }



    private fun calculateRentalPrice(data: List<CartRental>): Float? {

        return data.sumByDouble { cartRental ->
            (cartRental.rental.offerPercentage.getRentalPrice(cartRental.rental.price) * cartRental.quantity).toDouble()
        }.toFloat()

    }


    init {
        getCartRentalData()
    }

    private fun getCartRentalData() {
        viewModelScope.launch { _cartData.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch { _cartData.emit(Resource.Error(error?.message.toString())) }
                } else {
                    cartRentalData = value.documents
                    val cartRentals = value.toObjects(CartRental::class.java)
                    viewModelScope.launch { _cartData.emit(Resource.Success(cartRentals)) }
                }


            }


    }

    @SuppressLint("SuspiciousIndentation")
    fun changeQuantity(
        cartRental: CartRental,
        quantityChanging: FirebaseCommon.QuantityChanging
    ) {


        val index = cartData.value.data?.indexOf(cartRental)

        /**
         * Index could be equal to 1 or -1 if the function getCartRentalData gets deleayed leaving the result to expect _cartData
         * and prevent the app from crashing
         ***/

        if(index != null && index != -1){
        val documentId = cartRentalData[index!!].id

            when (quantityChanging) {
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartData.emit(Resource.Loading()) }
                    increaseRentalDuration(documentId)
            }

                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if (cartRental.quantity == 1) {
                        viewModelScope.launch { _deleteDialog.emit(cartRental) }
                        return
                    }
                    viewModelScope.launch { _cartData.emit(Resource.Loading()) }
                    decreaseRentalDuration(documentId)
                }
            }
        }

    }

    private fun decreaseRentalDuration(documentId: String) {

        firebaseCommon.decreaseRentalDuration(documentId) { result, e ->

            if(e != null){
                viewModelScope.launch { _cartData.emit(Resource.Error(e.message.toString())) }
            }

    }

}

    private fun increaseRentalDuration(documentId: String) {

        firebaseCommon.increaseRentalDuration(documentId) { result, e ->

            if(e != null){
                viewModelScope.launch { _cartData.emit(Resource.Error(e.message.toString())) }
            }

        }
    }


    }
