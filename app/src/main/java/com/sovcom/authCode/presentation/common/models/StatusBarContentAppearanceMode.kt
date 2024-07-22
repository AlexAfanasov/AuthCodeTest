package com.sovcom.authCode.presentation.common.models

enum class StatusBarContentAppearanceMode {
    Dark,
    LIGHT;

    operator fun invoke() = when (this) {
        Dark -> true
        LIGHT -> false
    }
}