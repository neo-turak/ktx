
@file:JvmName("DesignSnackbarKt")
@file:Suppress("NOTHING_TO_INLINE")
package com.github.neoturak.common

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
@JvmName("snackbar2")
inline fun View.snackbar(@StringRes message: Int) = Snackbar
        .make(this, message, Snackbar.LENGTH_SHORT)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
@JvmName("longSnackbar2")
inline fun View.longSnackbar(@StringRes message: Int) = Snackbar
        .make(this, message, Snackbar.LENGTH_LONG)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_INDEFINITE] duration.
 *
 * @param message the message text resource.
 */
@JvmName("indefiniteSnackbar2")
inline fun View.indefiniteSnackbar(@StringRes message: Int) = Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .apply { show() }

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
@JvmName("snackbar2")
inline fun View.snackbar(message: CharSequence) = Snackbar
        .make(this, message, Snackbar.LENGTH_SHORT)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
@JvmName("longSnackbar2")
inline fun View.longSnackbar(message: CharSequence) = Snackbar
        .make(this, message, Snackbar.LENGTH_LONG)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_INDEFINITE] duration.
 *
 * @param message the message text.
 */
@JvmName("indefiniteSnackbar2")
inline fun View.indefiniteSnackbar(message: CharSequence) = Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .apply { show() }

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
@JvmName("snackbar2")
inline fun View.snackbar(message: Int, @StringRes actionText: Int, noinline action: (View) -> Unit) = Snackbar
        .make(this, message, Snackbar.LENGTH_SHORT)
        .setAction(actionText, action)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
@JvmName("longSnackbar2")
inline fun View.longSnackbar(@StringRes message: Int, @StringRes actionText: Int, noinline action: (View) -> Unit) = Snackbar
        .make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionText, action)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_INDEFINITE] duration.
 *
 * @param message the message text resource.
 */
@JvmName("indefiniteSnackbar2")
inline fun View.indefiniteSnackbar(@StringRes message: Int, @StringRes actionText: Int, noinline action: (View) -> Unit) = Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, action)
        .apply { show() }

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
@JvmName("snackbar2")
inline fun View.snackbar(message: CharSequence, actionText: CharSequence, noinline action: (View) -> Unit) = Snackbar
        .make(this, message, Snackbar.LENGTH_SHORT)
        .setAction(actionText, action)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
@JvmName("longSnackbar2")
inline fun View.longSnackbar(message: CharSequence, actionText: CharSequence, noinline action: (View) -> Unit) = Snackbar
        .make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionText, action)
        .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_INDEFINITE] duration.
 *
 * @param message the message text.
 */
@JvmName("indefiniteSnackbar2")
inline fun View.indefiniteSnackbar(message: CharSequence, actionText: CharSequence, noinline action: (View) -> Unit) = Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, action)
        .apply { show() }
