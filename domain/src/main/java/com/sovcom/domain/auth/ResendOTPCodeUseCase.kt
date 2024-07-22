package com.sovcom.domain.auth

import com.sovcom.domain.FlowUseCase
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ResendOTPCodeUseCase : FlowUseCase<AuthCodeParams, AuthCodeData>

class ResendOTPCodeUseCaseImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : ResendOTPCodeUseCase {
    override fun execute(param: AuthCodeParams): Flow<AuthCodeData> = flow {
        emit(authRemoteDataSource.resendAuthCode(param.phone))
    }
}
