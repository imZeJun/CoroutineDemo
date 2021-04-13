package com.lizejun.coroutine.demo.chapter8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CoroutinePresenter(private val scope: CoroutineScope): CoroutineScope by scope {

    fun getUserData() {
        launch {

        }
    }

    /**
     * 嵌套作用域声明。
     */
    suspend fun getUserDataOther() = coroutineScope {
        launch {

        }
    }
}