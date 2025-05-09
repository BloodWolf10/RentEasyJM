package com.wizinc.renteasyjm.helper

fun Float?.getRentalPrice(price: Float): Float{
    //this --> Percentage

    if(this == null)
        return price
    val remainingPricePercentage = 1f - this
    val priceAfterOffer = remainingPricePercentage * price

    return priceAfterOffer

}