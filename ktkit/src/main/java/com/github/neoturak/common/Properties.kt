package com.github.neoturak.common

import android.os.Build
import androidx.annotation.RequiresApi


/**
 *@author   Hugo
 *@Description
 *@time    1/24/22 7:19 PM
 *@project  client-juran
 *Think Twice, Code Once!
 */

var android.view.View.backgroundColor: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setBackgroundColor(v)

var android.view.View.backgroundResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setBackgroundResource(v)

var android.widget.ImageView.imageResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setImageResource(v)

var android.widget.ImageView.imageURI: android.net.Uri?
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setImageURI(v)

var android.widget.ImageView.imageBitmap: android.graphics.Bitmap?
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setImageBitmap(v)

var android.widget.TextView.textColor: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setTextColor(v)

var android.widget.TextView.hintTextColor: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setHintTextColor(v)

var android.widget.TextView.linkTextColor: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setLinkTextColor(v)

var android.widget.TextView.lines: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setLines(v)

var android.widget.TextView.singleLine: Boolean
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setSingleLine(v)

var android.widget.RelativeLayout.horizontalGravity: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setHorizontalGravity(v)

var android.widget.RelativeLayout.verticalGravity: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setVerticalGravity(v)

var android.widget.LinearLayout.horizontalGravity: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setHorizontalGravity(v)

var android.widget.LinearLayout.verticalGravity: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setVerticalGravity(v)

var android.widget.Gallery.gravity: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setGravity(v)

var android.widget.AbsListView.selectorResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setSelector(v)

var android.widget.CalendarView.selectedDateVerticalBarResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    set(v) = setSelectedDateVerticalBar(v)

var android.widget.CheckedTextView.checkMarkDrawableResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setCheckMarkDrawable(v)

var android.widget.CompoundButton.buttonDrawableResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setButtonDrawable(v)

var android.widget.TabWidget.leftStripDrawableResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setLeftStripDrawable(v)

var android.widget.TabWidget.rightStripDrawableResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setRightStripDrawable(v)

var android.widget.TextView.hintResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setHint(v)

var android.widget.TextView.textResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    set(v) = setText(v)

var android.widget.Toolbar.logoResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(v) = setLogo(v)

var android.widget.Toolbar.logoDescriptionResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(v) = setLogoDescription(v)

var android.widget.Toolbar.navigationContentDescriptionResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(v) = setNavigationContentDescription(v)

var android.widget.Toolbar.navigationIconResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(v) = setNavigationIcon(v)

var android.widget.Toolbar.subtitleResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(v) = setSubtitle(v)

var android.widget.Toolbar.titleResource: Int
    @Deprecated(Internals.NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    set(v) = setTitle(v)
