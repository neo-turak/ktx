package com.github.neoturak.common

import android.app.Activity
import android.app.Service
import android.app.UiModeManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager



import java.io.Serializable
import java.util.*

object AnkoInternals {
    const val NO_GETTER: String = "Property does not have a getter"

    fun noGetter(): Nothing = throw AnkoException("Property does not have a getter")

    private class AnkoContextThemeWrapper(base: Context?, val theme: Int) : ContextThemeWrapper(base, theme)

    fun <T : View> addView(manager: ViewManager, view: T) = when (manager) {
        is ViewGroup -> manager.addView(view)
        is AnkoContext<*> -> manager.addView(view, null)
        else -> throw AnkoException("$manager is the wrong parent")
    }

    // Some constants not present in Android SDK v.15
    private object InternalConfiguration {
        val SCREENLAYOUT_LAYOUTDIR_MASK = 0xC0
        val SCREENLAYOUT_LAYOUTDIR_SHIFT = 6
        val SCREENLAYOUT_LAYOUTDIR_RTL = 0x02 shl SCREENLAYOUT_LAYOUTDIR_SHIFT

        val UI_MODE_TYPE_APPLIANCE = 0x05
        val UI_MODE_TYPE_WATCH = 0x06
    }

    @JvmStatic
    fun <T> createIntent(ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any?>>): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    @JvmStatic
    fun internalStartActivity(
            ctx: Context,
            activity: Class<out Activity>,
            params: Array<out Pair<String, Any?>>
    ) {
        ctx.startActivity(createIntent(ctx, activity, params))
    }

    @JvmStatic
    fun internalStartActivityForResult(
            act: Activity,
            activity: Class<out Activity>,
            requestCode: Int,
            params: Array<out Pair<String, Any?>>
    ) {
        act.startActivityForResult(createIntent(act, activity, params), requestCode)
    }

    @JvmStatic
    fun internalStartService(
            ctx: Context,
            service: Class<out Service>,
            params: Array<out Pair<String, Any?>>
    ): ComponentName? = ctx.startService(createIntent(ctx, service, params))

    @JvmStatic
    fun internalStopService(
            ctx: Context,
            service: Class<out Service>,
            params: Array<out Pair<String, Any?>>
    ): Boolean = ctx.stopService(createIntent(ctx, service, params))

    @JvmStatic
    private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            val value = it.second
            when (value) {
                null -> intent.putExtra(it.first, null as Serializable?)
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw AnkoException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw AnkoException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }

    @JvmStatic
    inline fun <T> useCursor(cursor: Cursor, f: (Cursor) -> T): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Closeable only added in API 16
            cursor.use(f)
        } else {
            try {
                f(cursor)
            } finally {
                try {
                    cursor.close()
                } catch (e: Exception) {
                    // Do nothing
                }
            }
        }
    }

    @JvmStatic
    fun <T : View> initiateView(ctx: Context, viewClass: Class<T>): T {
        fun getConstructor1() = viewClass.getConstructor(Context::class.java)
        fun getConstructor2() = viewClass.getConstructor(Context::class.java, AttributeSet::class.java)

        try {
            return getConstructor1().newInstance(ctx)
        } catch (e: NoSuchMethodException) {
            try {
                return getConstructor2().newInstance(ctx, null)
            }
            catch (e: NoSuchMethodException) {
                throw AnkoException("Can't initiate View of class ${viewClass.name}: can't find proper constructor")
            }
        }

    }
}
