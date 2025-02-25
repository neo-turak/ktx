package com.github.neoturak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.databinding.ActivityAnkoBinding

class AnkoActivity:AppCompatActivity() {
    private lateinit var binding: ActivityAnkoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnkoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}