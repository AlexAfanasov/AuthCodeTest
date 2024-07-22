package com.sovcom.authCode.presentation.common.customViews.smsRetriever

internal object SmsParser {

    fun parseOneTimeCode(message: String, codeLength: Int): String? {
        val regex = "\\d{$codeLength}".toRegex()
        return regex.find(message)?.value
    }
}