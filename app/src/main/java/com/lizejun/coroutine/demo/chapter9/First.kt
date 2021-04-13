package com.lizejun.coroutine.demo.chapter9

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    val channel = Channel<Int>()

    val producer = GlobalScope.launch {
        var index = 0
        while (true) {
            channel.send(index++)
            delay(1000)
        }
    }

    val receiver = GlobalScope.launch {
        val iterator = channel.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            println("$element")
        }
    }

    println("before producer")
    producer.join()
    println("after producer")
    //receiver.join()
    println("after receiver")
}