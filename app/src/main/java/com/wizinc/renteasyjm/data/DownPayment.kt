package com.wizinc.renteasyjm.data

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

data class DownPayment(
    val downPaymentStatus: String = "",
    val totalPrice: Float,
    val rental: List<CartRental>,
    val address: Address,
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val paymentId: Long = nextLong(0,100_000_000_000) + totalPrice.toLong()

){
    constructor(): this("", 0f, emptyList(), Address())
}
