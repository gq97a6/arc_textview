package com.alteratom.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.arctextview.ArcTextView
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arcTextView = findViewById<ArcTextView>(R.id.random_number)

        var setRandomNumber: () -> Unit = {}
        setRandomNumber = {
            arcTextView.text = Random.nextInt(100000000, 999999999).toString()
            arcTextView.invalidate()

            Handler(Looper.getMainLooper()).postDelayed({
                setRandomNumber()
            }, 500)
        }

        setRandomNumber()
    }
}