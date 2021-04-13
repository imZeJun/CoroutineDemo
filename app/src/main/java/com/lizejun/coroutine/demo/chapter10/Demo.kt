package com.lizejun.coroutine.demo.chapter10

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlin.random.Random

suspend fun main() {
    main5()
}

fun CoroutineScope.fizz() = produce {
    while (true) {
        delay(100)
        send("Fizz")
    }
}

fun CoroutineScope.buzz() = produce<String> {
    while (true) {
        delay(500)
        send("Buzz!")
    }
}

suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
    select<Unit> {
        fizz.onReceive {
            println("fizz -> $it")
        }
        buzz.onReceive {
            println("buzz -> $it")
        }
    }
}

fun main6() = runBlocking {
    val fizz = fizz()
    val buzz = buzz()
    repeat(7) {
        selectFizzBuzz(fizz, buzz)
    }
    coroutineContext.cancelChildren()
}

suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
    select<String> {
        a.onReceiveOrNull {
            if (it == null) "channel a is closed" else "a -> $it"
        }
        b.onReceiveOrNull {
            if (it == null) "channel b is closed" else "b -> $it"
        }
    }


fun main2() = runBlocking {
    val a = produce {
        repeat(4) {
            send("hello $it")
        }
    }
    val b = produce {
        repeat(4) {
            send("world $it")
        }
    }
    repeat(8) {
        println(selectAorB(a, b))
    }
    coroutineContext.cancelChildren()
}

fun CoroutineScope.produceNumber(side: SendChannel<Int>) = produce<Int> {
    for (index in 1..10) {
        delay(100)
        select<Unit> {
            /**
             * 主通道无法跟上时，将会发送到 side 通道。
             */
            onSend(index) { println("send by main channel $index") }
            side.onSend(index) { println("send by side $index") }
        }
    }
}

fun main3() = runBlocking {
    val side = Channel<Int>()
    launch {
        side.consumeEach { println("side channel has $it") }
    }
    produceNumber(side).consumeEach {
        println("main channel has $it")
        delay(500)
    }
    println("done consuming")
    coroutineContext.cancelChildren()
}

fun CoroutineScope.asyncString(time: Int) = async {
    delay(time.toLong())
    "wait for $time ms"
}

fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
    val random = Random(4)
    return List(12) {
        val time = random.nextInt(1000)
        println("$it time: $time")
        asyncString(time)
    }
}

fun main4() = runBlocking {
    val list = asyncStringsList()
    val result = select<String> {
        list.withIndex().forEach { (index, deferred) ->
            deferred.onAwait { answer ->
                "Deferred $index produced answer $answer"
            }
        }
    }
    println(result)
    val countActive = list.count { it.isActive }
    println("$countActive coroutines are still active")
}

fun CoroutineScope.switchMapDeferred(input: ReceiveChannel<Deferred<String>>) = produce<String> {
    var current = input.receive()
    while (isActive) {
        val next = select<Deferred<String>?> {
            input.onReceiveOrNull { update ->
                update
            }
            current.onAwait { value ->
                send(value)
                input.receiveOrNull()
            }
        }
        if (next == null) {
            println("channel was closed")
            break
        } else {
            current = next
        }
    }
}

fun CoroutineScope.asyncString2(str: String, time: Long) = async {
    delay(time)
    str
}

fun main5() = runBlocking {
    val chan = Channel<Deferred<String>>()
    launch {
        for (s in switchMapDeferred(chan)) {
            println(s)
        }
    }
    chan.send(asyncString2("BEGIN", 100))
    delay(200)
    chan.send(asyncString2("Slow", 500))
    delay(100)
    chan.send(asyncString2("Replace", 100))
    delay(500)
    chan.send(asyncString2("END", 500))
    delay(1000)
    chan.close()
    delay(500)
}
