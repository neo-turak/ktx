package com.github.neoturak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.databinding.ActivityMainBinding

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

        val main = MainFragment()
        supportFragmentManager.beginTransaction()
            .add(binding.home.id,main)
            .commit()
    }
}