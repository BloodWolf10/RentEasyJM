package com.wizinc.renteasyjm.firebase

import android.util.Log
import androidx.compose.ui.test.isSelected
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wizinc.renteasyjm.data.CartRental

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

) {

    private val cartCollection =
        firestore.collection("user").document(auth.uid!!).collection("cart")



    fun addPropertyToCart(cartRental: CartRental, onResult: (CartRental?, Exception?) -> Unit)
    {
        cartCollection.document().set(cartRental).addOnSuccessListener {
            onResult(cartRental, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }


    fun increaseRentalDuration(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val rental = document.toObject(CartRental::class.java)
            rental?.let { cartRental ->
                val newDuration = cartRental.quantity + 1 // Assuming 'quantity' means rental days
                val updatedRental = cartRental.copy(quantity = newDuration)
                transaction.set(documentRef, updatedRental)
            }
        }.addOnSuccessListener {
            onResult("Rental 30 days duration successfully increased.", null)
        }.addOnFailureListener { e ->
            onResult(null, e)
        }
    }

}