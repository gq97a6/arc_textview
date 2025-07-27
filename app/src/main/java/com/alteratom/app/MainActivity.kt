package com.alteratom.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alteratom.lib.ArcTextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.ProtectionDomain
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arcTextView = findViewById<ArcTextView>(R.id.random_number)

        lifecycleScope.launch {
            while (true) {
                delay(500)
                arcTextView.text = Random.nextInt(100000000, 999999999).toString()
                arcTextView.invalidate()
            }
        }
    }
}