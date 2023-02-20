package com.github.neoturak

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.databinding.ActivityMainBinding
import com.github.neoturak.ui.immersiveNavigationBar
import com.github.neoturak.ui.immersiveStatusBar
import com.github.neoturak.ui.setNavigationBarColor

/**
 * @author 努尔江
 * Created on: 2023/2/9
 * @project ktkit
 * Description:
 **/

class MainActivity:AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        this.immersiveStatusBar()
        this.setNavigationBarColor(Color.BLUE)
        this.immersiveNavigationBar {

        }
    }
}