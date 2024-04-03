package com.github.neoturak.common

import android.view.View

/**
 *@author   Hugo
 *@Description
 *@time    2023/1/6 上午12:12
 *@project  miko
 *Think Twice, Code Once!
 */

/**
 * Single click
 *
 * @param debounceInterval 规定时间内的点击事件都会被拦截。
 * @param listenerBlock 功能
 * @receiver
 */
fun View.singleClick(debounceInterval: Long, listenerBlock: (View) -> Unit) =
    setOnClickListener(DebounceOnClickListener(debounceInterval, listenerBlock))

/**
 * Single click
 *
 * @param listenerBlock 500毫秒内拦截。
 * @receiver
 */
fun View.singleClick(listenerBlock: (View) -> Unit) =
    setOnClickListener(DebounceOnClickListener(500, listenerBlock))


class DebounceOnClickListener<T : View>(
    private val interval: Long,
    private val listenerBlock: (T) -> Unit
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val time = System.currentTimeMillis()
        if (time - lastClickTime >= interval) {
            lastClickTime = time
            @Suppress("UNCHECKED_CAST")
            listenerBlock(v as T)
        }
    }
}

/**
 * Single click
 *
 * @param T Type of the view
 * @param debounceInterval Interval to debounce clicks
 * @param listenerBlock Functionality
 * @receiver View on which the click event occurs
 */
fun <T : View> T.singleClick(debounceInterval: Long, listenerBlock: (T) -> Unit) =
    setOnClickListener(DebounceOnClickListener(debounceInterval, listenerBlock))

/**
 * Single click with default interval of 500 milliseconds
 *
 * @param T Type of the view
 * @param listenerBlock Functionality
 * @receiver View on which the click event occurs
 */
fun <T : View> T.singleClick(listenerBlock: (T) -> Unit) =
    setOnClickListener(DebounceOnClickListener(500, listenerBlock))