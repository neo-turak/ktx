package com.github.neoturak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.databinding.ActivityShapeableViewBinding

class ActivtyShapeableView:AppCompatActivity() {
private lateinit var binding:ActivityShapeableViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShapeableViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}