package com.sovcom.authCode.presentation.common.extentions

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.sovcom.domain.common.model.CommonBackendFailure
import com.sovcom.domain.common.model.ErrorItem

fun Throwable.logToFirebase(customKey: String? = null) {
    Firebase.crashlytics.log(getFormattedThrowable(customKey = customKey, throwable = this))
    kotlin.runCatching { }
}

fun getFormattedThrowable(customKey: String?, throwable: Throwable): String {
    return """
        ${if (customKey != null) "$customKey:" else ""}
        |message: ${throwable.message},
        |stackTrace: ${throwable.stackTraceToString()}.
    """.trimIndent()
}

fun CommonBackendFailure.getApiPropertyError(property: AuthApiErrorProperty): ErrorItem? {
    return this.apiError?.violations
        ?.find { it.propertyPath == property.property }
}

enum class AuthApiErrorProperty(val property: String) {
    Phone("phone"),
    Code("code"),
}