package com.sovcom.authCode.presentation.common.baseFragment

import androidx.lifecycle.ViewModel
import com.sovcom.authCode.presentation.common.models.AppEvent
import com.sovcom.authCode.presentation.common.models.UiEvent
import com.sovcom.authCode.presentation.common.models.UiState
import com.sovcom.domain.common.model.CommonBackendFailure
import com.sovcom.domain.common.model.NoInternetFailure
import com.sovcom.domain.common.model.UnknownFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<S : UiState, E : UiEvent>(initialState: S) : ViewModel() {

    private val _appWideEvents = MutableStateFlow(emptyList<AppEvent>())
    internal val appWideEvents = _appWideEvents.asStateFlow()

    private val _uiState = MutableStateFlow(initialState)
    internal val uiState = _uiState.asStateFlow()

    private val _uiEvents = MutableStateFlow(emptyList<E>())
    internal val uiEvents = _uiEvents.asStateFlow()

    val currentState: S
        get() = _uiState.value

    fun updateUiState(block: S.() -> S) {
        _uiState.update { block(it) }
    }

    fun removeEvent(eventId: String) {
        _uiEvents.update { uiEvents -> uiEvents.filterNot { it.id == eventId } }
    }

    fun sendUiEvent(event: E) {
        _uiEvents.update { it + event }
    }

    fun removeAppWideEvent(eventId: String) {
        _appWideEvents.update { uiEvents -> uiEvents.filterNot { it.id == eventId } }
    }

    fun handleError(throwable: Throwable) {
        val error = when (throwable) {
            is CommonBackendFailure -> AppEvent.ErrorMessage(throwable.apiError?.message ?: "")
            is UnknownFailure -> AppEvent.Unknown
            is NoInternetFailure -> AppEvent.NoInternet
            else -> AppEvent.Unknown
        }
        _appWideEvents.update { it + error }
    }
}