package com.github.neoturak.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

/**
 * @author 努尔江
 * Created on: 2023/3/2
 * @project ktkit
 * Description:
 **/

class ViewUtils {

    /**
     * @return Gradient.Orientation
     * @param angle int of enum value of the view
     */
    fun realAngle(angle: Int): GradientDrawable.Orientation {
        return when (angle) {
            0 -> GradientDrawable.Orientation.TOP_BOTTOM
            1 -> GradientDrawable.Orientation.TR_BL
            2 -> GradientDrawable.Orientation.RIGHT_LEFT
            3 -> GradientDrawable.Orientation.BR_TL
            4 -> GradientDrawable.Orientation.BOTTOM_TOP
            5 -> GradientDrawable.Orientation.BL_TR
            6 -> GradientDrawable.Orientation.LEFT_RIGHT
            7 -> GradientDrawable.Orientation.TL_BR
            else -> {
                GradientDrawable.Orientation.LEFT_RIGHT
            }
        }
    }

    /**
     * 获取2个颜色的中间值
     * calculate the middle color of two colors
     * @param color1 第一个颜色
     * @param color2 第二个颜色
     * @return 中间颜色
     */
     fun middleColor(color1: Int, color2: Int): Int {
        val red1 = Color.red(color1)
        val green1 = Color.green(color1)
        val blue1 = Color.blue(color1)
        val red2 = Color.red(color2)
        val green2 = Color.green(color2)
        val blue2 = Color.blue(color2)
        val red = (red1 + red2) / 2
        val green = (green1 + green2) / 2
        val blue = (blue1 + blue2) / 2
        return Color.rgb(red, green, blue)
    }
}