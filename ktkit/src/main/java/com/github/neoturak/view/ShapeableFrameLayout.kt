package com.github.neoturak.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.neoturak.ktkit.R

/**
 * @author 努尔江
 * Created on: 2023/3/1
 * @project ktkit
 * Description:
 **/

class ShapeableFrameLayout : FrameLayout {

    //corners.
    var cornersRadius = 0f
        set(value) {
            field = value
            invalidate()
        }

    var cornerTopLeft = 0f
        set(value) {
            field = value
            invalidate()
        }

    var cornerTopRight = 0f
        set(value) {
            field = value
            invalidate()
        }
    var cornerBottomLeft = 0f
        set(value) {
            field = value
            invalidate()
        }
    var cornerBottomRight = 0f
        set(value) {
            field = value
            invalidate()
        }

    //strokes
    var strokeColor = 0
        set(value) {
            field = value
            invalidate()
        }

    var strokeWidth = 0f
        set(value) {
            field = value
            invalidate()
        }

    //sold color
    var soldColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun provideWithCare(f: Float): Float {
        if (f == 0f) return cornersRadius
        return f
    }


    private fun initView(context: Context?, attrs: AttributeSet?) {
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.ShapeableFrameLayout)
        //全部边框值
        cornersRadius = ta.getDimension(R.styleable.ShapeableFrameLayout_shape_cornersRadius, 0f)
        //边框-边角
        cornerTopLeft = ta.getDimension(R.styleable.ShapeableFrameLayout_shape_cornerTopLeft, 0F)
        cornerTopRight = ta.getDimension(R.styleable.ShapeableFrameLayout_shape_cornerTopRight, 0F)
        cornerBottomLeft = ta.getDimension(R.styleable.ShapeableFrameLayout_shape_cornerBottomLeft, 0F)
        cornerBottomRight = ta.getDimension(R.styleable.ShapeableFrameLayout_shape_cornerBottomRight, 0F)
        //边框颜色
        strokeColor = ta.getColor(R.styleable.ShapeableFrameLayout_shape_strokeColor, Color.BLUE)
        strokeWidth = ta.getDimension(R.styleable.ShapeableFrameLayout_shape_strokeWidth, 0f)
        //背景颜色
        soldColor = ta.getColor(R.styleable.ShapeableFrameLayout_shape_soldColor, Color.CYAN)
        ta.recycle()
        setAttrs()
    }

    /**
     * 设置属性
     */
    private fun setAttrs() {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        //为了4个角的角度值。
        val corners = floatArrayOf(
            provideWithCare(cornerTopLeft), provideWithCare(cornerTopLeft),
            provideWithCare(cornerTopRight), provideWithCare(cornerTopRight),
            provideWithCare(cornerBottomRight), provideWithCare(cornerBottomRight),
            provideWithCare(cornerBottomLeft), provideWithCare(cornerBottomLeft)
        )
        shape.cornerRadii = corners
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shape.color = ColorStateList.valueOf(soldColor)
            if (strokeWidth != 0f) {
                shape.setStroke(strokeWidth.toInt(), ColorStateList.valueOf(strokeColor))
            }
            this.background = shape
        }
    }

}