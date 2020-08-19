package com.minaseinori.minasemusic.logic.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("password_again") val passwordAgain: String
)