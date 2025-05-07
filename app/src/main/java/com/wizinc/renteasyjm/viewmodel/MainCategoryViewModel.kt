package com.wizinc.renteasyjm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _specialRentals = MutableStateFlow<Resource<List<Rental>>>(Resource.Unspecified())
    val specialRentals: StateFlow<Resource<List<Rental>>> = _specialRentals

    private val _bestDealRentals = MutableStateFlow<Resource<List<Rental>>>(Resource.Unspecified())
    val bestDealRentals: StateFlow<Resource<List<Rental>>> = _bestDealRentals

    private val _bestRentals = MutableStateFlow<Resource<List<Rental>>>(Resource.Unspecified())
    val bestRentals: StateFlow<Resource<List<Rental>>> = _bestRentals

    init {
        fetchSpecialRentals()
        fetchBestDeals()
        fetchBestRentals()
    }

    fun fetchSpecialRentals(){
        viewModelScope.launch {
            _specialRentals.emit(Resource.Loading())
        }
        firestore.collection("properties")
            .whereEqualTo("category", "Home").get().addOnSuccessListener { result ->
                val specialRentalsList = result.toObjects(Rental::class.java)
                viewModelScope.launch {
                    _specialRentals.emit(Resource.Success(specialRentalsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialRentals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestDeals(){
        viewModelScope.launch {
            _bestDealRentals.emit(Resource.Loading())
        }
        firestore.collection("properties")
            .whereEqualTo("category", "Home").get()
            .addOnSuccessListener { result ->
                val bestDealRentals = result.toObjects(Rental::class.java)
                viewModelScope.launch {
                    _bestDealRentals.emit(Resource.Success(bestDealRentals))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestDealRentals.emit(Resource.Error(it.message.toString()))
                    }
                }
    }

    fun fetchBestRentals(){
        viewModelScope.launch {
            _bestRentals.emit(Resource.Loading())
        }
        firestore.collection("properties").get()
            .addOnSuccessListener { result ->
                val bestRentals = result.toObjects(Rental::class.java)
                viewModelScope.launch {
                    _bestRentals.emit(Resource.Success(bestRentals))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestRentals.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}