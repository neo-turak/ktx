package com.github.neoturak

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.github.neoturak.common.dp2px

class PokerView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var number = 5
    private var type = ""
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(250, 350)
    }

    init {
        type = POKER_HEART
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSmallUp(canvas)
        drawSmallDown(canvas)
        drawBig(canvas)
    }

    private fun drawBig(canvas: Canvas?) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width.coerceAtMost(height) / 2f
        textPaint.textSize = radius
        textPaint.textAlign = Paint.Align.CENTER
        canvas!!.drawText(type, centerX, centerY + radius / 4, textPaint)
        canvas.save()
    }

    private fun drawSmallUp(canvas: Canvas?) {
        val radius = width.coerceAtMost(height) / 8f
        textPaint.textSize = radius
        textPaint.textAlign = Paint.Align.CENTER
        canvas!!.drawText(
            type,
            context.dp2px(10f),
            context.dp2px(30f) + radius / 8,
            textPaint
        )
        canvas.drawText(
            number.toString(),
            context.dp2px(9f),
            context.dp2px(15f) + radius / 8,
            textPaint
        )
        canvas.save()
    }

    private fun drawSmallDown(canvas: Canvas?) {
        val radius = width.coerceAtMost(height) / 8f
        textPaint.textSize = radius
        textPaint.textAlign = Paint.Align.CENTER
        val centerX = width / 2f
        val centerY = height / 2f
        canvas!!.rotate(180f, centerX, centerY)
        canvas.drawText(
            type,
            context.dp2px(10f),
            context.dp2px(30f) + radius / 8,
            textPaint
        )
        canvas.drawText(
            number.toString(),
            context.dp2px(9f),
            context.dp2px(15f) + radius / 8,
            textPaint
        )
        canvas.restore()
    }


    companion object {
        const val POKER_HEART = "♥"
        const val POKER_DIAMOND = "♦"
        const val POKER_CLUB = "♠"
        const val POKER_SPADE = "♣"
    }
}