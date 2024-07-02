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
     * Calculate the middle color of two colors including the alpha channel
     * @param color1 第一个颜色
     * @param color2 第二个颜色
     * @return 中间颜色
     */
    fun middleColor(color1: Int, color2: Int): Int {
        // Extract the alpha, red, green, and blue components from the first color
        val alpha1 = Color.alpha(color1)
        val red1 = Color.red(color1)
        val green1 = Color.green(color1)
        val blue1 = Color.blue(color1)

        // Extract the alpha, red, green, and blue components from the second color
        val alpha2 = Color.alpha(color2)
        val red2 = Color.red(color2)
        val green2 = Color.green(color2)
        val blue2 = Color.blue(color2)

        // Calculate the average of the alpha components
        val alpha = (alpha1 + alpha2) / 2
        // Calculate the average of the red components
        val red = (red1 + red2) / 2
        // Calculate the average of the green components
        val green = (green1 + green2) / 2
        // Calculate the average of the blue components
        val blue = (blue1 + blue2) / 2
        // Combine the averaged alpha, red, green, and blue components into a new color
        return Color.argb(alpha, red, green, blue)
    }
}