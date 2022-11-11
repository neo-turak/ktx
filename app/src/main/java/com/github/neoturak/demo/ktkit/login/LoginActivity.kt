package com.github.neoturak.demo.ktkit.login

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.github.neoturak.demo.ktkit.R


/**
 * <pre>
 *     author: dhl
 *     date  : 2021/7/18
 *     desc  :
 * </pre>
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LayoutInflater.from(this).inflate(R.layout.activity_login,null)
        setContentView(root)
    }
}
