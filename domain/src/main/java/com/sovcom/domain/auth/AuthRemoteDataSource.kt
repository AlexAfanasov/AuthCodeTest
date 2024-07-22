package com.sovcom.domain.auth

import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData

interface AuthRemoteDataSource {
    suspend fun sendAuthCode(phone: String): AuthCodeData
    suspend fun resendAuthCode(phone: String): AuthCodeData
    suspend fun authWithCode(phone: String, code: String): AuthData
}