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

    init {
        fetchSpecialRentals()
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
}