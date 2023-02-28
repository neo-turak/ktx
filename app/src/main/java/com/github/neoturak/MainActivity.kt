package com.github.neoturak

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.github.neoturak.common.backgroundColor
import com.github.neoturak.databinding.ActivityMainBinding
import com.github.neoturak.ui.immersiveNavigationBar
import com.github.neoturak.ui.immersiveStatusBar
import com.github.neoturak.ui.setNavigationBarColor
import java.util.UUID

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
        binding.root.backgroundColor =Color.WHITE

        this.immersiveStatusBar()
        this.setNavigationBarColor(Color.BLUE)
        this.immersiveNavigationBar {

        }
    }
}