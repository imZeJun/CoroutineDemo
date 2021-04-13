package com.lizejun.coroutine.demo.chapter3

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun main() {
    runBlocking {
        flow {
            listOf(1, 2, 3).forEach {
                println("thread=" + Thread.currentThread().name)
                emit(it)
            }
        }.flowOn(Dispatchers.IO).collect {
            println("it=$it")
        }
    }

}

