package com.sovcom.domain.auth

import com.sovcom.domain.FlowUseCase
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface SendOTPCodeUseCase : FlowUseCase<AuthCodeParams, AuthCodeData>

class SendOTPCodeUseCaseImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : SendOTPCodeUseCase {
    override fun execute(param: AuthCodeParams): Flow<AuthCodeData> = flow {
        emit(authRemoteDataSource.sendAuthCode(param.phone))
    }
}

data class AuthCodeParams(
    val phone: String,
)
