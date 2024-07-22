package com.sovcom.domain

import com.sovcom.domain.auth.AuthCodeParams
import com.sovcom.domain.auth.AuthRemoteDataSource
import com.sovcom.domain.auth.SendOTPCodeUseCaseImpl
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.common.model.ApiError
import com.sovcom.domain.common.model.CommonBackendFailure
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SendOTPCodeUseCaseTest {

    private val mockAuthRemoteDataSource: AuthRemoteDataSource =
        mock(AuthRemoteDataSource::class.java)

    private val sendOTPCodeUseCaseImpl =
        SendOTPCodeUseCaseImpl(mockAuthRemoteDataSource)

    @Test
    fun testExecuteSuccess() = runBlocking {
        val param = AuthCodeParams(PHONE)
        val expectedAuthCodeData = AuthCodeData(60)

        `when`(mockAuthRemoteDataSource.sendAuthCode(param.phone))
            .thenReturn(expectedAuthCodeData)

        val flowResult = sendOTPCodeUseCaseImpl.execute(param).toList()

        assert(flowResult.firstOrNull() == expectedAuthCodeData)
    }

    @Test(expected = CommonBackendFailure::class)
    fun testExecuteServerError() = runBlocking {
        val param = AuthCodeParams(PHONE)
        val expectedApiError = ApiError("extra", 0, "msg", emptyList(), "key", "email")

        `when`(mockAuthRemoteDataSource.sendAuthCode(param.phone))
            .thenAnswer {
                throw CommonBackendFailure(expectedApiError)
            }

        val flowResult = sendOTPCodeUseCaseImpl.execute(param).firstOrNull()
        assertEquals(null, flowResult)
    }

    @Test(expected = Exception::class)
    fun testExecuteFailure() = runBlocking {
        val param = AuthCodeParams(PHONE)

        `when`(mockAuthRemoteDataSource.sendAuthCode(param.phone))
            .thenThrow(Exception(AUTH_ERROR))

        val result = sendOTPCodeUseCaseImpl.execute(param).firstOrNull()
        assertEquals(null, result)
    }

    companion object {
        private val PHONE = "1234567890"
        private val AUTH_ERROR = "Failed to send OTP"
    }
}