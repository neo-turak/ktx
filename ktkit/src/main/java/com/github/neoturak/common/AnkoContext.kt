package com.github.neoturak.common


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager


interface AnkoContext<out T> : ViewManager {
    val ctx: Context
    val owner: T
    val view: View

    override fun updateViewLayout(view: View, params: ViewGroup.LayoutParams) {
        throw UnsupportedOperationException()
    }

    override fun removeView(view: View) {
        throw UnsupportedOperationException()
    }

}

