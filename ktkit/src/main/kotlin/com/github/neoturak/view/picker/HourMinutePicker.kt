package com.github.neoturak.view.picker

import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.github.neoturak.ktkit.R
import com.github.neoturak.view.picker.NumberPicker.DividerType
import com.github.neoturak.view.picker.NumberPicker.Order
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

/**
 * 小时和分钟
 */
class HourMinutePicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BasePicker(context, attrs, defStyle) {
    // ui components
    private var mHourNPicker: NumberPicker
    private val mMinuteNPicker: NumberPicker
    private val mAmPmStrings: Array<String>

    // state
    private var mIs24HourView = true
    private var mIsAm = false
    private var mIsEnabled = DEFAULT_ENABLED_STATE
    private var mIsAutoScroll = DEFAULT_AUTO_SCROLL_STATE

    // callbacks
    private var mOnChangedListener: OnChangedListener? = null
    private var mTempCalendar: Calendar? = null
    private var mCurrentLocale: Locale? = null

    private var currentHour: Int?
        /**
         * @return The current hour in the range (0-23).
         */
        get() {
            val currentHour = mHourNPicker.value
            return if (is24HourView()) {
                currentHour
            } else if (mIsAm) {
                currentHour % HOURS_IN_HALF_DAY
            } else {
                currentHour % HOURS_IN_HALF_DAY + HOURS_IN_HALF_DAY
            }
        }

        /**
         * Set the current hour.
         */
        set(currentHour) {
            // why was Integer used in the first place?
            var hour = currentHour
            if (hour == null || hour === this.currentHour) {
                return
            }
            if (!is24HourView()) {
                // convert [0,23] ordinal to wall clock display
                if (hour >= HOURS_IN_HALF_DAY) {
                    mIsAm = false
                    if (hour > HOURS_IN_HALF_DAY) {
                        hour -= HOURS_IN_HALF_DAY
                    }
                } else {
                    mIsAm = true
                    if (hour == 0) {
                        hour = HOURS_IN_HALF_DAY
                    }
                }
            }
            mHourNPicker.value = hour
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
        set(currentMinute) {
            if (currentMinute === this.currentMinute) {
                return
            }
            mMinuteNPicker.value = currentMinute
            onChanged()
        }

    init {
        setCurrentLocale(Locale.getDefault())
        val layoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT
        )
        val childParam = LinearLayoutCompat.LayoutParams(-2,-2)
        val linearLayout = LinearLayoutCompat(context!!)
        linearLayout.layoutParams = layoutParams
        linearLayout.gravity = Gravity.CENTER
        linearLayout.orientation = LinearLayoutCompat.HORIZONTAL

        // Hour NumberPicker
        val hourPicker = NumberPicker(context)
        hourPicker.id = R.id.hour
        hourPicker.layoutParams = childParam
        hourPicker.gravity = Gravity.CENTER
        hourPicker.isFocusable = true
        hourPicker.isFocusableInTouchMode = true
        linearLayout.addView(hourPicker)

        // Minute NumberPicker
        val minutePicker = NumberPicker(context)
        minutePicker.id = R.id.minute
        minutePicker.layoutParams = childParam
        minutePicker.gravity = Gravity.CENTER
        minutePicker.isFocusable = true
        minutePicker.isFocusableInTouchMode = true
        linearLayout.addView(minutePicker)
        addView(linearLayout)

        // hour
        mHourNPicker = findViewById(R.id.hour)
        mHourNPicker.setOnChangedListener { _, oldVal, newVal ->
            updateInputState()
            if (!is24HourView() && isAutoScrollState) {
                if (oldVal == HOURS_IN_HALF_DAY - 1 &&
                    newVal == HOURS_IN_HALF_DAY ||
                    oldVal == HOURS_IN_HALF_DAY &&
                    newVal == HOURS_IN_HALF_DAY - 1) {
                    mIsAm = !mIsAm
                }
            }
            onChanged()
        }

        mHourNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        // minute
        mMinuteNPicker = findViewById(R.id.minute)
        mMinuteNPicker.minValue = 0
        mMinuteNPicker.maxValue = 59
        mMinuteNPicker.setOnLongPressUpdateInterval(100)
        mMinuteNPicker.formatter = NumberPicker.getTwoDigitFormatter()
        mMinuteNPicker.setOnChangedListener { _, oldVal, newVal ->
            updateInputState()
            val minValue = mMinuteNPicker.minValue
            val maxValue = mMinuteNPicker.maxValue
            if (oldVal == maxValue && newVal == minValue && isAutoScrollState) {
                val newHour = mHourNPicker.value + 1
                if (!is24HourView() && newHour == HOURS_IN_HALF_DAY) {
                    mIsAm = !mIsAm
                }
                mHourNPicker.value = newHour
            } else if (oldVal == minValue && newVal == maxValue && isAutoScrollState) {
                val newHour = mHourNPicker.value - 1
                if (!is24HourView()
                    && newHour == HOURS_IN_HALF_DAY - 1
                ) {
                    mIsAm = !mIsAm
                }
                mHourNPicker.value = newHour
            }
            onChanged()
        }
        mMinuteNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)

        /* Get the localized am/pm strings and use them in the NPicker */mAmPmStrings =
            DateFormatSymbols().amPmStrings

        // update controls to initial state
        updateHourControl()
        setOnChangedListener(object : OnChangedListener {
            override fun onChanged(picker: HourMinutePicker?, hourOfDay: Int, minute: Int) {}
        })

        // set to current time
        currentHour = mTempCalendar!![Calendar.HOUR_OF_DAY]
        currentMinute = mTempCalendar!![Calendar.MINUTE]
        if (!isEnabled) {
            isEnabled = false
        }

        // set the content descriptions
        setContentDescriptions()

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

    var isAutoScrollState: Boolean
        get() = mIsAutoScroll
        /**
         * Sets the automatic scrolling of items in the picker.
         */
        set(isAutoScrollState) {
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

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     *
     * @param onChangedListener the callback, should not be null.
     */
    fun setOnChangedListener(onChangedListener: OnChangedListener?) {
        mOnChangedListener = onChangedListener
    }

    /**
     * Set whether in 24 hour or AM/PM mode.
     *
     * @param is24HourView True = 24 hour mode. False = AM/PM.
     */
    fun setIs24HourView(is24HourView: Boolean) {
        if (mIs24HourView == is24HourView) {
            return
        }
        mIs24HourView = is24HourView
        // cache the current hour since NPicker range changes
        val currentHour = currentHour!!
        updateHourControl()
        // set value after NPicker range is updated
        this.currentHour = currentHour
    }

    /**
     * @return true if this is in 24 hour view else false.
     */
    fun is24HourView(): Boolean {
        return mIs24HourView
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
        flags = if (mIs24HourView) {
            flags or DateUtils.FORMAT_24HOUR
        } else {
            flags or DateUtils.FORMAT_12HOUR
        }
        mTempCalendar!![Calendar.HOUR_OF_DAY] = currentHour!!
        mTempCalendar!![Calendar.MINUTE] = currentMinute
        val selectedDateUtterance = DateUtils.formatDateTime(
            context,
            mTempCalendar!!.timeInMillis, flags
        )
        event.text.add(selectedDateUtterance)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = HourMinutePicker::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = HourMinutePicker::class.java.name
    }

    private fun updateHourControl() {
        if (is24HourView()) {
            mHourNPicker.minValue = 0
            mHourNPicker.maxValue = 23
            mHourNPicker.formatter = NumberPicker.getTwoDigitFormatter()
        } else {
            mHourNPicker.minValue = 1
            mHourNPicker.maxValue = 12
            mHourNPicker.setFormatter("")
        }
    }

    private fun onChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        if (mOnChangedListener != null) {
            mOnChangedListener!!.onChanged(
                this, currentHour!!,
                currentMinute
            )
        }
    }

    private fun setContentDescriptions() {
        if (true) return  // This is never reached anyway, backport doesn't have
        // increment/decrement buttons
        // Minute
        trySetContentDescription(
            mMinuteNPicker, R.id.np__increment,
            R.string.time_picker_increment_minute_button
        )
        trySetContentDescription(
            mMinuteNPicker, R.id.np__decrement,
            R.string.time_picker_decrement_minute_button
        )
        // Hour
        trySetContentDescription(
            mHourNPicker, R.id.np__increment,
            R.string.time_picker_increment_hour_button
        )
        trySetContentDescription(
            mHourNPicker, R.id.np__decrement,
            R.string.time_picker_decrement_hour_button
        )
    }

    private fun trySetContentDescription(
        root: View, viewId: Int,
        contDescResId: Int
    ) {
        val target = root.findViewById<View>(viewId)
        if (target != null) {
            target.contentDescription = context.getString(contDescResId)
        }
    }

    private fun updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        // InputMethodManager inputMethodManager =
        // InputMethodManager.peekInstance();
        val inputMethodManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    interface OnChangedListener {
        /**
         * @param picker    The view associated with this listener.
         * @param hourOfDay The current hour.
         * @param minute    The current minute.
         */
        fun onChanged(picker: HourMinutePicker?, hourOfDay: Int, minute: Int)
    }

    /**
     * Used to save / restore state of time picker
     */
    private class SavedState : BaseSavedState {
        val hour: Int
        val minute: Int

        constructor(superState: Parcelable?, hour: Int, minute: Int) : super(superState) {
            this.hour = hour
            this.minute = minute
        }

        private constructor(`in`: Parcel) : super(`in`) {
            hour = `in`.readInt()
            minute = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(hour)
            dest.writeInt(minute)
        }
    }

    fun setAccessibilityDescriptionEnabled(enabled: Boolean) {
        super.setAccessibilityDescriptionEnabled(enabled, mHourNPicker, mMinuteNPicker)
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
        super.setOrder(order, mHourNPicker, mMinuteNPicker)
    }

    fun setOrientation(@NumberPicker.Orientation orientation: Int) {
        super.setOrientation(orientation, mHourNPicker, mMinuteNPicker)
    }

    fun setWheelItemCount(count: Int) {
        super.setWheelItemCount(count, mHourNPicker, mMinuteNPicker)
    }

    fun setFormatter(hourFormatter: String?, minuteFormatter: String?) {
        mHourNPicker.setFormatter(hourFormatter)
        mMinuteNPicker.setFormatter(minuteFormatter)
    }

    fun setFormatter(@StringRes hourFormatterId: Int, @StringRes minuteFormatterId: Int) {
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
        super.setSelectedTextSize(resources.getDimension(dimenId), mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTextStrikeThru(strikeThruText: Boolean) {
        super.setSelectedTextStrikeThru(strikeThruText, mHourNPicker, mMinuteNPicker)
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
        super.setSelectedTypeface(string, Typeface.NORMAL, mHourNPicker, mMinuteNPicker)
    }

    fun setSelectedTypeface(@StringRes stringId: Int, style: Int) {
        super.setSelectedTypeface(
            resources.getString(stringId),
            style,
            mHourNPicker,
            mMinuteNPicker
        )
    }

    fun setSelectedTypeface(@StringRes stringId: Int) {
        super.setSelectedTypeface(stringId, Typeface.NORMAL, mHourNPicker, mMinuteNPicker)
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
        super.setMaxFlingVelocityCoefficient(coefficient, mHourNPicker, mMinuteNPicker)
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
        private const val HOURS_IN_HALF_DAY = 12
    }
}