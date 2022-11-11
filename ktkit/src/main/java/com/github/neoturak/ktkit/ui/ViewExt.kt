@file:OptIn(ExperimentalContracts::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.github.neoturak.ktkit.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlin.contracts.ExperimentalContracts

/**
 * <pre>
 *     author: dhl
 *     date  : 2021/3/29
 *     desc  :
 * </pre>
 */

@kotlin.internal.InlineOnly
inline fun View.visible() {
    visibility = View.VISIBLE
}

@kotlin.internal.InlineOnly
inline fun View.gone() {
    visibility = View.GONE
}

@kotlin.internal.InlineOnly
inline fun View.invisible() {
    visibility = View.INVISIBLE
}

@kotlin.internal.InlineOnly
inline fun View.showShortSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

@kotlin.internal.InlineOnly
inline fun View.showShortSnackbar(@StringRes stringResId: Int) {
    Snackbar.make(this, stringResId, Snackbar.LENGTH_SHORT).show()
}

@kotlin.internal.InlineOnly
inline fun View.showLongSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

@kotlin.internal.InlineOnly
inline fun View.showLongSnackbar(@StringRes stringResId: Int) {
    Snackbar.make(this, stringResId, Snackbar.LENGTH_LONG).show()
}

/**
 * Example：
 *
 * ```
 * btn.showActionSnackBar("公众号：ByteCode", "click me") {
 *      showLongToast("hi 我是 dhl")
 * }
 * ```
 */
@kotlin.internal.InlineOnly
inline fun View.showActionSnackBar(
    message: String,
    actionName: String,
    noinline block: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionName) {
            block()
        }.show()
}

/**
 * 快捷设置View的自定义纯色带圆角背景
 *
 * @receiver View
 * @param color Int 颜色值
 * @param cornerRadius Float 圆角 单位px
 */
@kotlin.internal.InlineOnly
inline fun View.setRoundRectBg(
    @ColorInt color: Int,
    cornerRadius: Float = 15F
) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
        background = GradientDrawable().apply {
            setColor(color)
            setCornerRadius(cornerRadius)
        }
    }
}


/**
 * px 转换万能公式
 */
inline fun <reified T:Any> Context.px2dp(v:T):T{
    when(v){
        is Int->{
            return v.div(resources.displayMetrics.density).plus(0.5).toInt() as T
        }
        is Float->{
            return v.div(resources.displayMetrics.density).plus(0.5).toFloat() as T
        }
        is Double->{
            return v.div(resources.displayMetrics.density).plus(0.5) as T
        }
        is Long->{
            return v.div(resources.displayMetrics.density).plus(0.5).toLong() as T
        }
        else-> {
            return 0 as T
        }
    }
}

/**
 * sp 转换万能公式
 */
inline fun <reified T:Any> Context.sp2px(v:T):T{
    when(v){
        is Int->{
            return (resources.displayMetrics.scaledDensity.times(v).plus(0.5)).toInt() as T
        }
        is Float->{
            return (resources.displayMetrics.scaledDensity.times(v).plus(0.5)).toFloat() as T
        }
        is Double->{
            return (resources.displayMetrics.scaledDensity.times(v).plus(0.5)) as T
        }
        is Long->{
            return (resources.displayMetrics.scaledDensity.times(v).plus(0.5)).toLong() as T
        }
        else-> {
            return 0 as T
        }
    }
}
