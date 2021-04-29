package com.lizejun.coroutine.demo.chapter1

import kotlinx.coroutines.*

fun main() = main5()

fun main1() {
    GlobalScope.launch(Dispatchers.IO) {
        println(Thread.currentThread().name)
        delay(1000L)
    }
    println("main1" + Thread.currentThread().name)
    Thread.sleep(2000L)
}

fun main2() {
    GlobalScope.launch {
        delay(1000L)
        println(Thread.currentThread().name)
    }
    println("main2:" + Thread.currentThread().name)
    runBlocking {
        println(Thread.currentThread().name)
        delay(2000L)
    }
}

fun main3(): Unit = runBlocking<Unit> {
    GlobalScope.launch {
        delay(1000L)
        println("GlobalScope.launch:" + Thread.currentThread().name)
    }
    println("main3:" + Thread.currentThread().name)
    delay(2000L)
}

fun main4() = runBlocking {
    val job = GlobalScope.launch {
        delay(1000L)
        launch {

        }
    }
    job.join()
}

fun main9() = runBlocking {
    launch {
        delay(1000L)
        println("launch()")
    }
    println("main9()")
}

fun main5() = runBlocking {
    launch {
        delay(200L)
        println("Task From runBlocking")
    }
    coroutineScope {
        launch {
            delay(50L)
            println("Task From nested launch")
        }
        delay(100L)
        println("Task From coroutineScope")
    }
    println("CoroutineScope is over")
}

fun main7() = runBlocking {
    repeat(100) {
        launch {
            delay(1000L)
            println("main7()" + Thread.currentThread().name)
        }
    }
}

fun main8() = runBlocking {
    GlobalScope.launch {
        repeat(100) {
            println("I am sleeping $it")
            delay(500L)
        }
    }
    delay(1300L)
}

