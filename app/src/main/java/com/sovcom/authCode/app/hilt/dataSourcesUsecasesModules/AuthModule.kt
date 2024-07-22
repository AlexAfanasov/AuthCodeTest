package com.sovcom.authCode.app.hilt.dataSourcesUsecasesModules

import com.sovcom.data.net.auth.AuthRemoteDataSourceImpl
import com.sovcom.domain.auth.AuthRemoteDataSource
import com.sovcom.domain.auth.AuthWithCodeUseCase
import com.sovcom.domain.auth.AuthWithCodeUseCaseImpl
import com.sovcom.domain.auth.ResendOTPCodeUseCase
import com.sovcom.domain.auth.ResendOTPCodeUseCaseImpl
import com.sovcom.domain.auth.SendOTPCodeUseCase
import com.sovcom.domain.auth.SendOTPCodeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    // region data sources
    @Binds
    fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    // endregion

    // region use cases
    @Binds
    fun bindSendOTPCodeUseCase(impl: SendOTPCodeUseCaseImpl): SendOTPCodeUseCase

    @Binds
    fun bindResendOTPCodeUseCase(impl: ResendOTPCodeUseCaseImpl): ResendOTPCodeUseCase

    @Binds
    fun bindAuthWithCodeUseCase(impl: AuthWithCodeUseCaseImpl): AuthWithCodeUseCase
    // endregion
}
