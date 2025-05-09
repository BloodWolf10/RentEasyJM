package com.wizinc.renteasyjm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Rental (
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Float = 0f,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val sizes: String? = null,
    val imageUrls: List<String> = emptyList()
): Parcelable {
    constructor() : this("", "", "", 0f, 0f, "", "", emptyList())
}

