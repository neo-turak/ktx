package com.github.neoturak.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.github.neoturak.ktkit.R
import com.google.android.material.appbar.AppBarLayout

/**
 * @author 努尔江
 * Created on: 2023/2/28
 * @project kt-kit
 * Description: 带属性的view
 **/

class ShapeableAppBarLayout : AppBarLayout {

    //corners.
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

    //strokes
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

    //sold color
    var soldColor: Int = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    //start color
    var startColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    //end color
    var endColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    //center color
    var centerColor = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    //angle
    var angle = 0
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }
    var cutChild = false
        set(value) {
            field = value
            invalidate()
            setAttrs()
        }

    private val path = Path()

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int=0) : super(
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
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.ShapeableAppBarLayout)
        //全部边框值
        cornersRadius = ta.getDimension(R.styleable.ShapeableAppBarLayout_shape_cornersRadius, 0f)
        //边框-边角
        cornerTopLeft = ta.getDimension(R.styleable.ShapeableAppBarLayout_shape_cornerTopLeft, 0F)
        cornerTopRight = ta.getDimension(R.styleable.ShapeableAppBarLayout_shape_cornerTopRight, 0F)
        cornerBottomLeft = ta.getDimension(R.styleable.ShapeableAppBarLayout_shape_cornerBottomLeft, 0F)
        cornerBottomRight = ta.getDimension(R.styleable.ShapeableAppBarLayout_shape_cornerBottomRight, 0F)
        //边框颜色
        strokeColor = ta.getColor(R.styleable.ShapeableAppBarLayout_shape_strokeColor, Color.WHITE)
        strokeWidth = ta.getDimension(R.styleable.ShapeableAppBarLayout_shape_strokeWidth, 0f)
        //背景颜色
        soldColor = ta.getColor(R.styleable.ShapeableAppBarLayout_shape_soldColor, Color.WHITE)
        //开始颜色
        startColor =
            ta.getColor(R.styleable.ShapeableAppBarLayout_gradient_startColor, 0)
        //中间颜色
        centerColor =
            ta.getColor(R.styleable.ShapeableAppBarLayout_gradient_centerColor, 0)
        //结束颜色
        endColor = ta.getColor(R.styleable.ShapeableAppBarLayout_gradient_endColor, 0)
        //角度值
        angle = ta.getInteger(R.styleable.ShapeableAppBarLayout_gradient_angle, 6)
        //是否要剪切child
        cutChild = ta.getBoolean(R.styleable.ShapeableAppBarLayout_cut_child, false)
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
        val realAngle = ViewUtils().realAngle(angle)
        if (startColor == endColor && startColor == centerColor) {//没有渐变色
            shape.color = ColorStateList.valueOf(soldColor)
        } else {//有渐变色
            if (endColor == 0) {
                endColor = Color.WHITE
            }
            //如果中间颜色没有，那么按照官方的逻辑取中间颜色。
            if (centerColor == 0) {
                centerColor =ViewUtils().middleColor(startColor,endColor)
            }
            shape.colors = intArrayOf(startColor, centerColor, endColor)
            shape.orientation = realAngle
        }
        //描边。
        if (strokeWidth != 0f && strokeColor != Color.TRANSPARENT) {
            shape.setStroke(strokeWidth.toInt(), ColorStateList.valueOf(strokeColor))
        }
        this.background = shape
    }

    override fun dispatchDraw(canvas: Canvas?) {
        val save = canvas?.save()
        path.reset()
        val width = width.toFloat()
        val height = height.toFloat()
        //需要剪切child的时候。
        if (cutChild) {
            if (cornersRadius != 0f) {
                path.addRoundRect(
                    0f,
                    0f,
                    width,
                    height,
                    cornersRadius,
                    cornersRadius,
                    Path.Direction.CW
                )
            } else {
                val rect = RectF(0f, 0f, getWidth().toFloat(), getHeight().toFloat())
                rect.set(0f, 0f, width, height)
                path.addRoundRect(
                    rect,
                    floatArrayOf(
                        cornerTopLeft,
                        cornerTopLeft,
                        cornerTopRight,
                        cornerTopRight,
                        cornerBottomRight,
                        cornerBottomRight,
                        cornerBottomLeft,
                        cornerBottomLeft
                    ),
                    Path.Direction.CW
                )
            }
            canvas?.clipPath(path)
        }
        super.dispatchDraw(canvas)
        save?.let { canvas.restoreToCount(it) }
    }
}