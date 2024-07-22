package com.sovcom.domain

import com.sovcom.domain.auth.AuthParams
import com.sovcom.domain.auth.AuthRemoteDataSource
import com.sovcom.domain.auth.AuthWithCodeUseCaseImpl
import com.sovcom.domain.auth.model.AuthData
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
class AuthWithCodeUseCaseTest {

    private val mockAuthRemoteDataSource: AuthRemoteDataSource =
        mock(AuthRemoteDataSource::class.java)

    private val authWithCodeUseCaseImpl =
        AuthWithCodeUseCaseImpl(mockAuthRemoteDataSource)

    @Test
    fun testExecuteSuccess() = runBlocking {
        val param = AuthParams(PHONE, CODE)
        val expectedAuthData = AuthData("accessToken", "refreshToken")

        `when`(mockAuthRemoteDataSource.authWithCode(param.phone, param.code))
            .thenReturn(expectedAuthData)

        val flowResult = authWithCodeUseCaseImpl.execute(param).toList()

        assert(flowResult.firstOrNull() == expectedAuthData)
    }

    @Test(expected = CommonBackendFailure::class)
    fun testExecuteServerError() = runBlocking {
        val param = AuthParams(PHONE, CODE)
        val expectedApiError = ApiError("extra", 0, "msg", emptyList(), "key", "email")

        `when`(mockAuthRemoteDataSource.authWithCode(param.phone, param.code))
            .thenAnswer {
                throw CommonBackendFailure(expectedApiError)
            }

        val flowResult = authWithCodeUseCaseImpl.execute(param).firstOrNull()
        assertEquals(null, flowResult)
    }

    @Test(expected = Exception::class)
    fun testExecuteFailure() = runBlocking {
        val param = AuthParams(PHONE, CODE)

        `when`(mockAuthRemoteDataSource.authWithCode(param.phone, param.code))
            .thenThrow(Exception(AUTH_ERROR))

        val result = authWithCodeUseCaseImpl.execute(param).firstOrNull()
        assertEquals(null, result)
    }

    companion object {
        private val PHONE = "1234567890"
        private val CODE = "1234"
        private val AUTH_ERROR = "Authentication failed"
    }
}