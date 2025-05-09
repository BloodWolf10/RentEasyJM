package com.wizinc.renteasyjm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.CartRental
import com.wizinc.renteasyjm.firebase.FirebaseCommon
import com.wizinc.renteasyjm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartRental>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdatePropertyInCart(cartRental: CartRental) {
        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("rental.id", cartRental.rental.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {

                        addNewProperty(cartRental)

                    } else {
                        val rental = it.first().toObject(CartRental::class.java)
                        if (rental == cartRental) {

                            //property already added to cart

                            val documentId = it.first().id
                            increaseRentalDuration(documentId, cartRental)


                        } else {
                            // add new property
                            addNewProperty(cartRental)
                        }

                    }

                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }
            }

    }

    private fun addNewProperty(cartRental: CartRental) {
        firebaseCommon.addPropertyToCart(cartRental) { addedRental, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedRental!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }

    }

    private fun increaseRentalDuration(documentId: String, cartRental: CartRental) {

        firebaseCommon.increaseRentalDuration(documentId){ _, e ->

            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartRental))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }

        }

    }



}