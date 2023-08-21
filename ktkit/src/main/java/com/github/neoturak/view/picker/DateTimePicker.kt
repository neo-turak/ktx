package com.github.neoturak.view.picker

import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.neoturak.ktkit.R
import com.github.neoturak.view.picker.NumberPicker.DividerType
import com.github.neoturak.view.picker.NumberPicker.Order
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Objects

/**
 * 年月日时分秒
 */
class DateTimePicker : BasePicker {
    private lateinit var mMinuteNPicker: NumberPicker
    private lateinit var mHourNPicker: NumberPicker
    private lateinit var mDayNPicker: NumberPicker
    private lateinit var mMonthNPicker: NumberPicker
    private lateinit var mYearNPicker: NumberPicker
    private var mCurrentLocale: Locale = Locale.CHINA
    private var mOnChangedListener: OnChangedListener? = null
    private lateinit var mShortMonths: Array<String?>
    private var mNumberOfMonths = 0
    private var  mTempDate: Calendar = Calendar.getInstance(Locale.CHINA)
    private var mMinDate: Calendar = GregorianCalendar(1980,0,1)
    private var mMaxDate: Calendar = GregorianCalendar(2050,0,1)
    private var mCurrentDate: Calendar = Calendar.getInstance(Locale.CHINA)
    private var mIsEnabled = true
    private var mIsAutoScroll = DEFAULT_AUTO_SCROLL_STATE

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.date_time_picker, this)
        setCurrentLocale(Locale.getDefault())
        val startYear = DEFAULT_START_YEAR
        val endYear = DEFAULT_END_YEAR
        val minDate = ""
        val maxDate = ""
        val onChangeListener: NumberPicker.OnValueChangeListener =
            NumberPicker.OnValueChangeListener { picker, oldValue, newValue ->
                updateInputState()
                mTempDate.timeInMillis = mCurrentDate.timeInMillis
                // take care of wrapping of days and months to update greater
                // fields
                if (picker === mDayNPicker) {
                    val maxDayOfMonth = mTempDate
                        .getActualMaximum(Calendar.DAY_OF_MONTH)
                    if (oldValue == maxDayOfMonth && newValue == 1 && isAutoScrollState) {
                        mTempDate.add(Calendar.DAY_OF_MONTH, 1)
                    } else if (oldValue == 1 && newValue == maxDayOfMonth && isAutoScrollState) {
                        mTempDate.add(Calendar.DAY_OF_MONTH, -1)
                    } else {
                        mTempDate.add(Calendar.DAY_OF_MONTH, newValue - oldValue)
                    }
                } else if (picker === mMonthNPicker) {
                    if (oldValue == 11 && newValue == 0 && isAutoScrollState) {
                        mTempDate.add(Calendar.MONTH, 1)
                    } else if (oldValue == 0 && newValue == 11 && isAutoScrollState) {
                        mTempDate.add(Calendar.MONTH, -1)
                    } else {
                        mTempDate.add(Calendar.MONTH, newValue - oldValue)
                    }
                } else if (picker === mYearNPicker) {
                    mTempDate[Calendar.YEAR] = newValue
                } else if (picker === mHourNPicker) {
                    mTempDate[Calendar.HOUR_OF_DAY] = newValue
                } else if (picker === mMinuteNPicker) {
                    mTempDate[Calendar.MINUTE] = newValue
                } else {
                    throw IllegalArgumentException()
                }
                // now set the date to the adjusted one
                setDate(
                    mTempDate[Calendar.YEAR],
                    mTempDate[Calendar.MONTH],
                    mTempDate[Calendar.DAY_OF_MONTH],
                    mTempDate[Calendar.HOUR_OF_DAY],
                    mTempDate[Calendar.MINUTE],
                    mTempDate[Calendar.SECOND]
                )
                updateNPickers()
                notifyDateChanged()
            }
        // year
        mYearNPicker = findViewById(R.id.year)
        mYearNPicker.formatter = NumberPicker.Formatter { "${it}" }
        mYearNPicker.setOnLongPressUpdateInterval(100)
        mYearNPicker.setOnChangedListener(onChangeListener)
        mYearNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        // month
        mMonthNPicker = findViewById(R.id.month)
        mMonthNPicker.setMinValue(0)
        mMonthNPicker.setMaxValue(mNumberOfMonths - 1)
        mMonthNPicker.setDisplayedValues(mShortMonths)
        mMonthNPicker.setOnLongPressUpdateInterval(200)
        mMonthNPicker.setOnChangedListener(onChangeListener)
        mMonthNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        // day
        mDayNPicker = findViewById(R.id.day)
        mDayNPicker.setFormatter(NumberPicker.getTwoDigitFormatter())
        mDayNPicker.setOnLongPressUpdateInterval(100)
        mDayNPicker.setOnChangedListener(onChangeListener)
        mDayNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        // hour
        mHourNPicker = findViewById(R.id.hour)
        mHourNPicker.setOnLongPressUpdateInterval(100)
        mHourNPicker.setOnChangedListener(onChangeListener)
        mHourNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)
        // minute
        mMinuteNPicker = findViewById(R.id.minute)
        mMinuteNPicker.setMinValue(0)
        mMinuteNPicker.setMaxValue(59)
        mMinuteNPicker.setOnLongPressUpdateInterval(100)
        mMinuteNPicker.setFormatter(NumberPicker.getTwoDigitFormatter())
        mMinuteNPicker.setOnChangedListener(onChangeListener)
        mMinuteNPicker.setImeOptions(EditorInfo.IME_ACTION_NEXT)

        // set the min date giving priority of the minDate over startYear
        mTempDate.clear()
        if (!TextUtils.isEmpty(minDate)) {
            if (!parseDate(minDate, mTempDate)) {
                mTempDate[startYear, 0] = 1
            }
        } else {
            mTempDate[startYear, 0] = 1
        }
        setMinDate(mTempDate.timeInMillis)

        // set the max date giving priority of the maxDate over endYear
        mTempDate.clear()
        if (!TextUtils.isEmpty(maxDate)) {
            if (!parseDate(maxDate, mTempDate)) {
                mTempDate[endYear, 11] = 31
            }
        } else {
            mTempDate[endYear, 11] = 31
        }
        setMaxDate(mTempDate.timeInMillis)

        // initialize to current date
        mCurrentDate.timeInMillis = System.currentTimeMillis()
        init(
            mCurrentDate[Calendar.YEAR],
            mCurrentDate[Calendar.MONTH],
            mCurrentDate[Calendar.DAY_OF_MONTH],
            mCurrentDate[Calendar.HOUR_OF_DAY],
            mCurrentDate[Calendar.MINUTE],
            mCurrentDate[Calendar.SECOND]
        )

        // re-order the number NPickers to match the current date format
        reorderNPickers()

        // If not explicitly specified this view is important for accessibility.
        if (importantForAccessibility == IMPORTANT_FOR_ACCESSIBILITY_AUTO
        ) {
            importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    fun setOnChangedListener(listener: OnChangedListener) {
        mOnChangedListener = listener
    }

    fun setMinDate(minDate: Long) {
        mTempDate.timeInMillis = minDate
        if (mTempDate[Calendar.YEAR] == mMinDate[Calendar.YEAR]
            && mTempDate[Calendar.DAY_OF_YEAR] != mMinDate
                .get(Calendar.DAY_OF_YEAR)
        ) {
            return
        }
        mMinDate.timeInMillis = minDate
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.timeInMillis = mMinDate.timeInMillis
        }
        updateNPickers()
    }

    fun setMaxDate(maxDate: Long) {
        mTempDate.timeInMillis = maxDate
        if (mTempDate[Calendar.YEAR] == mMaxDate[Calendar.YEAR]
            && mTempDate[Calendar.DAY_OF_YEAR] != mMaxDate
                .get(Calendar.DAY_OF_YEAR)
        ) {
            return
        }
        mMaxDate.timeInMillis = maxDate
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.timeInMillis = mMaxDate.timeInMillis
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
        mHourNPicker.isEnabled = enabled
        mDayNPicker.isEnabled = enabled
        mMonthNPicker.isEnabled = enabled
        mYearNPicker.isEnabled = enabled
        mMinuteNPicker.isEnabled = enabled
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
            DateUtils.formatDateTime(context, mCurrentDate.timeInMillis, flags)
        event.text.add(selectedDateUtterance)
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = DateTimePicker::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = DateTimePicker::class.java.name
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setCurrentLocale(newConfig.locale)
    }

    private fun setCurrentLocale(locale: Locale) {
        if (locale == mCurrentLocale) {
            return
        }
        mCurrentLocale = locale
        mTempDate = getCalendarForLocale(mTempDate, locale)
        mMinDate = getCalendarForLocale(mMinDate, locale)
        mMaxDate = getCalendarForLocale(mMaxDate, locale)
        mCurrentDate = getCalendarForLocale(mCurrentDate, locale)
        mNumberOfMonths = mTempDate.getActualMaximum(Calendar.MONTH) + 1
        mShortMonths = arrayOfNulls(mNumberOfMonths)
        for (i in 0 until mNumberOfMonths) {
            mShortMonths[i] = if (i<10) "0${i}" else i.toString()
        }
    }

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

    private fun reorderNPickers() {
        val order: CharArray = try {
            DateFormat.getDateFormatOrder(context)
        } catch (expected: IllegalArgumentException) {
            CharArray(0)
        }
        val NPickerCount = order.size
        for (i in 0 until NPickerCount) {
            when (order[i]) {
                'm' -> setImeOptions(mMinuteNPicker, NPickerCount, i)
                'h' -> setImeOptions(mHourNPicker, NPickerCount, i)
                'd' -> setImeOptions(mDayNPicker, NPickerCount, i)
                'M' -> setImeOptions(mMonthNPicker, NPickerCount, i)
                'y' -> setImeOptions(mYearNPicker, NPickerCount, i)
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun updateDate(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minute: Int
    ) {
        if (!isNewDate(year, month, dayOfMonth, hourOfDay, minute)) {
            return
        }
        setDate(year, month, dayOfMonth, hourOfDay, minute, second)
        updateNPickers()
        notifyDateChanged()
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, year, month, dayOfMonth, hourOfDay, minute, second)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        setDate(ss.mYear, ss.mMonth, ss.mDay, ss.mHour, ss.mMinute, ss.mSecond)
        updateNPickers()
    }

    fun init(
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minute: Int,
        second: Int
    ) {
        setDate(year, monthOfYear, dayOfMonth, hourOfDay, minute, second)
        updateNPickers()
    }

    private fun parseDate(date: String, outDate: Calendar): Boolean {
        return try {
            val mDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            outDate.time = Objects.requireNonNull(mDateFormat.parse(date))
            true
        } catch (e: ParseException) {
            Log.w("DateTimePicker", "Date: $date not in format: MM/dd/yyyy")
            false
        }
    }

    private fun isNewDate(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minute: Int,
    ): Boolean {
        return mCurrentDate[Calendar.YEAR] != year ||
                mCurrentDate[Calendar.MONTH] != dayOfMonth ||
                mCurrentDate[Calendar.DAY_OF_MONTH] != month ||
                mCurrentDate[Calendar.HOUR_OF_DAY] != hourOfDay ||
                mCurrentDate[Calendar.MINUTE] != minute
    }

    private fun setDate(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minute: Int,
        second: Int
    ) {
        mCurrentDate[year, month, dayOfMonth, hourOfDay, minute] = second
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.timeInMillis = mMinDate.timeInMillis
        } else if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.timeInMillis = mMaxDate.timeInMillis
        }
    }

    private fun updateNPickers() {
        if (mCurrentDate == mMinDate) {
            mDayNPicker.minValue = mCurrentDate[Calendar.DAY_OF_MONTH]
            mDayNPicker.maxValue = mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            mDayNPicker.wrapSelectorWheel = false
            mMonthNPicker.displayedValues = null
            mMonthNPicker.minValue = mCurrentDate[Calendar.MONTH]
            mMonthNPicker.maxValue = mCurrentDate.getActualMaximum(Calendar.MONTH)
            mMonthNPicker.wrapSelectorWheel = false
        } else if (mCurrentDate == mMaxDate) {
            mDayNPicker.minValue = mCurrentDate.getActualMinimum(Calendar.DAY_OF_MONTH)
            mDayNPicker.maxValue = mCurrentDate[Calendar.DAY_OF_MONTH]
            mDayNPicker.wrapSelectorWheel = false
            mMonthNPicker.displayedValues = null
            mMonthNPicker.minValue = mCurrentDate.getActualMinimum(Calendar.MONTH)
            mMonthNPicker.maxValue = mCurrentDate[Calendar.MONTH]
            mMonthNPicker.wrapSelectorWheel = false
        } else {
            mDayNPicker.minValue = 1
            mDayNPicker.maxValue = mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH)
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
        mYearNPicker.minValue = mMinDate[Calendar.YEAR]
        mYearNPicker.maxValue = mMaxDate[Calendar.YEAR]
        mYearNPicker.wrapSelectorWheel = false
        mHourNPicker.minValue = 0
        mHourNPicker.maxValue = 23
        mHourNPicker.wrapSelectorWheel = true
        mMinuteNPicker.minValue = 0
        mMinuteNPicker.maxValue = 59
        mMinuteNPicker.wrapSelectorWheel = true
        // set the NPicker values
        mYearNPicker.value = mCurrentDate[Calendar.YEAR]
        mMonthNPicker.value = mCurrentDate[Calendar.MONTH]
        mDayNPicker.value = mCurrentDate[Calendar.DAY_OF_MONTH]
        mHourNPicker.value = mCurrentDate[Calendar.HOUR_OF_DAY]
        mMinuteNPicker.value = mCurrentDate[Calendar.MINUTE]
    }

    val year: Int
        /**
         * @return The selected year.
         */
        get() = mCurrentDate[Calendar.YEAR]
    val month: Int
        /**
         * @return The selected month.
         */
        get() = mCurrentDate[Calendar.MONTH]
    val dayOfMonth: Int
        /**
         * @return The selected day of month.
         */
        get() = mCurrentDate[Calendar.DAY_OF_MONTH]
    val hourOfDay: Int
        /**
         * @return The selected day of month.
         */
        get() = mCurrentDate[Calendar.HOUR_OF_DAY]
    val minute: Int
        get() = mCurrentDate[Calendar.MINUTE]
    val second: Int
        get() = mCurrentDate[Calendar.SECOND]

    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    private fun notifyDateChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        if (mOnChangedListener != null) {
            mOnChangedListener!!.onChanged(this, year, month, dayOfMonth, hourOfDay, minute, second)
        }
    }

    /**
     * Sets the IME options for a NPicker based on its ordering.
     *
     * @param numberPicker The NPicker.
     * @param pickerCount  The total NPicker count.
     * @param pickerIndex  The index of the given NPicker.
     */
    private fun setImeOptions(numberPicker: NumberPicker?, pickerCount: Int, pickerIndex: Int) {
        val imeOptions: Int = if (pickerIndex < pickerCount - 1) {
            EditorInfo.IME_ACTION_NEXT
        } else {
            EditorInfo.IME_ACTION_DONE
        }
        numberPicker?.setImeOptions(imeOptions)
    }

    private fun updateInputState() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mYearNPicker)) {
                mYearNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            } else if (inputMethodManager.isActive(mMonthNPicker)) {
                mMonthNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            } else if (inputMethodManager.isActive(mDayNPicker)) {
                mDayNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            } else if (inputMethodManager.isActive(mHourNPicker)) {
                mHourNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            } else if (inputMethodManager.isActive(mMinuteNPicker)) {
                mMinuteNPicker.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            }
        }
    }

    interface OnChangedListener {
        fun onChanged(
            picker: DateTimePicker?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int,
            hourOfDay: Int,
            minute: Int,
            second: Int
        )
    }

    /**
     * Class for managing state storing/restoring.
     */
    private class SavedState : BaseSavedState {
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        val mHour: Int
        val mMinute: Int
        val mSecond: Int

        /**
         * Constructor called from [DateTimePicker.onSaveInstanceState]
         */
        constructor(
            superState: Parcelable?,
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int,
            second: Int
        ) : super(superState) {
            mYear = year
            mMonth = month
            mDay = day
            mHour = hour
            mMinute = minute
            mSecond = second
        }

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(pc: Parcel) : super(pc) {
            mYear = pc.readInt()
            mMonth = pc.readInt()
            mDay = pc.readInt()
            mHour = pc.readInt()
            mMinute = pc.readInt()
            mSecond = pc.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(mYear)
            dest.writeInt(mMonth)
            dest.writeInt(mDay)
            dest.writeInt(mHour)
            dest.writeInt(mMinute)
            dest.writeInt(mSecond)
        }
    }

    fun setAccessibilityDescriptionEnabled(enabled: Boolean) {
        super.setAccessibilityDescriptionEnabled(
            enabled,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker
        )
    }

    fun setDividerColor(@ColorInt color: Int) {
        super.setDividerColor(
            color,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setDividerColorResource(@ColorRes colorId: Int) {
        super.setDividerColor(
            ContextCompat.getColor(context, colorId),
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setDividerDistance(distance: Int) {
        super.setDividerDistance(
            distance,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setDividerDistanceResource(@DimenRes dimenId: Int) {
        super.setDividerDistanceResource(
            dimenId,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setDividerType(@DividerType dividerType: Int) {
        super.setDividerType(
            dividerType,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setDividerThickness(thickness: Int) {
        super.setDividerThickness(
            thickness,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setDividerThicknessResource(@DimenRes dimenId: Int) {
        super.setDividerThicknessResource(
            dimenId,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setOrder(@Order order: Int) {
        super.setOrder(
            order,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setOrientation(@NumberPicker.Orientation orientation: Int) {
        super.setOrientation(
            orientation,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setWheelItemCount(count: Int) {
        super.setWheelItemCount(
            count,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setFormatter(
        yearFormatter: String?, monthFormatter: String?, dayFormatter: String?,
        hourFormatter: String?, minuteFormatter: String?
    ) {
        mYearNPicker.setFormatter(yearFormatter)
        mMonthNPicker.setFormatter(monthFormatter)
        mDayNPicker.setFormatter(dayFormatter)
        mHourNPicker.setFormatter(hourFormatter)
        mMinuteNPicker.setFormatter(minuteFormatter)
    }

    fun setFormatter(
        @StringRes yearFormatterId: Int,
        @StringRes monthFormatterId: Int,
        @StringRes dayFormatterId: Int,
        @StringRes hourFormatterId: Int,
        @StringRes minuteFormatterId: Int
    ) {
        mYearNPicker.setFormatter(resources.getString(yearFormatterId))
        mMonthNPicker.setFormatter(resources.getString(monthFormatterId))
        mDayNPicker.setFormatter(resources.getString(dayFormatterId))
        mHourNPicker.setFormatter(resources.getString(hourFormatterId))
        mMinuteNPicker.setFormatter(resources.getString(minuteFormatterId))
    }

    fun setFadingEdgeEnabled(fadingEdgeEnabled: Boolean) {
        super.setFadingEdgeEnabled(
            fadingEdgeEnabled,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setFadingEdgeStrength(strength: Float) {
        super.setFadingEdgeStrength(
            strength,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setScrollerEnabled(scrollerEnabled: Boolean) {
        super.setScrollerEnabled(
            scrollerEnabled,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextAlign(@NumberPicker.Align align: Int) {
        super.setSelectedTextAlign(
            align,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextColor(@ColorInt color: Int) {
        super.setSelectedTextColor(
            color,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextColorResource(@ColorRes colorId: Int) {
        super.setSelectedTextColorResource(
            colorId,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextSize(textSize: Float) {
        super.setSelectedTextSize(
            textSize,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextSize(@DimenRes dimenId: Int) {
        super.setSelectedTextSize(
            resources.getDimension(dimenId),
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextStrikeThru(strikeThruText: Boolean) {
        super.setSelectedTextStrikeThru(
            strikeThruText,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTextUnderline(underlineText: Boolean) {
        super.setSelectedTextUnderline(
            underlineText,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTypeface(typeface: Typeface?) {
        super.setSelectedTypeface(
            typeface,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTypeface(string: String?, style: Int) {
        super.setSelectedTypeface(
            string,
            style,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTypeface(string: String?) {
        super.setSelectedTypeface(
            string,
            Typeface.NORMAL,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTypeface(@StringRes stringId: Int, style: Int) {
        super.setSelectedTypeface(
            resources.getString(stringId),
            style,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setSelectedTypeface(@StringRes stringId: Int) {
        super.setSelectedTypeface(
            stringId,
            Typeface.NORMAL,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextAlign(@NumberPicker.Align align: Int) {
        super.setTextAlign(
            align,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextColor(@ColorInt color: Int) {
        super.setTextColor(
            color,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextColorResource(@ColorRes colorId: Int) {
        super.setTextColorResource(
            colorId,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextSize(textSize: Float) {
        super.setTextSize(
            textSize,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextSize(@DimenRes dimenId: Int) {
        super.setTextSize(
            dimenId,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextStrikeThru(strikeThruText: Boolean) {
        super.setTextStrikeThru(
            strikeThruText,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTextUnderline(underlineText: Boolean) {
        super.setTextUnderline(
            underlineText,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTypeface(typeface: Typeface?) {
        super.setTypeface(
            typeface,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTypeface(string: String?, style: Int) {
        super.setTypeface(
            string,
            style,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTypeface(string: String?) {
        super.setTypeface(
            string,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTypeface(@StringRes stringId: Int, style: Int) {
        super.setTypeface(
            stringId,
            style,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setTypeface(@StringRes stringId: Int) {
        super.setTypeface(
            stringId,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setLineSpacingMultiplier(multiplier: Float) {
        super.setLineSpacingMultiplier(
            multiplier,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setMaxFlingVelocityCoefficient(coefficient: Int) {
        super.setMaxFlingVelocityCoefficient(
            coefficient,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setImeOptions(imeOptions: Int) {
        super.setImeOptions(
            imeOptions,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    fun setItemSpacing(itemSpacing: Int) {
        super.setItemSpacing(
            itemSpacing,
            mYearNPicker,
            mMonthNPicker,
            mDayNPicker,
            mHourNPicker,
            mMinuteNPicker,

            )
    }

    companion object {
        private const val DEFAULT_START_YEAR = 1900
        private const val DEFAULT_END_YEAR = 2100
        private const val DEFAULT_AUTO_SCROLL_STATE = true
    }
}