package com.lizejun.coroutine.demo.chapter11

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.NullPointerException

suspend fun main() {
    main10()
}

suspend fun main1() {
    val seq = sequence<Int> {
        (1..3).forEach {
            yield(it)
        }
    }
    seq.forEach {
        println("$it")
    }
    println("----")
    val flow = flow<Int> {
        (1..3).forEach {
            emit(it)
            delay(300)
        }
    }
    flow.flowOn(Dispatchers.IO).collect {
        println("${Thread.currentThread().name} : $it")
    }
}

suspend fun main2() {
    val flow = flow<Int> {
        (1..3).forEach {
            emit(it)
            delay(300)
        }
        throw NullPointerException()
    }
    flow.catch {
        println(it)
        emit(10)
    }.onCompletion {
        println("onCompletion")
    }.collect {
        println(it)
    }
}

suspend fun main3() {
    val flow = flow<Int> {
        (1..3).forEach {
            emit(it)
            delay(300)
        }
    }
    val each = flow.onEach {
        println("each: $it")
    }
    each.collect {
        println("collect: $it")
    }
}

suspend fun main4() {
    val job = GlobalScope.launch {
        val flow = flow<Int> {
            (1..3).forEach {
                emit(it)
                delay(500)
            }
        }
        flow.collect { println(it) }
    }
    delay(1000)
    job.cancelAndJoin()
}

suspend fun main5() {
    val flow = channelFlow<Int> {
        send(1)
        withContext(Dispatchers.IO) {
            send(2)
        }
    }
    flow.collect {
        println(it)
    }
}

suspend fun main6() {
    flow {
        for (value in 1..300) {
            emit(value)
            println("emit: $value")
            delay(50)
        }
    }.conflate().collect {
        println("collect1: $it")
        delay(500)
        println("collect2: $it")
    }
}

suspend fun main7() {
    flow<Int> {
        for (value in 1..300) {
            emit(value)
            println("emit: $value")
            delay(50)
        }
    }.collectLatest {
        println("collect1: $it")
        delay(500)
        println("collect2: $it")
    }
}

suspend fun main8() {
    flow {
        List (5) {
            emit(it)
        }
    }.map {
        println("--$it--")
        flow {
            List(it) {
                emit(it)
            }
        }
    }.flatMapConcat {
        it
    }.collect {
        println(it)
    }
}

suspend fun main9() {
    val flow1 = flow {
        for (value in 1..3) {
            emit(value)
        }
    }
    val flow2 = flow {
        for (value in 1..4) {
            emit(value)
        }
    }
    flow1.combine(flow2) { v1, v2 ->
        v1 + v2
    }.collect {
        println(it)
    }
}

suspend fun main10() {
    val flow1 = flow {
        for (value in 1..3) {
            emit(value)
        }
    }
    val liveData = flow1.asLiveData(Dispatchers.IO)
    liveData.observeForever {
        println(it)
    }
}