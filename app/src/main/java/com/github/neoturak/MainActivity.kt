package com.github.neoturak

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.github.neoturak.common.backgroundColor
import com.github.neoturak.databinding.ActivityMainBinding
import com.github.neoturak.ui.immersiveNavigationBar
import com.github.neoturak.ui.immersiveStatusBar
import com.github.neoturak.ui.setNavigationBarColor
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * @author 努尔江
 * Created on: 2023/2/9
 * @project ktkit
 * Description:
 **/

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.backgroundColor =Color.WHITE

        binding.sbButton.cornerBottomLeft
        this.immersiveStatusBar()
        this.setNavigationBarColor(Color.BLUE)
        this.immersiveNavigationBar {

        }
        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.test)
        val drawable = RoundedBitmapDrawableFactory.create(resources,bitmap)
        drawable.isCircular = true
        binding.svShape.setImageDrawable(drawable)
        val defaultDate: Calendar = GregorianCalendar(1980, 0, 1)
        val minDate: Calendar = GregorianCalendar(1900, 0, 1)
        val maxDate: Calendar = GregorianCalendar(2100, 0, 1)

    }
}