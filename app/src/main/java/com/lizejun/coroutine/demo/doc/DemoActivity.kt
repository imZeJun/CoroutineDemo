package com.lizejun.coroutine.demo.doc

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lizejun.coroutine.demo.R
import com.lizejun.coroutine.demo.doc.api.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception

class DemoActivity: AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        findViewById<Button>(R.id.bt_network).setOnClickListener {
            requestNetwork()
        }
    }

    private fun requestNetwork() {
        launch {
            try {
                val news = NewsRepository.instance.requestNews()
                news.data.forEach {
                    println(it)
                }
            } catch(e: Exception) {
                Toast.makeText(this@DemoActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}