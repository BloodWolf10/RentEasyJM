package com.wizinc.renteasyjm.data

data class CartRental(
    val rental: Rental,
    val quantity: Int,
    var isSelected: Boolean = false

){


    constructor(): this(Rental(),1)
}



