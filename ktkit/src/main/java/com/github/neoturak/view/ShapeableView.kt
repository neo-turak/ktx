package com.github.neoturak.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.neoturak.ktkit.R

/**
 * @author 努尔江
 * Created on: 2023/2/28
 * @project kt-kit
 * Description: 带属性的view
 **/

class ShapeableView : View {
    private var mRectF: RectF = RectF()

    //corners.
    private var cornersRadius = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var cornerTopLeft = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var cornerTopRight = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var cornerBottomLeft = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var cornerBottomRight = 0f
        set(value) {
            field = value
            invalidate()
        }

    //strokes
    private var strokeColor = 0
        set(value) {
            field = value
            invalidate()
        }

    private var strokeWidth = 0f
        set(value) {
            field = value
            invalidate()
        }

    //sold color
    private var soldColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()

    constructor(context: Context?) : super(context) {
        initView(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //为了4个角的角度值。
        val corners = floatArrayOf(
            provideWithCare(cornerTopLeft), provideWithCare(cornerTopLeft),
            provideWithCare(cornerTopRight), provideWithCare(cornerTopRight),
            provideWithCare(cornerBottomRight), provideWithCare(cornerBottomRight),
            provideWithCare(cornerBottomLeft), provideWithCare(cornerBottomLeft)
        )

        //底色(背景色)
        //style 是线风格。
        mPaint.style = Paint.Style.FILL
        mPaint.color = soldColor
        mPaint.strokeWidth = 1f
        mRectF.left = strokeWidth / 2f
        mRectF.top = strokeWidth / 2f
        mRectF.right = width - strokeWidth / 2f
        mRectF.bottom = height - strokeWidth / 2f
        mPath.addRoundRect(mRectF, corners, Path.Direction.CW)
        canvas!!.drawPath(mPath, mPaint)

        //如果为0，不需要绘制边
        if (strokeWidth != 0f) {
            //切换style属性实心。
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = strokeWidth
            mPaint.color = strokeColor
            mRectF.left = strokeWidth / 2f
            mRectF.top = strokeWidth / 2f
            mRectF.right = width - strokeWidth / 2f
            mRectF.bottom = height - strokeWidth / 2f
            mPath.addRoundRect(mRectF, corners, Path.Direction.CW)
            canvas.drawPath(mPath, mPaint)
        }
    }

    private fun provideWithCare(f: Float): Float {
        if (f == 0f) return cornersRadius
        return f
    }


    private fun initView(context: Context?, attrs: AttributeSet?) {
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.ShapeableView)
        //全部边框值
        cornersRadius = ta.getDimension(R.styleable.ShapeableView_shape_cornersRadius, 0f)
        //边框-边角
        cornerTopLeft = ta.getDimension(R.styleable.ShapeableView_shape_cornerTopLeft, 0F)
        cornerTopRight = ta.getDimension(R.styleable.ShapeableView_shape_cornerTopRight, 0F)
        cornerBottomLeft = ta.getDimension(R.styleable.ShapeableView_shape_cornerBottomLeft, 0F)
        cornerBottomRight = ta.getDimension(R.styleable.ShapeableView_shape_cornerBottomRight, 0F)
        //边框颜色
        strokeColor = ta.getColor(R.styleable.ShapeableView_shape_strokeColor, Color.BLUE)
        strokeWidth = ta.getDimension(R.styleable.ShapeableView_shape_strokeWidth, 0f)
        //背景颜色
        soldColor = ta.getColor(R.styleable.ShapeableView_shape_soldColor, Color.CYAN)
        ta.recycle()
    }

}