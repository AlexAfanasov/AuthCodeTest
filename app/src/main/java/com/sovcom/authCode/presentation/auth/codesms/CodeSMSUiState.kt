package com.sovcom.authCode.presentation.auth.codesms

import com.sovcom.authCode.presentation.common.models.UiEvent
import com.sovcom.authCode.presentation.common.models.UiState

data class CodeSMSUiState(
    val isLoading: Boolean = false,
    val errorApiMessage: String? = null,
) : UiState

sealed class CodeSMSUiEvent : UiEvent() {
    object NavigateUp : CodeSMSUiEvent()
    object NavigateToMainScreen : CodeSMSUiEvent()
    data class RearmResendTimer(val timerValue: Int) : CodeSMSUiEvent()
}