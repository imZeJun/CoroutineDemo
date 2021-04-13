package com.lizejun.coroutine.demo.chapter9

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    main1()
}

suspend fun main1() {
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
        var index = 0
        while (true) {
            delay(1000)
            send(index++)
        }
    }
    val sendChannel: SendChannel<Int> = GlobalScope.actor {
        while (true) {
            val element = receive()
            println("$element")
        }
    }
    for (i in 1..10) {
        val element = receiveChannel.receive()
        println("$element")
    }
    println("-----")
    for (i in 1..10) {
        sendChannel.send(i)
    }
}

suspend fun main2() {
    val channel = Channel<Int>(3)
    val producer = GlobalScope.launch {
        List(5) {
            channel.send(it)
            println("send $it")
        }
        channel.close()
        println("close channel, closeForSend=${channel.isClosedForSend}, closeForReceive=${channel.isClosedForReceive}")
    }
    val consumer = GlobalScope.launch {
        for (element in channel) {
            println("$element")
            delay(1000)
        }
        println("after consume, closeForSend=${channel.isClosedForSend}, closeForReceive=${channel.isClosedForReceive}")
    }
    producer.join()
    consumer.join()
}

suspend fun main3() {
    val broadcastChannel = BroadcastChannel<Int>(5)
    val producer = GlobalScope.launch {
        delay(1000)
        List(5) {
            broadcastChannel.send(it)
            println("send $it")
        }
        println("send close")
        broadcastChannel.close()
    }
    List(3) {
        println("list $it")
        GlobalScope.launch {
            println("list launch $it")
            val receiveChannel = broadcastChannel.openSubscription()
            for (element in receiveChannel) {
                println("$it receive: $element")
                delay(100)
            }
        }
    }.forEach {
        it.join()
    }
    producer.join()
}

suspend fun main4() {
    println("---channel 模拟序列生成器---")
    val channel = GlobalScope.produce(Dispatchers.Unconfined) {
        println("A")
        send(1)
        println("B")
        send(2)
        println("done")
    }
    for (item in channel) {
        println("get $item")
    }
    println("---sequence---")
    val sequence = sequence {
        println("A")
        yield(1)
        println("B")
        yield(2)
        println("done")
    }
    for (item in sequence) {
        println("get $item")
    }
}