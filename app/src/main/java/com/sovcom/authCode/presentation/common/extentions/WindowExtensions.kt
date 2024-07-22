package com.sovcom.authCode.presentation.common.extentions

import android.view.Window
import androidx.core.view.WindowCompat
import com.sovcom.authCode.presentation.common.models.StatusBarContentAppearanceMode

fun Window.setStatusBarContentColor(mode: StatusBarContentAppearanceMode) {
    WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = mode()
}