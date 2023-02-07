package com.github.neoturak.demo.ktkit.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.common.singleClick

/**
 * @author 努尔江
 * Created on: 2023/2/6
 * @project miko
 * Description:
 **/

class SecondActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        View(this).apply {
            setBackgroundColor(Color.CYAN)
            setContentView(this)
            Log.e(this@SecondActivity.javaClass.simpleName,"${intent.getStringExtra("hi")}")
            setOnClickListener {
                val i = Intent()
                i.putExtra("data","333333")
                setResult(Activity.RESULT_OK,i)
                finish()
            }
        }
    }
}