package com.github.neoturak

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.plus
import com.github.neoturak.common.dp2px

class MonthView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }

    private val mPath = Path()
    private var arcRight = RectF()
    private var arcLeft = RectF()
    private var cutOfPercent = 30

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("DrawAllocation")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = Color.parseColor("#413B46")
        canvas!!.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, paint)

        when (cutOfPercent) {
            0 -> {
                paint.color = Color.parseColor("#696969")
                canvas.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, paint)
            }
            100 -> {
                paint.color = Color.parseColor("#D06A6A")
                canvas.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, paint)
            }
            else -> {

                paint.color = Color.parseColor("#BB9D50")

                // Define the path of the lune shape
                if (height > width) {//半径是width
                    arcRight = RectF(
                        0f,
                        (height - width) / 2f,
                        width.toFloat(),
                        (height + width) / 2f,
                    )
                    arcLeft = RectF(
                        0f,
                        (height - width) / 2f,
                        width - width * cutOfPercent / 100f,
                        (height + width) / 2f,
                    )
                } else { //半径是height=60, width=80
                    arcRight = RectF(
                        0f,
                        (height - width) / 2f,
                        width.toFloat(),
                        (height + width) / 2f,
                    )
                    arcLeft = RectF(
                        0f,
                        (height - width) / 2f,
                        width - width * cutOfPercent / 100f,
                        (height + width) / 2f,
                    )
                }
                //path handler
                mPath.reset()
                mPath.arcTo(
                    arcRight,
                    -90f, 180f
                )
                mPath.arcTo(
                    arcLeft,
                    90f, -180f
                )
                mPath.close()
                canvas.save()
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
                canvas.clipPath(mPath)
                paint.color = Color.parseColor("#BB9E54")
                canvas.drawCircle(width / 2f, height / 2f, minOf(width, height) / 2f, paint)
                canvas.restore()
            }
        }
    }

    fun currentMonth(@androidx.annotation.IntRange(from = 0, to = 15) range: Int) {
        cutOfPercent = when (range) {
            0 -> 0
            15 -> 100
            else -> {
                ((1 + range) / 16f * 100).toInt()
            }
        }
    }
}