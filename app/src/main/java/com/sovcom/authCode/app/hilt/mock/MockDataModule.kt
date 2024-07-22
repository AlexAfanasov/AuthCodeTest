package com.sovcom.authCode.app.hilt.mock

import com.sovcom.data.mockdata.AuthApiMockImpl
import com.sovcom.data.net.auth.AuthApi
import com.sovcom.data.net.common.Mock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface MockDataModule {

    @Binds
    @Mock
    @Singleton
    fun bindMockAuthApi(impl: AuthApiMockImpl): AuthApi
}
