package com.sovcom.data.net.auth

import com.sovcom.data.net.auth.model.AuthCodeDataResponse
import com.sovcom.data.net.auth.model.AuthDataResponse
import com.sovcom.data.net.auth.model.AuthRequestBody
import com.sovcom.data.net.auth.model.AuthWithCodeRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("reg/otp")
    suspend fun sendAuthCode(@Body body: AuthRequestBody): AuthCodeDataResponse

    @POST("reg/otpresend")
    suspend fun resendAuthCode(@Body body: AuthRequestBody): AuthCodeDataResponse

    @POST("reg/auth")
    suspend fun authWithCode(@Body body: AuthWithCodeRequestBody): AuthDataResponse
}