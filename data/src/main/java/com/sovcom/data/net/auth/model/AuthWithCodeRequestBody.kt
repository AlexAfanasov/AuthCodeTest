package com.sovcom.data.net.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthWithCodeRequestBody(
    val phone: String,
    val code: String,
)