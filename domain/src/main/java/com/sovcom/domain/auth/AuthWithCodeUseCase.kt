package com.sovcom.domain.auth

import com.sovcom.domain.FlowUseCase
import com.sovcom.domain.auth.model.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface AuthWithCodeUseCase : FlowUseCase<AuthParams, AuthData>

class AuthWithCodeUseCaseImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthWithCodeUseCase {
    override fun execute(param: AuthParams): Flow<AuthData> = flow {
        emit(authRemoteDataSource.authWithCode(phone = param.phone, code = param.code))
    }
}

data class AuthParams(
    val phone: String,
    val code: String,
)
