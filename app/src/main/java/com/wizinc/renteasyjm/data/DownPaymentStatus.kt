package com.wizinc.renteasyjm.data

sealed class DownPaymentStatus(val status: String)  {

    object Paid: DownPaymentStatus("Paid")
    object Pending: DownPaymentStatus("Pending")
    object Cancelled: DownPaymentStatus("Cancelled")
    object Defaulted : DownPaymentStatus("Defaulted")
    object GracePeriod : DownPaymentStatus("GracePeriod")
    object Refunded : DownPaymentStatus("Refunded")
}