package com.wizinc.renteasyjm.data

import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong


@Parcelize
data class DownPayment(
    val downPaymentStatus: String = "",
    val totalPrice: Float,
    val rental: List<CartRental>,
    val address: Address,
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val paymentId: Long = nextLong(0,100_000_000_000) + totalPrice.toLong()

): Parcelable {
    constructor(): this("", 0f, emptyList(), Address())
}
