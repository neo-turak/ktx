package com.github.neoturak.view.picker

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.neoturak.view.picker.NumberPicker.DividerType
import com.github.neoturak.view.picker.NumberPicker.Order

abstract class BasePicker : FrameLayout {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    protected fun setAccessibilityDescriptionEnabled(
        enabled: Boolean,
        vararg pickers: NumberPicker
    ) {
        for (picker in pickers) {
            picker.isAccessibilityDescriptionEnabled = enabled
        }
    }

    protected fun setDividerColor(@ColorInt color: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.dividerColor = color
        }
    }

    protected fun setDividerColorResource(@ColorRes colorId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.dividerColor = ContextCompat.getColor(context, colorId)
        }
    }

    protected fun setDividerDistance(distance: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setDividerDistance(distance)
        }
    }

    protected fun setDividerDistanceResource(@DimenRes dimenId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setDividerDistanceResource(dimenId)
        }
    }

    protected fun setDividerType(@DividerType dividerType: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setDividerType(dividerType)
        }
    }

    protected fun setDividerThickness(thickness: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setDividerThickness(thickness)
        }
    }

    protected fun setDividerThicknessResource(
        @DimenRes dimenId: Int,
        vararg pickers: NumberPicker
    ) {
        for (picker in pickers) {
            picker.setDividerThicknessResource(dimenId)
        }
    }

    protected fun setOrder(@Order order: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.order = order
        }
    }

    protected fun setOrientation(
        @NumberPicker.Orientation orientation: Int,
        vararg pickers: NumberPicker
    ) {
        for (picker in pickers) {
            picker.orientation = orientation
        }
    }

    protected fun setWheelItemCount(count: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.wheelItemCount = count
        }
    }

    protected fun setFadingEdgeEnabled(fadingEdgeEnabled: Boolean, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.isFadingEdgeEnabled = fadingEdgeEnabled
        }
    }

    protected fun setFadingEdgeStrength(strength: Float, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.fadingEdgeStrength = strength
        }
    }

    protected fun setScrollerEnabled(scrollerEnabled: Boolean, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.isScrollerEnabled = scrollerEnabled
        }
    }

    protected fun setSelectedTextAlign(
        @NumberPicker.Align align: Int,
        vararg pickers: NumberPicker
    ) {
        for (picker in pickers) {
            picker.selectedTextAlign = align
        }
    }

    protected fun setSelectedTextColor(@ColorInt color: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.selectedTextColor = color
        }
    }

    protected fun setSelectedTextColorResource(
        @ColorRes colorId: Int,
        vararg pickers: NumberPicker
    ) {
        for (picker in pickers) {
            picker.setSelectedTextColorResource(colorId)
        }
    }

    protected fun setSelectedTextSize(textSize: Float, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.selectedTextSize = textSize
        }
    }

    protected fun setSelectedTextSize(@DimenRes dimenId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.selectedTextSize = resources.getDimension(dimenId)
        }
    }

    protected fun setSelectedTextStrikeThru(strikeThruText: Boolean, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.selectedTextStrikeThru = strikeThruText
        }
    }

    protected fun setSelectedTextUnderline(underlineText: Boolean, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.selectedTextUnderline = underlineText
        }
    }

    protected fun setSelectedTypeface(typeface: Typeface?, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setSelectedTypeface(typeface)
        }
    }

    protected fun setSelectedTypeface(string: String?, style: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setSelectedTypeface(string, style)
        }
    }

    protected fun setSelectedTypeface(string: String?, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setSelectedTypeface(string, Typeface.NORMAL)
        }
    }

    protected fun setSelectedTypeface(
        @StringRes stringId: Int,
        style: Int,
        vararg pickers: NumberPicker
    ) {
        for (picker in pickers) {
            picker.setSelectedTypeface(resources.getString(stringId), style)
        }
    }

    protected fun setSelectedTypeface(@StringRes stringId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setSelectedTypeface(stringId, Typeface.NORMAL)
        }
    }

    protected fun setTextAlign(@NumberPicker.Align align: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.textAlign = align
        }
    }

    protected fun setTextColor(@ColorInt color: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.textColor = color
        }
    }

    protected fun setTextColorResource(@ColorRes colorId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setTextColorResource(colorId)
        }
    }

    protected fun setTextSize(textSize: Float, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.textSize = textSize
        }
    }

    protected fun setTextSize(@DimenRes dimenId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setTextSize(dimenId)
        }
    }

    protected fun setTextStrikeThru(strikeThruText: Boolean, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.textStrikeThru = strikeThruText
        }
    }

    protected fun setTextUnderline(underlineText: Boolean, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.textUnderline = underlineText
        }
    }

    protected fun setTypeface(typeface: Typeface?, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.typeface = typeface
        }
    }

    protected fun setTypeface(string: String?, style: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setTypeface(string, style)
        }
    }

    protected fun setTypeface(string: String?, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setTypeface(string)
        }
    }

    protected fun setTypeface(@StringRes stringId: Int, style: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setTypeface(stringId, style)
        }
    }

    protected fun setTypeface(@StringRes stringId: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setTypeface(stringId)
        }
    }

    protected fun setLineSpacingMultiplier(multiplier: Float, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.lineSpacingMultiplier = multiplier
        }
    }

    protected fun setMaxFlingVelocityCoefficient(coefficient: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.maxFlingVelocityCoefficient = coefficient
        }
    }

    protected fun setImeOptions(imeOptions: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setImeOptions(imeOptions)
        }
    }

    protected fun setItemSpacing(itemSpacing: Int, vararg pickers: NumberPicker) {
        for (picker in pickers) {
            picker.setItemSpacing(itemSpacing)
        }
    }
}