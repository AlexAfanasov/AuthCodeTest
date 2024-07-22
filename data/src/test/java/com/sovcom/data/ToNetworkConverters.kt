package com.sovcom.data

import com.sovcom.data.net.auth.model.AuthCodeDataResponse
import com.sovcom.data.net.auth.model.AuthDataResponse
import com.sovcom.data.net.auth.model.AuthRequestBody
import com.sovcom.data.net.common.model.ApiErrorResponse
import com.sovcom.data.net.common.model.ApiSuccessResponse
import com.sovcom.data.net.common.model.ErrorItemResponse
import com.sovcom.domain.auth.AuthCodeParams
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import com.sovcom.domain.common.model.ApiError
import com.sovcom.domain.common.model.ApiSuccess
import com.sovcom.domain.common.model.ErrorItem

@Suppress("TooManyFunctions", "LargeClass")
object ToNetworkConverters {

    fun AuthData.toNetwork() = AuthDataResponse(
        token = accessToken,
        refreshToken = refreshToken
    )

    fun AuthCodeData.toNetwork() = AuthCodeDataResponse(
        resendTimer = resendTimerValue
    )

    fun ApiError.toNetwork() = ApiErrorResponse(
        extraMessage = extraMessage,
        code = code,
        message = message,
        violations = violations.map { it.toNetwork() },
        key = key,
        mail = email
    )

    fun ErrorItem.toNetwork() = ErrorItemResponse(
        propertyPath = propertyPath,
        key = key,
        message = message
    )

    fun ApiSuccess.toNetwork() = ApiSuccessResponse(
        message = message
    )

    fun AuthCodeParams.toNetwork() = AuthRequestBody(
        phone = phone
    )
}