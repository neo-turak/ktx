package com.github.neoturak

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.common.singleClick
import com.github.neoturak.databinding.ActivityLoginBinding
import com.github.neoturak.ui.startActivity
import kotlin.random.Random

/**
 * @author 努尔江
 * Created on: 2023/2/9
 * @project ktkit
 * Description:
 **/

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val random = Random.nextInt(10,20)
        binding.button.singleClick {
            val i = Intent()
            i.putExtra("hola","congratulation! $random")
            setResult(Activity.RESULT_OK,i)
            finish()
            startActivity<SecondActivity>()
        }
    }
}