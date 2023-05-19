package com.github.neoturak.common


/**
 *@author   Hugo
 *@Description
 *@time    1/24/22 7:35 PM
 *@project  client-juran
 *Think Twice, Code Once!
 */
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Execute [f] on the application UI thread.
 */
fun Context.runOnUiThread(f: Context.() -> Unit) {
    if (Looper.getMainLooper() === Looper.myLooper()) f() else ContextHelper.handler.post { f() }
}

/**
 * Execute [f] on the application UI thread.
 */

inline fun Fragment.runOnUiThread(crossinline f: () -> Unit) {
    activity?.runOnUiThread { f() }
}

class AnkoAsyncContext<T>(val weakRef: WeakReference<T>)

/**
 * Execute [f] on the application UI thread.
 * If the [doAsync] receiver still exists (was not collected by GC),
 *  [f] gets it as a parameter ([f] gets null if the receiver does not exist anymore).
 */
fun <T> AnkoAsyncContext<T>.onComplete(f: (T?) -> Unit) {
    val ref = weakRef.get()
    if (Looper.getMainLooper() === Looper.myLooper()) {
        f(ref)
    } else {
        ContextHelper.handler.post { f(ref) }
    }
}

private val crashLogger = { throwable : Throwable -> throwable.printStackTrace() }
/**
 * Execute [task] asynchronously.
 *
 * @param exceptionHandler optional exception handler.
 *  If defined, any exceptions thrown inside [task] will be passed to it. If not, exceptions will be ignored.
 * @param task the code to execute asynchronously.
 */
fun <T> T.doAsync(
    exceptionHandler: ((Throwable) -> Unit)? = crashLogger,
    task: AnkoAsyncContext<T>.() -> Unit
): Future<Unit> {
    val context = AnkoAsyncContext(WeakReference(this))
    return BackgroundExecutor.submit {
        return@submit try {
            context.task()
        } catch (thr: Throwable) {
            val result = exceptionHandler?.invoke(thr)
            if (result != null) {
                result
            } else {
                Unit
            }
        }
    }
}

fun <T> T.doAsync(
    exceptionHandler: ((Throwable) -> Unit)? = crashLogger,
    executorService: ExecutorService,
    task: AnkoAsyncContext<T>.() -> Unit
): Future<Unit> {
    val context = AnkoAsyncContext(WeakReference(this))
    return executorService.submit<Unit> {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
        }
    }
}

fun <T, R> T.doAsyncResult(
    exceptionHandler: ((Throwable) -> Unit)? = crashLogger,
    task: AnkoAsyncContext<T>.() -> R
): Future<R> {
    val context = AnkoAsyncContext(WeakReference(this))
    return BackgroundExecutor.submit {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
            throw thr
        }
    }
}

fun <T, R> T.doAsyncResult(
    exceptionHandler: ((Throwable) -> Unit)? = crashLogger,
    executorService: ExecutorService,
    task: AnkoAsyncContext<T>.() -> R
): Future<R> {
    val context = AnkoAsyncContext(WeakReference(this))
    return executorService.submit<R> {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
            throw thr
        }
    }
}

internal object BackgroundExecutor {
    var executor: ExecutorService =
        Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

    fun <T> submit(task: () -> T): Future<T> = executor.submit(task)

}

private object ContextHelper {
    val handler = Handler(Looper.getMainLooper())
}