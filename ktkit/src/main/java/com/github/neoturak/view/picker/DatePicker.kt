package com.github.neoturak.view.picker

import android.content.Context
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.neoturak.ktx.R
import com.github.neoturak.view.NumberPicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class DatePicker
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BasePicker(context, attrs, defStyle) {
    private val mNPickers: LinearLayout
    private lateinit var mDayNPicker: NumberPicker
    private lateinit var mMonthNPicker: NumberPicker
    private lateinit var mYearNPicker: NumberPicker
    private var mOnChangedListener: OnChangedListener? = null
    private lateinit var mShortMonths: Array<String?>
    private var mNumberOfMonths = 0
    private var mTempDate: Calendar? = null
    private var mMinDate: Calendar? = null
    private var mMaxDate: Calendar? = null
    private var mCurrentDate: Calendar? = null
    private var mIsEnabled = DEFAULT_ENABLED_STATE
    private var mIsAutoScroll = DEFAULT_AUTO_SCROLL_STATE

    init {
        setCurrentLocale(Locale.CHINESE)
        val startYear = DEFAULT_START_YEAR
        val endYear = DEFAULT_END_YEAR
        val minDate = "1980-01-01"
        val maxDate = "2050-01-01"

        val lParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val linearLayout = LinearLayout(context)
        linearLayout.gravity = Gravity.CENTER
        linearLayout.id = R.id.pickers
        linearLayout.layoutParams = lParams
        linearLayout.orientation = LinearLayout.HORIZONTAL

        val pickerParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        // Day
        val dayPicker = NumberPicker(context)
        dayPicker.id = R.id.day
        dayPicker.layoutParams = pickerParams
        dayPicker.isFocusable = true
        dayPicker.gravity = Gravity.CENTER
        dayPicker.isFocusableInTouchMode = true
        //  dayPicker.setSelectionDividerHeight(1)
        linearLayout.addView(dayPicker)

        // Month
        val monthPicker = NumberPicker(context)
        monthPicker.id = R.id.month
        monthPicker.layoutParams = pickerParams
        monthPicker.isFocusable = true
        monthPicker.gravity = Gravity.CENTER
        monthPicker.isFocusableInTouchMode = true
       // monthPicker.setSelectionDividerHeight(1)
        linearLayout.addView(monthPicker)

        // Year
        val yearPicker = NumberPicker(context)
        yearPicker.id = R.id.year
        yearPicker.layoutParams = pickerParams
        yearPicker.isFocusable = true
        yearPicker.gravity = Gravity.CENTER
        yearPicker.isFocusableInTouchMode = true
      //  yearPicker.setSelectionDividerHeight(1)
        linearLayout.addView(yearPicker)

        addView(linearLayout)
        val onChangeListener: NumberPicker.OnValueChangeListener =
            NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                updateInputState()
                mTempDate!!.timeInMillis = mCurrentDate!!.timeInMillis
                // take care of wrapping of days and months to update greater
                // fields
                if (picker === mDayNPicker) {
                    val maxDayOfMonth = mTempDate!!
                        .getActualMaximum(Calendar.DAY_OF_MONTH)
                    if (oldVal == maxDayOfMonth && newVal == 1 && isAutoScrollState) {
                        mTempDate!!.add(Calendar.DAY_OF_MONTH, 1)
                    } else if (oldVal == 1 && newVal == maxDayOfMonth && isAutoScrollState) {
                        mTempDate!!.add(Calendar.DAY_OF_MONTH, -1)
                    } else {
                        mTempDate!!.add(Calendar.DAY_OF_MONTH, newVal - oldVal)
                    }
                } else if (picker === mMonthNPicker) {
                    if (oldVal == 11 && newVal == 0 && isAutoScrollState) {
                        mTempDate!!.add(Calendar.MONTH, 1)
                    } else if (oldVal == 0 && newVal == 11 && isAutoScrollState) {
                        mTempDate!!.add(Calendar.MONTH, -1)
                    } else {
                        mTempDate!!.add(Calendar.MONTH, newVal - oldVal)
                    }
                } else if (picker === mYearNPicker) {
                    mTempDate!![Calendar.YEAR] = newVal
                } else {
                    throw IllegalArgumentException()
                }
                // now set the date to the adjusted one
                setDate(
                    mTempDate!![Calendar.YEAR],
                    mTempDate!![Calendar.MONTH],
                    mTempDate!![Calendar.DAY_OF_MONTH]
                )
                updateNPickers()
                notifyDateChanged()
            }
        mNPickers = findViewById(R.id.pickers)
        // day
        mDayNPicker = findViewById(R.id.day)
        mDayNPicker.formatter = NumberPicker.getTwoDigitFormatter()
        mDayNPicker.setOnLongPressUpdateInterval(100)
        mDayNPicker.setOnChangedListener(onChangeListener)

        // month
        mMonthNPicker = findViewById(R.id.month)
        mMonthNPicker.minValue = 0
        mMonthNPicker.maxValue = mNumberOfMonths - 1
        mMonthNPicker.displayedValues = mShortMonths

        mMonthNPicker.setOnLongPressUpdateInterval(200)
        mMonthNPicker.setOnChangedListener(onChangeListener)

        // year
        mYearNPicker = findViewById(R.id.year)
        mYearNPicker.setOnLongPressUpdateInterval(100)
        mYearNPicker.setFormatter { "${it}" }
        mYearNPicker.setOnChangedListener(onChangeListener)

        // set the min date giving priority of the minDate over startYear
        mTempDate!!.clear()
        if (!TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mTempDate)) {
                mTempDate!![startYear, 0] = 1
            }
        } else {
            mTempDate!![startYear, 0] = 1
        }
        setMinDate(mTempDate!!.timeInMillis)

        // set the max date giving priority of the maxDate over endYear
        mTempDate!!.clear()
        if (!TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mTempDate)) {
                mTempDate!![endYear, 11] = 31
            }
        } else {
            mTempDate!![endYear, 11] = 31
        }
        setMaxDate(mTempDate!!.timeInMillis)

        // initialize to current date
        mCurrentDate!!.timeInMillis = System.currentTimeMillis()
        init(
            mCurrentDate!![Calendar.YEAR],
            mCurrentDate!![Calendar.MONTH],
            mCurrentDate!![Calendar.DAY_OF_MONTH]
        )

        // re-order the number NPickers to match the current date format
        //reorderNPickers()

        // If not explicitly specified this view is important for accessibility.
        if (importantForAccessibility == IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    /**
     * Sets the minimal date supported by this [NumberPicker] in
     * milliseconds since January 1, 1970 00:00:00 in
     *
     * @param minDate The minimal supported date.
     */
    fun setMinDate(minDate: Long) {
        mTempDate!!.timeInMillis = minDate
        if (mTempDate!![Calendar.YEAR] == mMinDate!![Calendar.YEAR]
            && mTempDate!![Calendar.DAY_OF_YEAR] != mMinDate!![Calendar.DAY_OF_YEAR]
        ) {
            return
        }
        mMinDate!!.timeInMillis = minDate
        if (mCurrentDate!!.before(mMinDate)) {
            mCurrentDate!!.timeInMillis = mMinDate!!.timeInMillis
        }
        updateNPickers()
    }

    /**
     * Sets the maximal date supported by this [DatePicker] in
     *
     * @param maxDate The maximal supported date.
     */
    fun setMaxDate(maxDate: Long) {
        mTempDate!!.timeInMillis = maxDate
        if (mTempDate!![Calendar.YEAR] == mMaxDate!![Calendar.YEAR]
            && mTempDate!![Calendar.DAY_OF_YEAR] != mMaxDate!!
                .get(Calendar.DAY_OF_YEAR)
        ) {
            return
        }
        mMaxDate!!.timeInMillis = maxDate
        if (mCurrentDate!!.after(mMaxDate)) {
            mCurrentDate!!.timeInMillis = mMaxDate!!.timeInMillis
        }
        updateNPickers()
    }

    override fun isEnabled(): Boolean {
        return mIsEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        if (mIsEnabled == enabled) {
            return
        }
        super.setEnabled(enabled)
        mDayNPicker.isEnabled = enabled
        mMonthNPicker.isEnabled = enabled
        mYearNPicker.isEnabled = enabled
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

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        onPopulateAccessibilityEvent(event)
        return true
    }

    override fun onPopulateAccessibilityEvent(event: AccessibilityEvent) {
        super.onPopulateAccessibilityEvent(event)
        val flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        val selectedDateUtterance =
            DateUtils.formatDateTime(context, mCurrentDate!!.timeInMillis, flags)
        event.text.add(selectedDateUtterance)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = DatePicker::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = DatePicker::class.java.name
    }


    /**
     *
     * @param shown True if the calendar view is to be shown.
     */
    fun setDayViewShown(shown: Boolean) {
        mDayNPicker.visibility = if (shown) VISIBLE else GONE
    }

    /**
     * Gets whether the NPickers are shown.
     *
     * @return True if the NPickers are shown.
     */
    fun getSpinnersShown(): Boolean {
        return mNPickers.isShown
    }

    /**
     * Sets whether the NPickers are shown.
     *
     * @param shown True if the NPickers are to be shown.
     */
    fun setSpinnersShown(shown: Boolean) {
        mNPickers.visibility = if (shown) VISIBLE else GONE
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
     fun setCurrentLocale(locale: Locale) {
        mTempDate = getCalendarForLocale(mTempDate, locale)
        mMinDate = getCalendarForLocale(mMinDate, locale)
        mMaxDate = getCalendarForLocale(mMaxDate, locale)
        mCurrentDate = getCalendarForLocale(mCurrentDate, locale)
        mNumberOfMonths = mTempDate!!.getActualMaximum(Calendar.MONTH) + 1
        mShortMonths = arrayOfNulls(mNumberOfMonths)
        for (i in 0 until mNumberOfMonths) {
            mShortMonths[i] = if (i + 1 < 10) "0${i + 1}" else "${i + 1}"
        }
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given
     * calendar.
     *
     * @param oldCalendar The old calendar.
     * @param locale      The locale.
     */
    private fun getCalendarForLocale(oldCalendar: Calendar?, locale: Locale): Calendar {
        return if (oldCalendar == null) {
            Calendar.getInstance(locale)
        } else {
            val currentTimeMillis = oldCalendar.timeInMillis
            val newCalendar = Calendar.getInstance(locale)
            newCalendar.timeInMillis = currentTimeMillis
            newCalendar
        }
    }

    /**
     * Updates the current date.
     *
     * @param year       The year.
     * @param month      The month which is **starting from zero**.
     * @param dayOfMonth The day of the month.
     */
    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        if (!isNewDate(year, month, dayOfMonth)) {
            return
        }
        setDate(year, month, dayOfMonth)
        updateNPickers()
        notifyDateChanged()
    }

    // Override so we are in complete control of save / restore for this widget.
    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, year, month, dayOfMonth)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        setDate(ss.mYear, ss.mMonth, ss.mDay)
        updateNPickers()
    }

    /**
     * Initialize the state. If the provided values designate an inconsistent
     * date the values are normalized before updating the NPickers.
     *
     * @param year        The initial year.
     * @param monthOfYear The initial month **starting from zero**.
     * @param dayOfMonth  The initial day of the month.
     */
    fun init(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        setDate(year, monthOfYear, dayOfMonth)
        updateNPickers()
    }

    /**
     * Parses the given `date` and in case of success sets the result
     * to the `outDate`.
     *
     * @return True if the date was parsed.
     */
    private fun parseDate(date: String, outDate: Calendar?): Boolean {
        return try {
            val mDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            outDate!!.time = Objects.requireNonNull(mDateFormat.parse(date))
            true
        } catch (e: ParseException) {
            Log.w("DatePicker", "Date: $date not in format: MM/dd/yyyy")
            false
        }
    }

    private fun isNewDate(year: Int, month: Int, dayOfMonth: Int): Boolean {
        return mCurrentDate!![Calendar.YEAR] != year || mCurrentDate!![Calendar.MONTH] != dayOfMonth || mCurrentDate!![Calendar.DAY_OF_MONTH] != month
    }

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        mCurrentDate!![year, month] = dayOfMonth
        if (mCurrentDate!!.before(mMinDate)) {
            mCurrentDate!!.timeInMillis = mMinDate!!.timeInMillis
        } else if (mCurrentDate!!.after(mMaxDate)) {
            mCurrentDate!!.timeInMillis = mMaxDate!!.timeInMillis
        }
    }

    private fun updateNPickers() {
        // set the NPicker ranges respecting the min and max dates
        if (mCurrentDate == mMinDate) {
            mDayNPicker.minValue = mCurrentDate!![Calendar.DAY_OF_MONTH]
            mDayNPicker.maxValue = mCurrentDate!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            mDayNPicker.wrapSelectorWheel = false
            mMonthNPicker.displayedValues = null
            mMonthNPicker.minValue = mCurrentDate!![Calendar.MONTH]
            mMonthNPicker.maxValue = mCurrentDate!!.getActualMaximum(Calendar.MONTH)
            mMonthNPicker.wrapSelectorWheel = false
        } else if (mCurrentDate == mMaxDate) {
            mDayNPicker.minValue = mCurrentDate!!.getActualMinimum(Calendar.DAY_OF_MONTH)
            mDayNPicker.maxValue = mCurrentDate!![Calendar.DAY_OF_MONTH]
            mDayNPicker.wrapSelectorWheel = false
            mMonthNPicker.displayedValues = null
            mMonthNPicker.minValue = mCurrentDate!!.getActualMinimum(Calendar.MONTH)
            mMonthNPicker.maxValue = mCurrentDate!![Calendar.MONTH]
            mMonthNPicker.wrapSelectorWheel = false
        } else {
            mDayNPicker.minValue = 1
            mDayNPicker.maxValue = mCurrentDate!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            mDayNPicker.wrapSelectorWheel = true
            mMonthNPicker.displayedValues = null
            mMonthNPicker.minValue = 0
            mMonthNPicker.maxValue = 11
            mMonthNPicker.wrapSelectorWheel = true
        }

        // make sure the month names are a zero based array
        // with the months in the month NPicker
        val displayedValues =
            Arrays.copyOfRange(mShortMonths, mMonthNPicker.minValue, mMonthNPicker.maxValue + 1)
        mMonthNPicker.displayedValues = displayedValues

        // year NPicker range does not change based on the current date
        mYearNPicker.minValue = mMinDate!![Calendar.YEAR]
        mYearNPicker.maxValue = mMaxDate!![Calendar.YEAR]
        mYearNPicker.wrapSelectorWheel = false

        // set the NPicker values
        mYearNPicker.value = mCurrentDate!![Calendar.YEAR]
        mMonthNPicker.value = mCurrentDate!![Calendar.MONTH]
        mDayNPicker.value = mCurrentDate!![Calendar.DAY_OF_MONTH]
    }

    val year: Int
        /**
         * @return The selected year.
         */
        get() = mCurrentDate!![Calendar.YEAR]
    val month: Int
        /**
         * @return The selected month.
         */
        get() = mCurrentDate!![Calendar.MONTH] + 1
    val dayOfMonth: Int
        /**
         * @return The selected day of month.
         */
        get() = mCurrentDate!![Calendar.DAY_OF_MONTH]

    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    private fun notifyDateChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        if (mOnChangedListener != null) {
            mOnChangedListener!!.onChanged(this, year, month + 1, dayOfMonth)
        }
    }

    /**
     * Sets the IME options for a NPicker based on its ordering.
     *
     * @param picker            The NPicker.
     * @param numberPickerCount The total NumberPicker count.
     * @param numberPickerIndex The index of the given NumberPicker.
     */
    private fun setImeOptions(
        picker: NumberPicker,
        numberPickerCount: Int,
        numberPickerIndex: Int
    ) {
        val imeOptions: Int = if (numberPickerIndex < numberPickerCount - 1) {
            EditorInfo.IME_ACTION_NEXT
        } else {
            EditorInfo.IME_ACTION_DONE
        }
        picker.setImeOptions(imeOptions)
    }

    private fun trySetContentDescription(root: View, viewId: Int, contDescResId: Int) {
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
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive(mYearNPicker)) {
            mYearNPicker.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } else if (inputMethodManager.isActive(mMonthNPicker)) {
            mMonthNPicker.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } else if (inputMethodManager.isActive(mDayNPicker)) {
            mDayNPicker.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun setOnChangedListener(listener: OnChangedListener?) {
        mOnChangedListener = listener
    }

    /**
     * The callback used to indicate the user changes\d the date.
     */
    interface OnChangedListener {
        /**
         * Called upon a date change.
         *
         * @param picker      The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility with
         * [Calendar].
         * @param dayOfMonth  The day of the month that was set.
         */
        fun onChanged(picker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int)
    }

    /**
     * Class for managing state storing/restoring.
     */
    private class SavedState : BaseSavedState {
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        /**
         * Constructor called from [DatePicker.onSaveInstanceState]
         */
        constructor(superState: Parcelable?, year: Int, month: Int, day: Int) : super(superState) {
            mYear = year
            mMonth = month
            mDay = day
        }

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(pc: Parcel) : super(pc) {
            mYear = pc.readInt()
            mMonth = pc.readInt()
            mDay = pc.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(mYear)
            dest.writeInt(mMonth)
            dest.writeInt(mDay)
        }

        companion object {
            // suppress unused and hiding
            @JvmField
            val CREATOR: Creator<SavedState> = object : Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    fun setAccessibilityDescriptionEnabled(enabled: Boolean) {
        super.setAccessibilityDescriptionEnabled(enabled, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setDividerColor(@ColorInt color: Int) {
        super.setDividerColor(color, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setDividerColorResource(@ColorRes colorId: Int) {
        super.setDividerColor(
            ContextCompat.getColor(context, colorId),
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker
        )
    }

    fun setDividerDistance(distance: Int) {
        super.setDividerDistance(distance, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setDividerDistanceResource(@DimenRes dimenId: Int) {
        super.setDividerDistanceResource(dimenId, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setDividerType(@NumberPicker.DividerType dividerType: Int) {
        super.setDividerType(dividerType, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setDividerThickness(thickness: Int) {
        super.setDividerThickness(thickness, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setDividerThicknessResource(@DimenRes dimenId: Int) {
        super.setDividerThicknessResource(dimenId, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setOrder(@NumberPicker.Order order: Int) {
        super.setOrder(order, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setOrientation(@NumberPicker.Orientation orientation: Int) {
        super.setOrientation(orientation, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setWheelItemCount(count: Int) {
        super.setWheelItemCount(count, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setFormatter(yearFormatter: String?, monthFormatter: String?, dayFormatter: String?) {
        mYearNPicker.setFormatter(yearFormatter)
        mMonthNPicker.setFormatter(monthFormatter)
        mDayNPicker.setFormatter(dayFormatter)
    }

    fun setFormatter(
        @StringRes yearFormatterId: Int,
        @StringRes monthFormatterId: Int,
        @StringRes dayFormatterId: Int
    ) {
        mYearNPicker.setFormatter(resources.getString(yearFormatterId))
        mMonthNPicker.setFormatter(resources.getString(monthFormatterId))
        mDayNPicker.setFormatter(resources.getString(dayFormatterId))
    }

    fun setFadingEdgeEnabled(fadingEdgeEnabled: Boolean) {
        super.setFadingEdgeEnabled(fadingEdgeEnabled, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setFadingEdgeStrength(strength: Float) {
        super.setFadingEdgeStrength(strength, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setScrollerEnabled(scrollerEnabled: Boolean) {
        super.setScrollerEnabled(scrollerEnabled, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTextAlign(@NumberPicker.Align align: Int) {
        super.setSelectedTextAlign(align, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTextColor(@ColorInt color: Int) {
        super.setSelectedTextColor(color, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTextColorResource(@ColorRes colorId: Int) {
        super.setSelectedTextColorResource(colorId, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTextSize(textSize: Float) {
        super.setSelectedTextSize(textSize, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTextSize(@DimenRes dimenId: Int) {
        super.setSelectedTextSize(
            resources.getDimension(dimenId),
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker
        )
    }

    fun setSelectedTextStrikeThru(strikeThruText: Boolean) {
        super.setSelectedTextStrikeThru(strikeThruText, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTextUnderline(underlineText: Boolean) {
        super.setSelectedTextUnderline(underlineText, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTypeface(typeface: Typeface?) {
        super.setSelectedTypeface(typeface, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTypeface(string: String?, style: Int) {
        super.setSelectedTypeface(string, style, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTypeface(string: String?) {
        super.setSelectedTypeface(string, Typeface.NORMAL, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setSelectedTypeface(@StringRes stringId: Int, style: Int) {
        super.setSelectedTypeface(
            resources.getString(stringId),
            style,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker
        )
    }

    fun setSelectedTypeface(@StringRes stringId: Int) {
        super.setSelectedTypeface(
            stringId,
            Typeface.NORMAL,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker
        )
    }

    fun setTextAlign(@NumberPicker.Align align: Int) {
        super.setTextAlign(align, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTextColor(@ColorInt color: Int) {
        super.setTextColor(color, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTextColorResource(@ColorRes colorId: Int) {
        super.setTextColorResource(colorId, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTextSize(textSize: Float) {
        super.setTextSize(textSize, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTextSize(@DimenRes dimenId: Int) {
        super.setTextSize(dimenId, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTextStrikeThru(strikeThruText: Boolean) {
        super.setTextStrikeThru(strikeThruText, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTextUnderline(underlineText: Boolean) {
        super.setTextUnderline(underlineText, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTypeface(typeface: Typeface?) {
        super.setTypeface(typeface, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTypeface(string: String?, style: Int) {
        super.setTypeface(string, style, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTypeface(string: String?) {
        super.setTypeface(string, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTypeface(@StringRes stringId: Int, style: Int) {
        super.setTypeface(stringId, style, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setTypeface(@StringRes stringId: Int) {
        super.setTypeface(stringId, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setLineSpacingMultiplier(multiplier: Float) {
        super.setLineSpacingMultiplier(multiplier, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setMaxFlingVelocityCoefficient(coefficient: Int) {
        super.setMaxFlingVelocityCoefficient(coefficient, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setImeOptions(imeOptions: Int) {
        super.setImeOptions(imeOptions, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    fun setItemSpacing(itemSpacing: Int) {
        super.setItemSpacing(itemSpacing, mYearNPicker, mMonthNPicker, mDayNPicker)
    }

    companion object {
        private const val DEFAULT_START_YEAR = 1900
        private const val DEFAULT_END_YEAR = 2100
        private const val DEFAULT_ENABLED_STATE = true
        private const val DEFAULT_AUTO_SCROLL_STATE = true
    }
}