package com.sovcom.authCode.presentation.common.extentions

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.color.MaterialColors
import java.util.Locale

fun View.showKeyboardCompat() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboardCompat() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

@ColorInt
internal fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    return MaterialColors.getColor(this, attrRes, Color.BLACK)
}

fun TextView.setDrawableStart(@DrawableRes drawable: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        drawable,
        0,
        0,
        0
    )
}

fun View.setSafeOnClickListener(delayMillis: Long = 1000, onSafeClick: (View) -> Unit) {
    setOnClickListener {
        this.isEnabled = false
        onSafeClick(this)
        postDelayed({ isEnabled = true }, delayMillis)
    }
}

fun TextView.setStyledResendCodeText(resendSeconds: Int, @StringRes stringResId: Int) {
    try {
        val template = context.getString(stringResId)

        val timer = formatSecondsToMMSS(resendSeconds)

        if (!template.contains("%s")) {
            Log.e(
                "setStyledResendCodeText",
                "The template string does not contain a placeholder for the timer."
            )
            return
        }

        val formattedText = String.format(Locale.getDefault(), template, timer)
        val spannableString = SpannableString(formattedText)

        val startIndex = formattedText.indexOf(timer)
        if (startIndex == -1) {
            Log.e("setStyledResendCodeText", "The timer string is not found in the formatted text.")
            return
        }
        val endIndex = startIndex + timer.length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            RelativeSizeSpan(1.2f),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.BLACK),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        this.text = spannableString
    } catch (e: Exception) {
        Log.e("setStyledResendCodeText", "Unexpected error: ${e.message}", e)
    }
}

fun formatSecondsToMMSS(totalSeconds: Int): String {
    return try {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    } catch (e: Exception) {
        Log.e("formatSecondsToMMSS", "Unexpected error: ${e.message}", e)
        "00:00" // Default value in case of an error
    }
}
