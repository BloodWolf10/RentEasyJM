package com.wizinc.renteasyjm.data

data class User(
    val  firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = ""
){

    constructor(): this("", "", "", "")
    }
