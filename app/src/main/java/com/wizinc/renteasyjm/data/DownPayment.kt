package com.wizinc.renteasyjm.data

data class DownPayment(
    val downPaymentStatus: String = "",
    val totalPrice: Float,
    val rental: List<CartRental>,
    val address: Address
){

}
