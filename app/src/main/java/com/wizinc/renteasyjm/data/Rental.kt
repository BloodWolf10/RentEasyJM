package com.wizinc.renteasyjm.data

data class Rental (
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Float? = 0f,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val sizes: String? = null,
    val imageUrls: List<String> = emptyList()
)

