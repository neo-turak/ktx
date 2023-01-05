package com.github.neoturak.common

import android.view.View
import com.github.neoturak.ui.clickFlow

/**
 *@author   Hugo
 *@Description
 *@time    2023/1/6 上午12:12
 *@project  miko
 *Think Twice, Code Once!
 */
class DebounceOnClickListener(
    private val interval: Long,
    private val listenerBlock: (View) -> Unit
): View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val time = System.currentTimeMillis()
        if (time - lastClickTime >= interval) {
            lastClickTime = time
            listenerBlock(v)
        }
    }
}

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