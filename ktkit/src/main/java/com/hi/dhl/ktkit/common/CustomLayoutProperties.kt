package com.hi.dhl.ktkit.common
import android.view.ViewGroup

/**
 *@author   Hugo
 *@Description
 *@time    1/24/22 7:31 PM
 *@project  client-juran
 *Think Twice, Code Once!
 */
val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT
val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT

var ViewGroup.MarginLayoutParams.verticalMargin: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(v) {
        topMargin = v
        bottomMargin = v
    }

var ViewGroup.MarginLayoutParams.horizontalMargin: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(v) { leftMargin = v; rightMargin = v }

var ViewGroup.MarginLayoutParams.margin: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(v) {
        leftMargin = v
        rightMargin = v
        topMargin = v
        bottomMargin = v
    }