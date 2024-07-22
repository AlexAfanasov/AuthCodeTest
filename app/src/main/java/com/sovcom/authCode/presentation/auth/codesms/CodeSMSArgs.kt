package com.sovcom.authCode.presentation.auth.codesms

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CodeSMSArgs(
    val authCodePhone: String,
    val resendTimerInSecs: Int,
) : Parcelable