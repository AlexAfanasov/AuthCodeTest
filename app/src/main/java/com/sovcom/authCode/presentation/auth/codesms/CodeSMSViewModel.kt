package com.sovcom.authCode.presentation.auth.codesms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sovcom.authCode.presentation.common.baseFragment.BaseViewModel
import com.sovcom.authCode.presentation.common.extentions.AuthApiErrorProperty
import com.sovcom.authCode.presentation.common.extentions.getApiPropertyError
import com.sovcom.domain.auth.AuthCodeParams
import com.sovcom.domain.auth.AuthParams
import com.sovcom.domain.auth.AuthWithCodeUseCase
import com.sovcom.domain.auth.ResendOTPCodeUseCase
import com.sovcom.domain.auth.SendOTPCodeUseCase
import com.sovcom.domain.common.Constants.ONE_MINUTE_IN_S
import com.sovcom.domain.common.model.CommonBackendFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CodeSMSViewModel @Inject constructor(
    private val sendOTPCodeUseCase: SendOTPCodeUseCase,
    private val resendOTPCodeUseCase: ResendOTPCodeUseCase,
    private val authWithCodeUseCase: AuthWithCodeUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CodeSMSUiState, CodeSMSUiEvent>(CodeSMSUiState()) {

    internal val args: CodeSMSArgs = savedStateHandle.get<CodeSMSArgs>("code_sms_args")
        ?: CodeSMSArgs(authCodePhone = "+79999999999", resendTimerInSecs = ONE_MINUTE_IN_S)


    init {
        requestOTPCode(args.authCodePhone)
    }

    fun requestOTPCode(authCodePhone: String) {
        sendOTPCodeUseCase(AuthCodeParams(phone = authCodePhone))
            .onStart { updateUiState { copy(isLoading = true) } }
            .onCompletion { updateUiState { copy(isLoading = false) } }
            .onEach { result ->
                result.onFailure {
                    handleError(it)
                }
                result.onSuccess {
                    sendUiEvent(CodeSMSUiEvent.RearmResendTimer(it.resendTimerValue))
                }
            }.launchIn(viewModelScope)
    }

    fun resendOTPCode(authCodePhone: String) {
        resendOTPCodeUseCase(AuthCodeParams(phone = authCodePhone))
            .onStart { updateUiState { copy(isLoading = true) } }
            .onCompletion { updateUiState { copy(isLoading = false) } }
            .onEach { result ->
                result.onFailure {
                    handleError(it)
                }
                result.onSuccess {
                    sendUiEvent(CodeSMSUiEvent.RearmResendTimer(it.resendTimerValue))
                }
            }.launchIn(viewModelScope)
    }

    fun clearUiErrors() {
        updateUiState { copy(errorApiMessage = "") }
    }

    fun confirmSMSCodeAuth(authCodePhone: String, code: String) {
        authWithCodeUseCase(AuthParams(phone = authCodePhone, code = code))
            .onStart { updateUiState { copy(isLoading = true) } }
            .onCompletion { updateUiState { copy(isLoading = false) } }
            .onEach { result ->
                result.onFailure {
                    handleApiError(it)
                }
                result.onSuccess {
                    sendUiEvent(event = CodeSMSUiEvent.NavigateToMainScreen)
                }
            }.launchIn(viewModelScope)
    }

    fun handleBackClick() {
        sendUiEvent(event = CodeSMSUiEvent.NavigateUp)
    }

    private fun handleApiError(throwable: Throwable) {
        if (throwable is CommonBackendFailure) {
            val phoneErrorItem = throwable.getApiPropertyError(AuthApiErrorProperty.Phone)
            val codeErrorItem = throwable.getApiPropertyError(AuthApiErrorProperty.Code)

            when {
                codeErrorItem != null -> {
                    updateUiState { copy(errorApiMessage = codeErrorItem.message) }
                    return
                }

                phoneErrorItem != null -> {
                    updateUiState { copy(errorApiMessage = phoneErrorItem.message) }
                    return
                }

                else -> {
                    updateUiState { copy(errorApiMessage = throwable.apiError?.message) }
                    handleError(throwable)
                    return
                }
            }
        } else {
            handleError(throwable)
        }
    }
}
