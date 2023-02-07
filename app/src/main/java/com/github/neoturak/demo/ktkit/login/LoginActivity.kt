package com.github.neoturak.demo.ktkit.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.common.find
import com.github.neoturak.common.registerLauncher
import com.github.neoturak.common.startActivityWithResult
import com.github.neoturak.demo.ktkit.R


/**
 * <pre>
 *     author: dhl
 *     date  : 2021/7/18
 *     desc  :
 * </pre>
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var c: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        c = registerLauncher {
            Log.e(this.javaClass.simpleName,"返回数据:${it?.getStringExtra("data")}")
        }
        val root = LayoutInflater.from(this).inflate(R.layout.activity_login, null)
        setContentView(root)
        find<Button>(R.id.button).setOnClickListener {
            startActivityWithResult<SecondActivity>(c, Pair("hi", "1111111111111"))
        }
    }
}
