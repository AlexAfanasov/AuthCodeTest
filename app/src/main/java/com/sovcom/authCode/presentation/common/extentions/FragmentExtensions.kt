package com.sovcom.authCode.presentation.common.extentions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.sovcom.authCode.presentation.MainActivity
import com.sovcom.authCode.presentation.common.utils.AppSnackBarUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Fragment.observeFlow(flow: Flow<T>, action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(action)
        }
    }
}

fun Fragment.showSnackBar(
    message: String,
    buttonTitle: String? = null,
    @DrawableRes startDrawableId: Int? = null,
    onClick: (() -> Unit)? = null,
    length: Int = Snackbar.LENGTH_LONG,
) {
    val viewGroup = view ?: return
    AppSnackBarUtils.showSnackBar(
        viewGroup = viewGroup,
        message = message,
        buttonText = buttonTitle,
        startDrawableId = startDrawableId,
        onClick = onClick,
        length = length
    )
}

fun Fragment.getColor(@ColorRes colorId: Int) = ContextCompat.getColor(requireContext(), colorId)

// Should be called from onAttach method
fun Fragment.handleBackClick(onBackClick: () -> Unit) {
    requireActivity()
        .onBackPressedDispatcher
        .addCallback(
            /* owner = */ this,
            /* onBackPressedCallback = */ object : OnBackPressedCallback(/* enabled = */ true) {
                override fun handleOnBackPressed(): Unit = onBackClick.invoke()
            }
        )
}

fun Fragment.setStatusBarColor(@ColorRes colorId: Int) {
    requireActivity().window.statusBarColor = getColor(colorId)
}