package com.wsdev.trendingmoviesapp.network

data class AuthenticationResponse(
    val success: Boolean,
    val status_code: Int?,
    val status_message: String?
)