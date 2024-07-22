package com.sovcom.authCode.presentation

import com.sovcom.authCode.presentation.common.baseFragment.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainUiState, MainUiEvent>(MainUiState)