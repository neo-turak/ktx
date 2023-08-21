package com.github.neoturak.view.picker

import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.neoturak.ktkit.R
import com.github.neoturak.view.picker.NumberPicker.DividerType
import com.github.neoturak.view.picker.NumberPicker.Order
import java.util.Calendar
import java.util.Locale

/**
 * 时分秒 Hour/Minute/Second
 */
class TimePicker
@JvmOverloads
constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BasePicker(context, attrs, defStyle) {
    private val mHourNPicker: NumberPicker
    private val mMinuteNPicker: NumberPicker
    private var mIsEnabled = DEFAULT_ENABLED_STATE
    private var mIsAutoScroll = DEFAULT_AUTO_SCROLL_STATE
    private var mOnChangedListener: OnChangedListener? = null
    private var mTempCalendar: Calendar? = null
    private var mCurrentLocale: Locale? = null

    var currentHour: Int?
        /**
         * @return The current hour in the range (0-23).
         */
        get() = mHourNPicker.value
        /**
         * Set the current hour.
         */
        set(currentHour) {
            // why was Integer used in the first place?
            if (currentHour == null || currentHour === this.currentHour) {
                return
            }
            mHourNPicker.value = currentHour
            onChanged()
        }
    var currentMinute: Int
        /**
         * @return The current minute.
         */
        get() = mMinuteNPicker.value
        /**
         * Set the current minute (0-59).
         */
        set(cur) {
            if (cur == this.currentMinute) {
                return
            }
            mMinuteNPicker.value = cur
            onChanged()
        }
    init {
        setCurrentLocale(Locale.getDefault())
        LayoutInflater.from(getContext()).inflate(R.layout.time_picker, this)

        // hour
        mHourNPicker = findViewById(R.id.picker_time_hour)
        mHourNPicker.setOnChangedListener { NPicker: NumberPicker?, _: Int, _: Int ->
            updateInputState()
            onChanged()
        }
        mHourNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)

        // minute
        mMinuteNPicker = findViewById(R.id.picker_time_minute)
        mMinuteNPicker.minValue = 0
        mMinuteNPicker.maxValue = 59
        mMinuteNPicker.setOnLongPressUpdateInterval(100)
        mMinuteNPicker.formatter = NumberPicker.getTwoDigitFormatter()
        mMinuteNPicker.setOnChangedListener { NPicker: NumberPicker?, oldVal: Int, newVal: Int ->
            updateInputState()
            val minValue = mMinuteNPicker.minValue
            val maxValue = mMinuteNPicker.maxValue
            if (oldVal == maxValue && newVal == minValue && mIsAutoScroll) {
                val newHour = mHourNPicker.value + 1
                mHourNPicker.value = newHour
            } else if (oldVal == minValue && newVal == maxValue && mIsAutoScroll) {
                val newHour = mHourNPicker.value - 1
                mHourNPicker.value = newHour
            }
            onChanged()
        }
        mMinuteNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)
             // update controls to initial state
        updateHourControl()
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)

        // set to current time
        currentHour = mTempCalendar!![Calendar.HOUR_OF_DAY]
        currentMinute = mTempCalendar!![Calendar.MINUTE]
        if (!isEnabled) {
            isEnabled = false
        }

        // If not explicitly specified this view is important for accessibility.
        if (importantForAccessibility == IMPORTANT_FOR_ACCESSIBILITY_AUTO
        ) {
            importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    override fun isEnabled(): Boolean {
        return mIsEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        if (mIsEnabled == enabled) {
            return
        }
        super.setEnabled(enabled)
        mMinuteNPicker.isEnabled = enabled
        mHourNPicker.isEnabled = enabled
        mIsEnabled = enabled
    }

    /**
     * Sets the automatic scrolling of items in the picker.
     */
    fun setIsAutoScrollState(isAutoScrollState: Boolean) {
        if (mIsAutoScroll == isAutoScrollState) {
            return
        }
        mIsAutoScroll = isAutoScrollState
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setCurrentLocale(newConfig.locale)
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
    private fun setCurrentLocale(locale: Locale) {
        if (locale == mCurrentLocale) {
            return
        }
        mCurrentLocale = locale
        mTempCalendar = Calendar.getInstance(locale)
    }

    fun setOnChangedListener(listener: OnChangedListener?) {
        mOnChangedListener = listener
    }

    override fun getBaseline(): Int {
        return mHourNPicker.baseline
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        onPopulateAccessibilityEvent(event)
        return true
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent) {
        super.onPopulateAccessibilityEvent(event)
        var flags = DateUtils.FORMAT_SHOW_TIME
        flags = flags or DateUtils.FORMAT_24HOUR
        mTempCalendar!![Calendar.HOUR_OF_DAY] = currentHour!!
        mTempCalendar!![Calendar.MINUTE] = currentMinute
        val selectedDateUtterance =
            DateUtils.formatDateTime(context, mTempCalendar!!.timeInMillis, flags)
        event.text.add(selectedDateUtterance)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = TimePicker::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = TimePicker::class.java.name
    }

    private fun updateHourControl() {
        mHourNPicker.minValue = 0
        mHourNPicker.maxValue = 23
        mHourNPicker.formatter = NumberPicker.getTwoDigitFormatter()
    }

    private fun onChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        if (mOnChangedListener != null) {
            mOnChangedListener!!.onChanged(this, currentHour!!, currentMinute)
        }
    }

    private fun updateInputState() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mHourNPicker)) {
                mHourNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            } else if (inputMethodManager.isActive(mMinuteNPicker)) {
                mMinuteNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            }
        }
    }

    interface OnChangedListener {
        fun onChanged(picker: TimePicker?, hourOfDay: Int, minute: Int)
    }

    /**
     * Used to save / restore state of time picker
     */
    private class SavedState : BaseSavedState {
        val hour: Int
        val minute: Int
        val second: Int

        constructor(
            superState: Parcelable?,
            hour: Int,
            minute: Int,
            second: Int
        ) : super(superState) {
            this.hour = hour
            this.minute = minute
            this.second = second
        }

        private constructor(pc: Parcel) : super(pc) {
            hour = pc.readInt()
            minute = pc.readInt()
            second = pc.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(hour)
            dest.writeInt(minute)
            dest.writeInt(second)
        }
    }

    fun setAccessibilityDescriptionEnabled(enabled: Boolean) {
        super.setAccessibilityDescriptionEnabled(
            enabled,
            mHourNPicker,
            mMinuteNPicker,
        )
    }

    fun setDividerColor(@ColorInt color: Int) {
        super.setDividerColor(color, mHourNPicker, mMinuteNPicker)
    }

    fun setDividerColorResource(@ColorRes colorId: Int) {
        super.setDividerColor(
            ContextCompat.getColor(context, colorId),
            mHourNPicker,
            mMinuteNPicker
        )
    }

    fun setDividerDistance(distance: Int) {
        super.setDividerDistance(distance, mHourNPicker, mMinuteNPicker)
    }

    fun setDividerDistanceResource(@DimenRes dimenId: Int) {
        super.setDividerDistanceResource(dimenId, mHourNPicker, mMinuteNPicker)
    }

    fun setDividerType(@DividerType dividerType: Int) {
        super.setDividerType(dividerType, mHourNPicker, mMinuteNPicker)
    }

    fun setDividerThickness(thickness: Int) {
        super.setDividerThickness(thickness, mHourNPicker, mMinuteNPicker)
    }

    fun setDividerThicknessResource(@DimenRes dimenId: Int) {
        super.setDividerThicknessResource(dimenId, mHourNPicker, mMinuteNPicker)
    }

    fun setOrder(@Order order: Int) {
        super.setOrder(order, mHourNPicker, mMinuteNPicker,)
    }

    fun setOrientation(@NumberPicker.Orientation orientation: Int) {
        super.setOrientation(orientation, mHourNPicker, mMinuteNPicker)
    }

    fun setWheelItemCount(count: Int) {
        super.setWheelItemCount(count, mHourNPicker, mMinuteNPicker)
    }

    fun setFormatter(hourFormatter: String?, minuteFormatter: String?, secondFormatter: String?) {
        mHourNPicker.setFormatter(hourFormatter)
        mMinuteNPicker.setFormatter(minuteFormatter)
    }

    fun setFormatter(
        @StringRes hourFormatterId: Int,
        @StringRes minuteFormatterId: Int,
        @StringRes secondFormatterId: Int
    ) {
        mHourNPicker.setFormatter(resources.getString(hourFormatterId))
        mMinuteNPicker.setFormatter(resources.getString(minuteFormatterId))
    }

    fun setFadingEdgeEnabled(fadingEdgeEnabled: Boolean) {
        super.setFadingEdgeEnabled(fadingEdgeEnabled, mHourNPicker, mMinuteNPicker)
    }

    fun setFadingEdgeStrength(strength: Float) {
        super.setFadingEdgeStrength(strength, mHourNPicker, mMinuteNPicker)
    }

    fun setScrollerEnabled(scrollerEnabled: Boolean) {
        super.setScrollerEnabled(scrollerEnabled, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTextAlign(@NumberPicker.Align align: Int) {
        super.setSelectedTextAlign(align, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTextColor(@ColorInt color: Int) {
        super.setSelectedTextColor(color, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTextColorResource(@ColorRes colorId: Int) {
        super.setSelectedTextColorResource(colorId, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTextSize(textSize: Float) {
        super.setSelectedTextSize(textSize, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTextSize(@DimenRes dimenId: Int) {
        super.setSelectedTextSize(
            resources.getDimension(dimenId),
            mHourNPicker,
            mMinuteNPicker,

        )
    }

    fun setSelectedTextStrikeThru(strikeThruText: Boolean) {
        super.setSelectedTextStrikeThru(
            strikeThruText,
            mHourNPicker,
            mMinuteNPicker,
        )
    }

    fun setSelectedTextUnderline(underlineText: Boolean) {
        super.setSelectedTextUnderline(underlineText, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTypeface(typeface: Typeface?) {
        super.setSelectedTypeface(typeface, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTypeface(string: String?, style: Int) {
        super.setSelectedTypeface(string, style, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTypeface(string: String?) {
        super.setSelectedTypeface(
            string,
            Typeface.NORMAL,
            mHourNPicker,
            mMinuteNPicker,
        )
    }

    fun setSelectedTypeface(@StringRes stringId: Int, style: Int) {
        super.setSelectedTypeface(
            resources.getString(stringId),
            style,
            mHourNPicker,
            mMinuteNPicker,
        )
    }

    fun setSelectedTypeface(@StringRes stringId: Int) {
        super.setSelectedTypeface(
            stringId,
            Typeface.NORMAL,
            mHourNPicker,
            mMinuteNPicker,
        )
    }

    fun setTextAlign(@NumberPicker.Align align: Int) {
        super.setTextAlign(align, mHourNPicker, mMinuteNPicker)
    }

    fun setTextColor(@ColorInt color: Int) {
        super.setTextColor(color, mHourNPicker, mMinuteNPicker)
    }

    fun setTextColorResource(@ColorRes colorId: Int) {
        super.setTextColorResource(colorId, mHourNPicker, mMinuteNPicker)
    }

    fun setTextSize(textSize: Float) {
        super.setTextSize(textSize, mHourNPicker, mMinuteNPicker)
    }

    fun setTextSize(@DimenRes dimenId: Int) {
        super.setTextSize(dimenId, mHourNPicker, mMinuteNPicker)
    }

    fun setTextStrikeThru(strikeThruText: Boolean) {
        super.setTextStrikeThru(strikeThruText, mHourNPicker, mMinuteNPicker)
    }

    fun setTextUnderline(underlineText: Boolean) {
        super.setTextUnderline(underlineText, mHourNPicker, mMinuteNPicker)
    }

    fun setTypeface(typeface: Typeface?) {
        super.setTypeface(typeface, mHourNPicker, mMinuteNPicker)
    }

    fun setTypeface(string: String?, style: Int) {
        super.setTypeface(string, style, mHourNPicker, mMinuteNPicker)
    }

    fun setTypeface(string: String?) {
        super.setTypeface(string, mHourNPicker, mMinuteNPicker)
    }

    fun setTypeface(@StringRes stringId: Int, style: Int) {
        super.setTypeface(stringId, style, mHourNPicker, mMinuteNPicker)
    }

    fun setTypeface(@StringRes stringId: Int) {
        super.setTypeface(stringId, mHourNPicker, mMinuteNPicker)
    }

    fun setLineSpacingMultiplier(multiplier: Float) {
        super.setLineSpacingMultiplier(multiplier, mHourNPicker, mMinuteNPicker)
    }

    fun setMaxFlingVelocityCoefficient(coefficient: Int) {
        super.setMaxFlingVelocityCoefficient(
            coefficient,
            mHourNPicker,
            mMinuteNPicker,
        )
    }

    fun setImeOptions(imeOptions: Int) {
        super.setImeOptions(imeOptions, mHourNPicker, mMinuteNPicker)
    }

    fun setItemSpacing(itemSpacing: Int) {
        super.setItemSpacing(itemSpacing, mHourNPicker, mMinuteNPicker)
    }

    companion object {
        private const val DEFAULT_ENABLED_STATE = true
        private const val DEFAULT_AUTO_SCROLL_STATE = true
    }
}