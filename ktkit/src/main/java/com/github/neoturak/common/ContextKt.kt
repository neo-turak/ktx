@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.github.neoturak.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

// toast
@kotlin.internal.InlineOnly
inline fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@kotlin.internal.InlineOnly
inline fun Context.showShortToast(@StringRes stringResId: Int) {
    Toast.makeText(this, getString(stringResId), Toast.LENGTH_SHORT).show()
}

@kotlin.internal.InlineOnly
inline fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@kotlin.internal.InlineOnly
inline fun Context.showLongToast(@StringRes stringResId: Int) {
    Toast.makeText(this, getString(stringResId), Toast.LENGTH_LONG).show()
}

// 屏幕宽度(px)
inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

// 屏幕高度(px)
inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

// 屏幕的密度
inline val Context.density: Float
    get() = resources.displayMetrics.density


/**
 * 这个方法 你给什么（T），就返回什么类型的数据（T）。
 * 万能公式
 */
@kotlin.internal.InlineOnly
inline fun <reified T:Any> Context.dp2px(v:T):T{
    when(v){
        is Int->{
            return (resources.displayMetrics.density.times(v).plus(0.5)).toInt() as T
        }
        is Float->{
            return (resources.displayMetrics.density.times(v).plus(0.5)).toFloat() as T
        }
        is Double->{
            return (resources.displayMetrics.density.times(v).plus(0.5)) as T
        }
        is Long->{
            return (resources.displayMetrics.density.times(v).plus(0.5)).toLong() as T
        }
        else-> {
            return 0 as T
        }
    }
}

/**
 * px 转换万能公式
 */
@kotlin.internal.InlineOnly
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

@kotlin.internal.InlineOnly
inline fun <reified T:Any> Context.px2sp(v:T):T{
    when(v){
        is Int->{
            return v.div(resources.displayMetrics.scaledDensity).plus(0.5).toInt() as T
        }
        is Float->{
            return v.div(resources.displayMetrics.scaledDensity).plus(0.5).toFloat() as T
        }
        is Double->{
            return v.div(resources.displayMetrics.scaledDensity).plus(0.5) as T
        }
        is Long->{
            return v.div(resources.displayMetrics.scaledDensity).plus(0.5).toLong() as T
        }
        else-> {
            return 0 as T
        }
    }
}
/**
 * sp 转换万能公式
 */
@kotlin.internal.InlineOnly
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

/**
 * 设置状态栏的颜色
 *
 * usage：
 * setSatatusBarColor(android.R.color.darker_gray)
 */
fun Context.setStatusBarColor(@ColorRes colorResId: Int) {

    if (this is Activity) {
        setStatusBarColor(WeakReference<Activity>(this), colorResId)
    }
}

private fun Context.setStatusBarColor(context: WeakReference<Activity>, @ColorRes colorResId: Int) {
    context.get()?.run {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(colorResId)
        }
    }
}

// 获取 Drawable
@kotlin.internal.InlineOnly
inline fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

// 获取 color
@kotlin.internal.InlineOnly
inline fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)
