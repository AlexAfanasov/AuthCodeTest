package com.sovcom.authCode

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.sovcom.authCode.presentation.auth.codesms.CodeSMSArgs
import com.sovcom.authCode.presentation.auth.codesms.CodeSMSUiEvent
import com.sovcom.authCode.presentation.auth.codesms.CodeSMSUiState
import com.sovcom.authCode.presentation.auth.codesms.CodeSMSViewModel
import com.sovcom.authCode.presentation.common.models.AppEvent
import com.sovcom.domain.auth.AuthCodeParams
import com.sovcom.domain.auth.AuthParams
import com.sovcom.domain.auth.AuthWithCodeUseCase
import com.sovcom.domain.auth.ResendOTPCodeUseCase
import com.sovcom.domain.auth.SendOTPCodeUseCase
import com.sovcom.domain.auth.model.AuthCodeData
import com.sovcom.domain.auth.model.AuthData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class CodeSMSViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockSendOTPCodeUseCase: SendOTPCodeUseCase

    @Mock
    private lateinit var mockResendOTPCodeUseCase: ResendOTPCodeUseCase

    @Mock
    private lateinit var mockAuthWithCodeUseCase: AuthWithCodeUseCase

    private lateinit var viewModel: CodeSMSViewModel

    @Before
    fun setUp() {
        val savedStateHandle =
            SavedStateHandle(mapOf(FRAGMENT_ARGS to CodeSMSArgs(PHONE, ONE_MINUTE_IN_S)))
        viewModel = generateViewModelMocks(savedStateHandle)
    }

    private fun generateViewModelMocks(savedStateHandle: SavedStateHandle): CodeSMSViewModel {
        `when`(mockSendOTPCodeUseCase.invoke(any())).thenReturn(
            flowOf(
                Result.success(
                    AuthCodeData(
                        60
                    )
                )
            )
        )
        `when`(mockResendOTPCodeUseCase.invoke(any())).thenReturn(
            flowOf(
                Result.success(
                    AuthCodeData(
                        60
                    )
                )
            )
        )
        `when`(mockAuthWithCodeUseCase.invoke(any())).thenReturn(
            flowOf(
                Result.success(
                    AuthData(
                        "accessToken",
                        "refreshToken"
                    )
                )
            )
        )
        return CodeSMSViewModel(
            mockSendOTPCodeUseCase,
            mockResendOTPCodeUseCase,
            mockAuthWithCodeUseCase,
            savedStateHandle
        )
    }

    @Test
    fun testRequestOTPCodeSuccess() = runTest {
        val authCodeData = AuthCodeData(60)

        advanceUntilIdle()

        assertEquals(false, viewModel.currentState.isLoading)
        verify(mockSendOTPCodeUseCase).invoke(AuthCodeParams(phone = PHONE))
        assertEquals(
            CodeSMSUiEvent.RearmResendTimer(authCodeData.resendTimerValue),
            viewModel.uiEvents.value.last()
        )
    }

    @Test
    fun testRequestOTPCodeFailure() = runTest {
        val error = Exception("Network error")

        `when`(mockSendOTPCodeUseCase(AuthCodeParams(phone = PHONE)))
            .thenReturn(flowOf(Result.failure(error)))

        viewModel.requestOTPCode(PHONE)
        advanceUntilIdle()

        assertEquals(false, viewModel.currentState.isLoading)
        assertTrue(viewModel.appWideEvents.value.last() is AppEvent.Unknown)
    }

    @Test
    fun testResendOTPCodeSuccess() = runTest {
        val authCodeData = AuthCodeData(60)

        `when`(mockResendOTPCodeUseCase(AuthCodeParams(phone = PHONE)))
            .thenReturn(flowOf(Result.success(authCodeData)))

        viewModel.resendOTPCode(PHONE)
        advanceUntilIdle()

        assertEquals(false, viewModel.currentState.isLoading)
        verify(mockResendOTPCodeUseCase).invoke(AuthCodeParams(phone = PHONE))
        assertEquals(
            CodeSMSUiEvent.RearmResendTimer(authCodeData.resendTimerValue),
            viewModel.uiEvents.value.last()
        )
    }

    @Test
    fun testResendOTPCodeFailure() = runTest {
        val error = Exception("Network error")

        `when`(mockResendOTPCodeUseCase(AuthCodeParams(phone = PHONE)))
            .thenReturn(flowOf(Result.failure(error)))

        viewModel.resendOTPCode(PHONE)
        advanceUntilIdle()

        assertEquals(false, viewModel.currentState.isLoading)
        assertTrue(viewModel.appWideEvents.value.last() is AppEvent.Unknown)
    }

    @Test
    fun testConfirmSMSCodeAuthSuccess() = runTest {
        val authData = AuthData("accessToken", "refreshToken")

        `when`(mockAuthWithCodeUseCase(AuthParams(phone = PHONE, code = CODE)))
            .thenReturn(flowOf(Result.success(authData)))

        viewModel.confirmSMSCodeAuth(PHONE, CODE)
        advanceUntilIdle()

        assertEquals(false, viewModel.currentState.isLoading)
        verify(mockAuthWithCodeUseCase).invoke(AuthParams(phone = PHONE, code = CODE))
        assertEquals(
            CodeSMSUiEvent.NavigateToMainScreen,
            viewModel.uiEvents.value.last()
        )
    }

    @Test
    fun testConfirmSMSCodeAuthFailure() = runTest {
        `when`(mockAuthWithCodeUseCase(AuthParams(phone = PHONE, code = CODE)))
            .thenReturn(flowOf(Result.failure(failure)))

        viewModel.confirmSMSCodeAuth(PHONE, CODE)
        advanceUntilIdle()

        assertEquals(false, viewModel.currentState.isLoading)
        assertTrue(viewModel.appWideEvents.value.last() is AppEvent.ErrorMessage)
    }

    @Test
    fun testClearUiErrors() {
        val initialState = CodeSMSUiState(errorApiMessage = "Some error")
        viewModel.updateUiState { initialState }

        viewModel.clearUiErrors()
        val updatedState = viewModel.currentState

        assertEquals("", updatedState.errorApiMessage)
    }

    @Test
    fun testHandleBackClick() {
        viewModel.handleBackClick()
        assertEquals(CodeSMSUiEvent.NavigateUp, viewModel.uiEvents.value.last())
    }

    companion object {
        private const val FRAGMENT_ARGS = "code_sms_args"
        private const val ONE_MINUTE_IN_S = 60
        private const val PHONE = "+79999999999"
        private const val CODE = "1234"
    }
}