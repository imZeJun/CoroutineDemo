package com.lizejun.coroutine.demo.chapter2

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.*

fun main() {
    val f = sequence {
        yield(1)
        var cur = 1
        var next = 1
        while (true) {
            println("before yield")
            yield(next)
            println("after yield")
            val tmp = cur + next
            cur = next
            next = tmp
        }
    }
    f.take(5).forEach {
        println(it)
    }
    println("-----")
    val f2 = sequence {
        println("yield(1, 2, 3)")
        yieldAll(listOf(1, 2, 3))
        println("yield(3, 4, 5)")
        yieldAll(listOf(3, 4, 5))
        println("yield(6, 7, 8)")
        yieldAll(listOf(6, 7, 8))
        println("end sequence")
    }
    f2.take(5).forEach {
        println(it)
    }
}

suspend fun hello() = suspendCancellableCoroutine<Int> {
    continuation ->
    println(1)
    thread {
        Thread.sleep(1000)
        println(2)
        continuation.resume(1024)
    }
    println(3)
}

suspend fun requestMethod(): User {
    return withContext(Dispatchers.IO) {
        delay(1000)
        suspendCoroutine<User> {
                continuation ->
            println(Thread.currentThread().name)
            continuation.resumeWith(Result.success(User("other")))
        }
    }
}

data class User(val name: String)

class MCoroutineScope {
    val field: Int = 1
}

fun MCoroutineScope.launch(block: MCoroutineScope.() -> Unit) {
    block()
}

public interface MCoroutineContext {

    public operator fun <E: Element> get(key: Key<*>): E?

    public fun <R> fold(initial: R, operation: (R, Element) -> R): R

    public operator fun plus(context: MCoroutineContext): MCoroutineContext

    public fun minusKey(key: Key<*>): MCoroutineContext

    public interface Key<E: Element>

    public interface Element: MCoroutineContext {

        public val key: Key<*>

        public override operator fun <E: Element> get(key: Key<*>): E? =
            if (this.key == key) this as E else null

        public override fun <R> fold(initial: R, operation: (R, Element) -> R): R =
            operation(initial, this)

        public override fun minusKey(key: Key<*>): MCoroutineContext =
            if (this.key == key) EmptyMCoroutineContext else this
    }

}

public object EmptyMCoroutineContext: MCoroutineContext {

    public override fun <R> fold(initial: R, operation: (R, MCoroutineContext.Element) -> R): R = initial

    public override fun <E : MCoroutineContext.Element> get(key: MCoroutineContext.Key<*>): E? = null

    public override fun minusKey(key: MCoroutineContext.Key<*>): MCoroutineContext = this

    public override fun plus(context: MCoroutineContext): MCoroutineContext = context

}

class CustomInterceptor: ContinuationInterceptor {
    override val key: CoroutineContext.Key<*> = ContinuationInterceptor
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        println("call interceptContinuation")
        return continuation
    }
}

interface Callback<T> {
    fun onSuccess(result: T)
    fun onFailed(throwable: Throwable)
}

fun request(success: Boolean, callback: Callback<Int>) {
    Thread.sleep(1000)
    if (success) {
        callback.onSuccess(2)
    } else {
        callback.onFailed(Throwable("message"))
    }
}

suspend fun request(): Int = suspendCancellableCoroutine {
    request(true, object : Callback<Int> {

        override fun onSuccess(result: Int) {
            it.resume(result)
        }

        override fun onFailed(throwable: Throwable) {
            it.resumeWithException(throwable)
        }
    })
}