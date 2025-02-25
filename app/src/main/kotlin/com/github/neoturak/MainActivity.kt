package com.github.neoturak

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.common.backgroundColor
import com.github.neoturak.common.onProgressChanged
import com.github.neoturak.common.setStatusBarColor
import com.github.neoturak.databinding.ActivityMainBinding

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
        binding.root.backgroundColor = Color.WHITE

        this.setStatusBarColor(R.color.design_default_color_secondary)

        binding.seekbar.onProgressChanged { sb, progress, fromUser ->
        }
    }
}