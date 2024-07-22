package com.sovcom.authCode.presentation.common.extentions

internal fun String.digits(): String = filter { char -> char.isDigit() }
