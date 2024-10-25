package com.sovcom.data.net.common

import com.sovcom.data.net.auth.model.AuthCodeDataResponse
import com.sovcom.data.net.auth.model.AuthDataResponse
import com.sovcom.data.net.common.model.ApiErrorResponse
import com.sovcom.data.net.common.model.ApiSuccessResponse
import com.sovcom.data.net.common.model.ErrorItemResponse
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import com.sovcom.domain.common.Constants.ONE_MINUTE_IN_S
import com.sovcom.domain.common.extentions.orDefault
import com.sovcom.domain.common.model.ApiError
import com.sovcom.domain.common.model.ApiSuccess
import com.sovcom.domain.common.model.ErrorItem

@Suppress("TooManyFunctions", "LargeClass")
object NetConverters {

    fun AuthDataResponse.toDomain() = AuthData(
        accessToken = token.orEmpty(),
        refreshToken = refreshToken.orEmpty()
    )

    fun AuthCodeDataResponse.toDomain() = AuthCodeData(
        resendTimerValue = resendTimer ?: ONE_MINUTE_IN_S
    )

    fun ApiErrorResponse?.toDomain() = ApiError(
        extraMessage = this?.extraMessage.orEmpty(),
        code = this?.code.orDefault(),
        message = this?.message.orEmpty(),
        violations = this?.violations?.map {
            it.toDomain()
        }.orEmpty(),
        key = this?.key.orEmpty(),
        email = this?.mail.orEmpty()
    )

    fun ErrorItemResponse?.toDomain() = ErrorItem(
        propertyPath = this?.propertyPath.orEmpty(),
        key = this?.key.orEmpty(),
        message = this?.message.orEmpty()
    )

    fun ApiSuccessResponse.toDomain() = ApiSuccess(
        message = message.orEmpty()
    )
}