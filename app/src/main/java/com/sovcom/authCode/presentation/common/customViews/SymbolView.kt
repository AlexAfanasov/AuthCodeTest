package com.sovcom.authCode.presentation.common.customViews

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Size
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.google.android.material.R as MaterialR

private const val textPaintAlphaAnimDuration = 25L
private const val borderPaintAlphaAnimDuration = 150L
private const val cursorAlphaAnimDuration = 500L
private const val cursorAlphaAnimStartDelay = 200L
private const val cursorSymbol = "|"

@SuppressLint("ViewConstructor")
class SymbolView(context: Context, private var symbolStyle: Style) : View(context) {

    data class State(
        val symbol: Char? = null,
        val isActive: Boolean = false
    )

    var state: State = State()
        set(value) {
            if (field == value) return
            field = value
            updateState(state)
        }

    private var showCursor: Boolean = symbolStyle.showCursor
    private var desiredW: Int = symbolStyle.width
    private var desiredH: Int = symbolStyle.height
    private var textSizePx: Int = symbolStyle.textSize
    private var cornerRadius: Float = symbolStyle.borderCornerRadius

    private val backgroundPaint: Paint = Paint().apply {
        color = symbolStyle.backgroundColor
        style = Paint.Style.FILL
    }
    private val borderPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = symbolStyle.borderColor
        style = Paint.Style.STROKE
        strokeWidth = symbolStyle.borderWidth.toFloat()
    }
    private val textPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = symbolStyle.textColor
        textSize = textSizePx.toFloat()
        typeface = symbolStyle.typeface
        textAlign = Paint.Align.CENTER
    }

    private var textSize: Size = calculateTextSize('0')

    private val backgroundRect = RectF()
    private val bottomBorderRect = RectF()

    private var textAnimator: Animator? = null

    @Suppress("SameParameterValue")
    private fun calculateTextSize(symbol: Char): Size {
        val textBounds = Rect()
        textPaint.getTextBounds(symbol.toString(), 0, 1, textBounds)
        return Size(textBounds.width(), textBounds.height())
    }

    private fun updateState(state: State) = with(state) {
        textAnimator?.cancel()
        if (symbol == null && isActive && showCursor) {
            textPaint.color = symbolStyle.borderColorActive
            textAnimator = ObjectAnimator.ofInt(textPaint, "alpha", 255, 255, 0, 0)
                .apply {
                    duration = cursorAlphaAnimDuration
                    startDelay = cursorAlphaAnimStartDelay
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                    addUpdateListener { invalidate() }
                }
        } else {
            textPaint.color = symbolStyle.textColor
            val startAlpha = if (symbol == null) 255 else 127
            val endAlpha = if (symbol == null) 0 else 255
            textAnimator = ObjectAnimator.ofInt(textPaint, "alpha", startAlpha, endAlpha)
                .apply {
                    duration = textPaintAlphaAnimDuration
                    addUpdateListener { invalidate() }
                }
        }

        textAnimator?.start()
        animateBorderColorChange(isActive)
    }

    private fun animateBorderColorChange(isActive: Boolean) {
        val borderColor = symbolStyle.borderColor
        val borderColorActive = symbolStyle.borderColorActive
        if (borderColor == borderColorActive) {
            return
        }

        val colorFrom =
            if (isActive) borderColor
            else borderColorActive
        val colorTo =
            if (isActive) borderColorActive
            else borderColor
        ObjectAnimator.ofObject(borderPaint, "color", ArgbEvaluator(), colorFrom, colorTo)
            .apply {
                duration = borderPaintAlphaAnimDuration
                addUpdateListener { invalidate() }
            }
            .start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSizeAndState(desiredW, widthMeasureSpec, 0)
        val h = resolveSizeAndState(desiredH, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val borderWidthHalf = borderPaint.strokeWidth / 2
        backgroundRect.left = borderWidthHalf
        backgroundRect.top = borderWidthHalf
        backgroundRect.right = measuredWidth.toFloat() - borderWidthHalf
        backgroundRect.bottom = measuredHeight.toFloat() - borderWidthHalf
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            backgroundRect,
            cornerRadius,
            cornerRadius,
            backgroundPaint
        )

        val borderWidth = borderPaint.strokeWidth
        bottomBorderRect.set(
            backgroundRect.left,
            backgroundRect.bottom - borderWidth,
            backgroundRect.right,
            backgroundRect.bottom
        )

        canvas.drawRoundRect(
            bottomBorderRect,
            cornerRadius,
            cornerRadius,
            borderPaint
        )

        canvas.drawText(
            if (state.isActive && showCursor) cursorSymbol else state.symbol?.toString() ?: "",
            backgroundRect.width() / 2 + borderPaint.strokeWidth / 2,
            backgroundRect.height() / 2 + textSize.height / 2 + borderPaint.strokeWidth / 2,
            textPaint
        )
    }

    fun updateStyle(newStyle: Style) {
        this.symbolStyle = newStyle
        showCursor = symbolStyle.showCursor
        desiredW = symbolStyle.width
        desiredH = symbolStyle.height
        textSizePx = symbolStyle.textSize
        cornerRadius = symbolStyle.borderCornerRadius

        backgroundPaint.color = symbolStyle.backgroundColor
        borderPaint.color = symbolStyle.borderColor
        borderPaint.strokeWidth = symbolStyle.borderWidth.toFloat()
        textPaint.color = symbolStyle.textColor
        textPaint.textSize = textSizePx.toFloat()
        textPaint.typeface = symbolStyle.typeface

        invalidate()
    }

    data class Style(
        val showCursor: Boolean,
        @Px val width: Int,
        @Px val height: Int,
        @ColorInt val backgroundColor: Int,
        @ColorInt val borderColor: Int,
        @ColorInt val borderColorActive: Int,
        @Px val borderWidth: Int,
        val borderCornerRadius: Float,
        @ColorInt val textColor: Int,
        @Px val textSize: Int,
        val typeface: Typeface = Typeface.DEFAULT_BOLD
    )
}