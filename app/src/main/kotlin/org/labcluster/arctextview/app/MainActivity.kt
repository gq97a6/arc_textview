package org.labcluster.arctextview.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.labcluster.arctextview.ArcTextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arcTextView = findViewById<ArcTextView>(R.id.random_number)

        lifecycleScope.launch {
            while (true) {
                arcTextView.text = Random.nextInt(100000000, 999999999).toString()
                arcTextView.invalidate()
                delay(1000)
            }
        }
    }
}