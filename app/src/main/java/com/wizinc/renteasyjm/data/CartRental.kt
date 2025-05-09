package com.wizinc.renteasyjm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartRental(
    val rental: Rental,
    val quantity: Int,
    var isSelected: Boolean = false

): Parcelable {

    constructor(): this(Rental(),1)
}



