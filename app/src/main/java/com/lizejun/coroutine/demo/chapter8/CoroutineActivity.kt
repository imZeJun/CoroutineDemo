package com.lizejun.coroutine.demo.chapter8

import android.os.Bundle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CoroutineActivity: ScopedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {

        }
    }

    suspend fun anotherOps() = coroutineScope {

    }
}