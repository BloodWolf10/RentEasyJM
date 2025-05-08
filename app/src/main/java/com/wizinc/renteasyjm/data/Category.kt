package com.wizinc.renteasyjm.data

sealed class Category(val category: String) {
    object Home: Category("Home")
    object Apartment: Category("Home")
    object Business: Category("bestDeals")
    object Industrial: Category("regular")
}