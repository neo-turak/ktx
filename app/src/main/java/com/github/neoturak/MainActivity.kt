package com.github.neoturak

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.github.neoturak.common.backgroundColor
import com.github.neoturak.databinding.ActivityMainBinding

/**
 * @author 努尔江
 * Created on: 2023/2/9
 * @project ktkit
 * Description:
 **/

class MainActivity : AppCompatActivity() {

    private var notification: Any? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.backgroundColor = Color.WHITE

        binding.sbButton.cornerBottomLeft
/*        this.immersiveStatusBar()
        this.setNavigationBarColor(Color.BLUE)
        this.immersiveNavigationBar {}*/
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
        val drawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        drawable.isCircular = true

        Glide.with(this)
            .load("https://images.unsplash.com/reserve/Af0sF2OS5S5gatqrKzVP_Silhoutte.jpg?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80")
            .into(binding.civImage)
    }
}