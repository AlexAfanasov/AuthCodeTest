package com.sovcom.authCode.presentation.common.customViews.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.sovcom.authCode.R
import com.sovcom.authCode.databinding.ToolbarLayoutBinding
import com.sovcom.authCode.presentation.common.extentions.setSafeOnClickListener


class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding: ToolbarLayoutBinding =
        ToolbarLayoutBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.BaseToolbarView, 0, 0
        )

        binding.apply {
            val title = typedArray.getString(R.styleable.BaseToolbarView_titleToolbarText)
            val titleRight = typedArray.getString(R.styleable.BaseToolbarView_rightToolbarText)
            val showTitle =
                typedArray.getBoolean(R.styleable.BaseToolbarView_showToolbarTitle, false)
            val showRightButton =
                typedArray.getBoolean(R.styleable.BaseToolbarView_showRightButton, false)
            val showBackArrow =
                typedArray.getBoolean(R.styleable.BaseToolbarView_showBackArrow, true)
            val backArrowDrawable = typedArray.getDrawable(R.styleable.BaseToolbarView_backDrawable)

            tvTitle.text = title
            tvTitle.isVisible = showTitle
            ivLastRight.isVisible = showRightButton
            ivBackLeft.isVisible = showBackArrow
            if (backArrowDrawable == null) {
                ivBackLeft.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_arrow_left
                    )
                )

            } else
                ivBackLeft.setImageDrawable(backArrowDrawable)
            typedArray.recycle()
        }
    }

    fun setBackLeftVisible(value: Boolean) {
        binding.ivBackLeft.isVisible = value
    }

    fun setButtonRightVisible(value: Boolean) {
        binding.ivLastRight.isVisible = value
    }

    fun setToolbarText(value: String) {
        binding.tvTitle.text = value
    }

    fun setBackLeftListener(function: () -> Unit) =
        binding.ivBackLeft.setSafeOnClickListener { function.invoke() }

    fun setButtonRightListener(function: () -> Unit) =
        binding.ivLastRight.setSafeOnClickListener { function.invoke() }
}