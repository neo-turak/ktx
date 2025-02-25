package com.github.neoturak.common


import android.app.Activity
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartActivity(this, T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartActivity(this.requireContext(), T::class.java, params)

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>
) =
    AnkoInternals.internalStartActivityForResult(this, T::class.java, requestCode, params)


@RequiresApi(Build.VERSION_CODES.M)
inline fun <reified T : Activity> Fragment.startActivityForResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>
) =
    startActivityForResult(
        AnkoInternals.createIntent(this.requireContext(), T::class.java, params),
        requestCode
    )

/**
 * Example:
 * 使用方法.
 *
 * ```
 * this.startActivityForResult<ProfileActivity>(KEY to "value",key to "value"){it:Intent?->
 * here is result of your callback.
 * }
 *
 * ```
 *
 */
inline fun Fragment.registerLauncher(
    crossinline callback: (i: Intent?) -> Unit
): ActivityResultLauncher<Intent> {
   return this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            callback(it.data)
        }
    }
}


inline fun <reified T : Activity> Fragment.startActivityWithResult(
    launcher: ActivityResultLauncher<Intent>,
    vararg params: Pair<String, Any?>,
) {
    launcher.launch(AnkoInternals.createIntent(this.requireContext(), T::class.java, params))
}

/**
 * Example:
 * 使用方法.
 *
 * ```
 * this.registerActivityLauncher{it:Intent?->
 *  //here is result of your callback.
 * }
 *
 * ```
 *
 */

 fun  AppCompatActivity.registerLauncher(
     callback: (i: Intent?) -> Unit
): ActivityResultLauncher<Intent> {
    return this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                callback(it.data)
            }
        }
}

inline fun <reified T : Activity> AppCompatActivity.startActivityWithResult(
    launcher: ActivityResultLauncher<Intent>,
    vararg params: Pair<String, Any?>,
) {
    launcher.launch(AnkoInternals.createIntent(this, T::class.java, params))
}


inline fun <reified T : Service> Context.startService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartService(this, T::class.java, params)

inline fun <reified T : Service> AnkoContext<*>.startService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartService(ctx, T::class.java, params)

inline fun <reified T : Service> Fragment.startService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartService(this.requireContext(), T::class.java, params)

inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStopService(this, T::class.java, params)

inline fun <reified T : Service> AnkoContext<*>.stopService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStopService(ctx, T::class.java, params)

inline fun <reified T : Service> Fragment.stopService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStopService(this.requireContext(), T::class.java, params)

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
    AnkoInternals.createIntent(this, T::class.java, params)

inline fun <reified T : Any> AnkoContext<*>.intentFor(vararg params: Pair<String, Any?>): Intent =
    AnkoInternals.createIntent(ctx, T::class.java, params)

inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
    AnkoInternals.createIntent(this.requireContext(), T::class.java, params)

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
@Deprecated(
    message = "Deprecated in Android",
    replaceWith = ReplaceWith("org.jetbrains.anko.newDocument")
)
inline fun Intent.clearWhenTaskReset(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_DOCUMENT] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newDocument(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
}

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }

inline fun AnkoContext<*>.browse(url: String, newTask: Boolean = false) = ctx.browse(url, newTask)

inline fun Fragment.browse(url: String, newTask: Boolean = false) =
    this.requireActivity().browse(url, newTask)

fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        return true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        return false
    }
}

inline fun AnkoContext<*>.share(text: String, subject: String = "", title: String? = null) =
    ctx.share(text, subject, title)

inline fun Fragment.share(text: String, subject: String = "", title: String? = null) =
    this.requireActivity().share(text, subject, title)

fun Context.share(text: String, subject: String = "", title: String? = null): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, title))
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }
}

inline fun AnkoContext<*>.email(email: String, subject: String = "", text: String = "") =
    ctx.email(email, subject, text)

inline fun Fragment.email(email: String, subject: String = "", text: String = "") =
    this.requireActivity().email(email, subject, text)

fun Context.email(email: String, subject: String = "", text: String = ""): Boolean {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    if (subject.isNotEmpty())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    if (text.isNotEmpty())
        intent.putExtra(Intent.EXTRA_TEXT, text)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false

}

inline fun AnkoContext<*>.makeCall(number: String): Boolean = ctx.makeCall(number)

inline fun Fragment.makeCall(number: String): Boolean = this.requireActivity().makeCall(number)

fun Context.makeCall(number: String): Boolean {
    try {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

inline fun AnkoContext<*>.sendSMS(number: String, text: String = ""): Boolean =
    ctx.sendSMS(number, text)

inline fun Fragment.sendSMS(number: String, text: String = ""): Boolean =
    this.requireActivity().sendSMS(number, text)

fun Context.sendSMS(number: String, text: String = ""): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
        intent.putExtra("sms_body", text)
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}


