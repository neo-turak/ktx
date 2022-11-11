package com.github.neoturak.ktkit.common

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 *@author   Hugo
 *@Description
 *@time    1/24/22 7:21 PM
 *Think Twice, Code Once!
 */
var View.backgroundDrawable: Drawable?
    inline get() = background
    set(value) = setBackgroundDrawable(value)

var View.backgroundColorResource: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(colorId) = setBackgroundColor(context.resources.getColor(colorId))

var View.leftPadding: Int
    inline get() = paddingLeft
    set(value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

var View.topPadding: Int
    inline get() = paddingTop
    set(value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

var View.rightPadding: Int
    inline get() = paddingRight
    set(value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

var View.bottomPadding: Int
    inline get() = paddingBottom
    set(value) = setPadding(paddingLeft, paddingTop, paddingRight, value)

@Deprecated("Use horizontalPadding instead", ReplaceWith("horizontalPadding"))
var View.paddingHorizontal: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) = setPadding(value, paddingTop, value, paddingBottom)

var View.horizontalPadding: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) = setPadding(value, paddingTop, value, paddingBottom)

@Deprecated("Use verticalPadding instead", ReplaceWith("verticalPadding"))
var View.paddingVertical: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) = setPadding(paddingLeft, value, paddingRight, value)

var View.verticalPadding: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) = setPadding(paddingLeft, value, paddingRight, value)

var View.padding: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    inline set(value) = setPadding(value, value, value, value)

var TextView.allCaps: Boolean
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    inline set(value) = setAllCaps(value)

var TextView.ems: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    inline set(value) = setEms(value)

inline var TextView.isSelectable: Boolean
    get() = isTextSelectable
    set(value) = setTextIsSelectable(value)

var TextView.textAppearance: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) = if (Build.VERSION.SDK_INT >= 23) setTextAppearance(value) else setTextAppearance(context, value)

var TextView.textSizeDimen: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(value) = setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(value))

var TextView.textColorResource: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(colorId) = setTextColor(context.resources.getColor(colorId))

var ImageView.image: Drawable?
    inline get() = drawable
    inline set(value) = setImageDrawable(value)
