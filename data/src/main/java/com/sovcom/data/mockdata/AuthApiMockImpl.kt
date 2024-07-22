package com.sovcom.data.mockdata

import com.sovcom.data.net.auth.AuthApi
import com.sovcom.data.net.auth.model.AuthCodeDataResponse
import com.sovcom.data.net.auth.model.AuthDataResponse
import com.sovcom.data.net.auth.model.AuthRequestBody
import com.sovcom.data.net.auth.model.AuthWithCodeRequestBody
import com.sovcom.domain.common.model.ApiError
import com.sovcom.domain.common.model.CommonBackendFailure
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class AuthApiMockImpl @Inject constructor() : AuthApi {
    override suspend fun sendAuthCode(body: AuthRequestBody): AuthCodeDataResponse {
        delay(500)
        // Suppose we get success from previous screen
        return AuthCodeDataResponse()
    }

    override suspend fun resendAuthCode(body: AuthRequestBody): AuthCodeDataResponse {
        delay(500)
        return when (Random.nextBoolean()) {
            true -> AuthCodeDataResponse()
            false -> throw failureException
        }
    }

    override suspend fun authWithCode(body: AuthWithCodeRequestBody): AuthDataResponse {
        delay(500)
        return when (Random.nextBoolean()) {
            true -> AuthDataResponse()
            false -> throw failureException
        }
    }

    private val failureException = CommonBackendFailure(
        ApiError(
            extraMessage = "CommonBackendFailure extraMessage",
            code = 0,
            message = "CommonBackendFailure message",
            violations = emptyList(),
            key = "CommonBackendFailure key",
            email = "CommonBackendFailure email"
        )
    )
}