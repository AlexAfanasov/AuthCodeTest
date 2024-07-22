package com.sovcom.data.net.auth

import com.sovcom.data.net.auth.model.AuthRequestBody
import com.sovcom.data.net.auth.model.AuthWithCodeRequestBody
import com.sovcom.data.net.common.Mock
import com.sovcom.data.net.common.NetConverters.toDomain
import com.sovcom.domain.auth.AuthRemoteDataSource
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    @Mock private val authApi: AuthApi
) : AuthRemoteDataSource {
    override suspend fun sendAuthCode(phone: String): AuthCodeData {
        return authApi.sendAuthCode(AuthRequestBody(phone = phone)).toDomain()
    }

    override suspend fun resendAuthCode(phone: String): AuthCodeData {
        return authApi.resendAuthCode(AuthRequestBody(phone = phone)).toDomain()
    }

    override suspend fun authWithCode(phone: String, code: String): AuthData {
        return authApi.authWithCode(AuthWithCodeRequestBody(phone = phone, code = code)).toDomain()
    }
}