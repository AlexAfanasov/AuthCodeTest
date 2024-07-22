package com.sovcom.authCode.presentation.auth.codesms

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.badoo.mvicore.modelWatcher
import com.sovcom.authCode.R
import com.sovcom.authCode.databinding.FragmentCodeSmsBinding
import com.sovcom.authCode.presentation.common.baseFragment.BaseFragment
import com.sovcom.authCode.presentation.common.customViews.SmsConfirmationView
import com.sovcom.authCode.presentation.common.customViews.symbolBorderColor
import com.sovcom.authCode.presentation.common.customViews.symbolTextColor
import com.sovcom.authCode.presentation.common.extentions.getColor
import com.sovcom.authCode.presentation.common.extentions.handleBackClick
import com.sovcom.authCode.presentation.common.extentions.hideKeyboardCompat
import com.sovcom.authCode.presentation.common.extentions.setSafeOnClickListener
import com.sovcom.authCode.presentation.common.extentions.setStyledResendCodeText
import com.sovcom.authCode.presentation.common.extentions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class CodeSMSFragment : BaseFragment<FragmentCodeSmsBinding, CodeSMSUiState, CodeSMSUiEvent>() {

    private var resendTimer: CountDownTimer? = null
    override val viewModel: CodeSMSViewModel by viewModels()

    override val stateRenderer = modelWatcher {
        CodeSMSUiState::isLoading { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
        CodeSMSUiState::errorApiMessage { message ->
            with(binding) {
//            Uncomment if default message value is not enough
//                tvCodeError.text = message
                val isErrorNotEmpty = message?.isNotEmpty() == true
                tvCodeError.isVisible = isErrorNotEmpty
                smsCodeView.symbolTextColor =
                    if (isErrorNotEmpty) getColor(R.color.error_100)
                    else getColor(R.color.black_solid_100)
                smsCodeView.symbolBorderColor =
                    if (isErrorNotEmpty) getColor(R.color.error_100)
                    else getColor(R.color.black_solid_100)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        handleBackClick { viewModel.handleBackClick() }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCodeSmsBinding {
        return FragmentCodeSmsBinding.inflate(inflater, container, false)
    }

    override fun CodeSMSUiEvent.handleEvent() {
        when (this) {
            CodeSMSUiEvent.NavigateUp -> findNavController().navigateUp()
            CodeSMSUiEvent.NavigateToMainScreen -> {
                showSnackBar(message = getString(R.string.success_auth))
            }

            is CodeSMSUiEvent.RearmResendTimer -> setupRequestCodeTimer(timerValue)
        }
    }

    override fun FragmentCodeSmsBinding.setupViews() {
        val args = viewModel.args
        with(binding) {
            tvResendCodeInstruction.setStyledResendCodeText(
                stringResId = R.string.resend_code_timer_text,
                resendSeconds = args.resendTimerInSecs
            )
            setupRequestCodeTimer(args.resendTimerInSecs)

            tvResendCode.setSafeOnClickListener {
                viewModel.resendOTPCode(args.authCodePhone)
            }

            smsCodeView.onChangeListener =
                SmsConfirmationView.OnChangeListener { code, isComplete ->
                    if (isComplete) {
                        viewModel.confirmSMSCodeAuth(
                            authCodePhone = args.authCodePhone,
                            code = code
                        )
                        smsCodeView.hideKeyboardCompat()
                    }
                    if (code.isNotEmpty()) {
                        viewModel.clearUiErrors()
                    }
                }
        }
    }

    private fun setupRequestCodeTimer(timerValue: Int) {
        with(binding) {
            tvResendCodeInstruction.isVisible = true
            tvResendCode.isVisible = false
            resendTimer = object : CountDownTimer(timerValue * 1_000L, 1_000L) {
                override fun onTick(remaining: Long) {
                    val secondsRemaining = (remaining / 1000f).roundToInt()
                    tvResendCodeInstruction.setStyledResendCodeText(secondsRemaining, R.string.resend_code_timer_text)
                }

                override fun onFinish() {
                    tvResendCodeInstruction.isVisible = false
                    tvResendCode.isVisible = true
                }
            }
            resendTimer?.cancel()
            resendTimer?.start()
        }
    }

    private fun stopResendTimer() {
        resendTimer?.cancel()
        resendTimer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopResendTimer()
    }
}