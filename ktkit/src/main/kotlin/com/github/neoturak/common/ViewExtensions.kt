package com.github.neoturak.common

import android.view.View
import android.widget.SeekBar

/**
 *@author   Hugo
 *@Description
 *@time    2023/1/6 上午12:12
 *@project  miko
 *Think Twice, Code Once!
 */

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

/**
 * Adds an action to be invoked when the progress of the SeekBar is changed.
 *
 * @param action The action to be invoked when the progress changes.
 * @return The [SeekBar.OnSeekBarChangeListener] added to the [SeekBar].
 */
 inline fun SeekBar.onProgressChanged(
    crossinline action: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit
): Unit = setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // Invokes the provided action when the progress is changed
        action.invoke(seekBar, progress, fromUser)
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // No-op, can be overridden separately
    }
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // No-op, can be overridden separately
    }
})

/**
 * Adds an action to be invoked when the user starts tracking the SeekBar (touching the SeekBar).
 *
 * @param action The action to be invoked when the user starts tracking the SeekBar.
 * @return The [SeekBar.OnSeekBarChangeListener] added to the [SeekBar].
 */

 inline fun SeekBar.onStartTrackingTouch(
    crossinline action: (seekBar: SeekBar?) -> Unit
): Unit = setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // No-op, progress change is not handled here
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // Invokes the provided action when the tracking touch starts
        action.invoke(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // No-op, can be overridden separately
    }
})

/**
 * Adds an action to be invoked when the user stops tracking the SeekBar (releasing the touch).
 *
 * @param action The action to be invoked when the user stops tracking the SeekBar.
 * @return The [SeekBar.OnSeekBarChangeListener] added to the [SeekBar].
 */
 inline fun SeekBar.onStopTrackingTouch(
    crossinline action: (seekBar: SeekBar?) -> Unit
): Unit = setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // No-op, can be overridden separately
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // No-op, can be overridden separately
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // Invokes the provided action when the tracking touch stops
        action.invoke(seekBar)
    }
})


/**
 * Adds a custom listener to the SeekBar to handle progress changes and tracking touch events.
 *
 * @param onProgressChanged Action to be invoked when the progress of the SeekBar changes.
 * @param onStartTrackingTouch Action to be invoked when the user starts interacting with the SeekBar.
 * @param onStopTrackingTouch Action to be invoked when the user stops interacting with the SeekBar.
 *
 * @return The [SeekBar.OnSeekBarChangeListener] added to the SeekBar.
 */
 inline fun SeekBar.valueChangeListener(
    crossinline onProgressChanged: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit = { _, _, _ -> },
    crossinline onStartTrackingTouch: (seekBar: SeekBar?) -> Unit = {},
    crossinline onStopTrackingTouch: (seekBar: SeekBar?) -> Unit = {}
) {
    // Sets a custom listener to the SeekBar for progress changes and tracking touch events
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        /**
         * Called when the progress of the SeekBar changes.
         *
         * @param seekBar The SeekBar whose progress has changed.
         * @param progress The new progress value of the SeekBar.
         * @param fromUser Whether the progress change was initiated by the user.
         */
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            // Invokes the provided action when the progress of the SeekBar changes
            onProgressChanged.invoke(seekBar, progress, fromUser)
        }

        /**
         * Called when the user starts interacting with the SeekBar.
         *
         * @param seekBar The SeekBar that is being interacted with.
         */
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // Invokes the provided action when the user starts interacting with the SeekBar
            onStartTrackingTouch.invoke(seekBar)
        }

        /**
         * Called when the user stops interacting with the SeekBar.
         *
         * @param seekBar The SeekBar that was being interacted with.
         */
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // Invokes the provided action

        }
    })
}