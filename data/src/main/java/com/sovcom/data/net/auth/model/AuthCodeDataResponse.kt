package com.sovcom.data.net.auth.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AuthCodeDataResponse(
    @SerialName("resend_timer")
    val resendTimer: Int? = null,
)