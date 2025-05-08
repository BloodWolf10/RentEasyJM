package com.wizinc.renteasyjm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.Category
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModel() {

    private val _offerRentals = MutableStateFlow<Resource<List<Rental>>>(Resource.Unspecified())
    val offerRentals = _offerRentals.asStateFlow()

    private val _bestRentals = MutableStateFlow<Resource<List<Rental>>>(Resource.Unspecified())
    val bestRentals = _bestRentals.asStateFlow()

    init {
        _fetchOfferRentals()
        _fetchBestRentals()
    }


    fun _fetchOfferRentals(){
        viewModelScope.launch {
            _offerRentals.emit(Resource.Loading())
        }
        firestore.collection("properties")
            .whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null).get()
            .addOnSuccessListener { result ->
                val Rentals = result.toObjects(Rental::class.java)
                viewModelScope.launch {
                    _offerRentals.emit(Resource.Success(Rentals))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerRentals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun _fetchBestRentals(){
        viewModelScope.launch {
            _bestRentals.emit(Resource.Loading())
        }
        firestore.collection("properties")
            .whereEqualTo("category", category.category)
            .whereEqualTo("offerPercentage", null).get()
            .addOnSuccessListener { result ->
                val Rentals = result.toObjects(Rental::class.java)
                viewModelScope.launch {
                    _bestRentals.emit(Resource.Success(Rentals))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestRentals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}