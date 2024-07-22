package com.sovcom.authCode.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sovcom.authCode.R
import com.sovcom.authCode.databinding.ActivityMainBinding
import com.sovcom.authCode.presentation.auth.codesms.CodeSMSArgs
import com.sovcom.authCode.presentation.common.extentions.observeFlow
import com.sovcom.authCode.presentation.common.models.AppEvent
import com.sovcom.authCode.presentation.common.utils.AppSnackBarUtils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.mapNotNull

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()
    }

    private fun observeViewModel() {
        observeFlow(viewModel.appWideEvents.mapNotNull { it.firstOrNull() }) { event ->
            event.handleAppWideEvent()
            viewModel.removeAppWideEvent(event.id)
        }
        observeFlow(viewModel.uiEvents.mapNotNull { it.firstOrNull() }) { event ->
            event.handleEvent()
            viewModel.removeEvent(event.id)
        }
    }

    private fun AppEvent.handleAppWideEvent() {
        val message = when (this) {
            is AppEvent.ErrorMessage -> this.message
            is AppEvent.Unknown -> getString(R.string.general_error_text_unknown)
            is AppEvent.NoInternet -> getString(R.string.general_error_text_no_internet)
        }
        showSnackBar(viewGroup = binding.root, message = message)
    }

    private fun MainUiEvent.handleEvent() {}
}
