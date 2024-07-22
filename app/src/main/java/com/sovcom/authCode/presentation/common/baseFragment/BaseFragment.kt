package com.sovcom.authCode.presentation.common.baseFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.badoo.mvicore.ModelWatcher
import com.sovcom.authCode.R
import com.sovcom.authCode.presentation.common.extentions.observeFlow
import com.sovcom.authCode.presentation.common.extentions.setStatusBarColor
import com.sovcom.authCode.presentation.common.extentions.setStatusBarContentColor
import com.sovcom.authCode.presentation.common.extentions.showSnackBar
import com.sovcom.authCode.presentation.common.models.AppEvent
import com.sovcom.authCode.presentation.common.models.ScreenMode
import com.sovcom.authCode.presentation.common.models.StatusBarContentAppearanceMode
import com.sovcom.authCode.presentation.common.models.UiEvent
import com.sovcom.authCode.presentation.common.models.UiState
import kotlinx.coroutines.flow.mapNotNull

abstract class BaseFragment<VB : ViewBinding, S : UiState, E : UiEvent> : Fragment() {

    abstract val viewModel: BaseViewModel<S, E>

    private var _binding: VB? = null
    protected val binding get() = _binding as VB

    open val screenMode: ScreenMode = ScreenMode.DEFAULT
    open val statusBarMode: StatusBarContentAppearanceMode = StatusBarContentAppearanceMode.Dark

    abstract val stateRenderer: ModelWatcher<S>

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun VB.setupViews()

    abstract fun E.handleEvent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAppEvents()
        observeUiState()
        setEdgeToEdgeMode()
        updateStatusBarAppearance()
        binding.setupViews()
    }

    private fun observeUiState() {
        observeFlow(viewModel.uiState, stateRenderer::invoke)
        observeFlow(viewModel.uiEvents.mapNotNull { it.firstOrNull() }) { event ->
            event.handleEvent()
            viewModel.removeEvent(event.id)
        }
    }

    private fun observeAppEvents() {
        observeFlow(viewModel.appWideEvents.mapNotNull { it.firstOrNull() }) { event ->
            event.handleAppWideEvent()
            viewModel.removeAppWideEvent(event.id)
        }
    }

    private fun AppEvent.handleAppWideEvent() {
        when (this) {
            is AppEvent.ErrorMessage -> showSnackBar(message = this.message)
            is AppEvent.Unknown -> showSnackBar(message = getString(R.string.general_error_text_unknown))
            is AppEvent.NoInternet -> showSnackBar(message = getString(R.string.general_error_text_no_internet))
        }
    }

    open fun setEdgeToEdgeMode() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.apply {
                when (screenMode) {
                    ScreenMode.DEFAULT -> {
                        setStatusBarColor(R.color.white_100)
                        updatePadding(insets.left, insets.top, insets.right, insets.bottom)
                    }

                    ScreenMode.FULLSCREEN -> {
                        setStatusBarColor(R.color.transparent)
                        updatePadding(insets.left, 0, insets.right, 0)
                    }

                    ScreenMode.UNDER_STATUS_BAR -> {
                        setStatusBarColor(R.color.transparent)
                        updatePadding(insets.left, 0, insets.right, insets.bottom)
                    }

                    ScreenMode.UNDER_NAVIGATION_BAR -> {
                        setStatusBarColor(R.color.white_100)
                        updatePadding(insets.left, insets.top, insets.right, 0)
                    }
                }
            }
            windowInsets
        }
    }

    private fun updateStatusBarAppearance() {
        requireActivity().window.setStatusBarContentColor(statusBarMode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stateRenderer.clear()
        _binding = null
    }
}