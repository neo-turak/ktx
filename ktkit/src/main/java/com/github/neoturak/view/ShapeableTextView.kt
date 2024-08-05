package com.github.neoturak.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.github.neoturak.ktx.R

/**
 * @author 努尔江
 * Created on: 2023/2/28
 * @project ktx
 * Description: 带属性的view
 **/
class ShapeableTextView : AppCompatTextView {

    // Corners
    var cornersRadius = 0f
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }
    var cornerTopLeft = 0f
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }
    var cornerTopRight = 0f
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }
    var cornerBottomLeft = 0f
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }
    var cornerBottomRight = 0f
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    // Strokes
    var strokeColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    var strokeWidth = 0f
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    // Solid color
    var soldColor: Int = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    // Gradient colors
    var startColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    var centerColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    var endColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    // Angle
    var angle = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context, attrs)
    }


    private fun provideWithCare(f: Float): Float {
        return if (f == 0f) cornersRadius else f
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.ShapeableTextView).apply {
            try {
                // Get attributes
                cornersRadius = getDimension(R.styleable.ShapeableTextView_shape_cornersRadius, 0f)
                cornerTopLeft = getDimension(R.styleable.ShapeableTextView_shape_cornerTopLeft, 0f)
                cornerTopRight = getDimension(R.styleable.ShapeableTextView_shape_cornerTopRight, 0f)
                cornerBottomLeft = getDimension(R.styleable.ShapeableTextView_shape_cornerBottomLeft, 0f)
                cornerBottomRight = getDimension(R.styleable.ShapeableTextView_shape_cornerBottomRight, 0f)
                strokeColor = getColor(R.styleable.ShapeableTextView_shape_strokeColor, Color.WHITE)
                strokeWidth = getDimension(R.styleable.ShapeableTextView_shape_strokeWidth, 0f)
                soldColor = getColor(R.styleable.ShapeableTextView_shape_soldColor, Color.WHITE)
                startColor = getColor(R.styleable.ShapeableTextView_gradient_startColor, 0)
                centerColor = getColor(R.styleable.ShapeableTextView_gradient_centerColor, 0)
                endColor = getColor(R.styleable.ShapeableTextView_gradient_endColor, 0)
                angle = getInteger(R.styleable.ShapeableTextView_gradient_angle, 6)
            } finally {
                recycle()
            }
        }
        setAttrs()
    }

    /**
     * 设置属性
     */
    private fun setAttrs() {
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE

            // Set corner radii
            cornerRadii = floatArrayOf(
                provideWithCare(cornerTopLeft), provideWithCare(cornerTopLeft),
                provideWithCare(cornerTopRight), provideWithCare(cornerTopRight),
                provideWithCare(cornerBottomRight), provideWithCare(cornerBottomRight),
                provideWithCare(cornerBottomLeft), provideWithCare(cornerBottomLeft)
            )

            // Handle gradient colors
            if (startColor == endColor && startColor == centerColor) {
                // No gradient
                color = ColorStateList.valueOf(soldColor)
            } else {
                // Gradient
                if (endColor == 0) endColor = Color.WHITE
                if (centerColor == 0) centerColor = ViewUtils().middleColor(startColor, endColor)

                colors = intArrayOf(startColor, centerColor, endColor)
                orientation = ViewUtils().realAngle(angle)
            }

            // Set stroke
            if (strokeWidth != 0f && strokeColor != Color.TRANSPARENT) {
                setStroke(strokeWidth.toInt(), ColorStateList.valueOf(strokeColor))
            }
        }

        this.background = shape
    }
}
