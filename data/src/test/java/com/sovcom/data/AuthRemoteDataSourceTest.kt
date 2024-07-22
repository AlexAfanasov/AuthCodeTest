package com.sovcom.data

import com.sovcom.data.ToNetworkConverters.toNetwork
import com.sovcom.data.net.auth.AuthApi
import com.sovcom.data.net.auth.AuthRemoteDataSourceImpl
import com.sovcom.data.net.auth.model.AuthRequestBody
import com.sovcom.data.net.auth.model.AuthWithCodeRequestBody
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class AuthRemoteDataSourceTest {

    @Mock
    private lateinit var mockAuthApi: AuthApi

    private lateinit var authRemoteDataSourceImpl: AuthRemoteDataSourceImpl

    @Before
    fun setup() {
        authRemoteDataSourceImpl = AuthRemoteDataSourceImpl(mockAuthApi)
    }

    @Test
    fun testSendAuthCodeSuccess() = runBlocking {
        val phone = PHONE
        val authCodeData = AuthCodeData(60)

        `when`(mockAuthApi.sendAuthCode(AuthRequestBody(phone = phone)))
            .thenReturn(authCodeData.toNetwork())

        val result = authRemoteDataSourceImpl.sendAuthCode(phone)

        assertEquals(authCodeData, result)
    }

    @Test
    fun testResendAuthCodeSuccess() = runBlocking {
        val phone = PHONE
        val authCodeData = AuthCodeData(60)

        `when`(mockAuthApi.resendAuthCode(AuthRequestBody(phone = phone)))
            .thenReturn(authCodeData.toNetwork())

        val result = authRemoteDataSourceImpl.resendAuthCode(phone)

        assertEquals(authCodeData, result)
    }

    @Test
    fun testAuthWithCodeSuccess() = runBlocking {
        val phone = PHONE
        val code = CODE
        val authData = AuthData("accessToken", "refreshToken")

        `when`(mockAuthApi.authWithCode(AuthWithCodeRequestBody(phone = phone, code = code)))
            .thenReturn(authData.toNetwork())

        val result = authRemoteDataSourceImpl.authWithCode(phone, code)

        assertEquals(authData, result)
    }

    companion object {
        private val PHONE = "1234567890"
        private val CODE = "1234"
    }
}