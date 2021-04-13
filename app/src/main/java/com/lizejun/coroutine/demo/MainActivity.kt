package com.lizejun.coroutine.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv_launch).setOnClickListener {
            GlobalScope.launch {
                val token = requestToken()
                val result = createPost(token)
                processPost(result)
            }
        }
        val builder = java.lang.StringBuilder();
        builder.length
        findViewById<View>(R.id.tv_async).setOnClickListener {
            runBlocking {
                //下面两个 async 协程是并行运行的。
                val result = GlobalScope.async {
                    Log.d("async", "before")
                    delay(500)
                    Log.d("async", "after")
                    return@async "async result"
                }
                val result2 = GlobalScope.async {
                    Log.d("async", "before2")
                    delay(800)
                    Log.d("async", "after2")
                    return@async "async result2"
                }
                Log.d("async", "result=${result.await()}, ${result2.await()}")
            }
        }
        findViewById<View>(R.id.tv_double_launch).setOnClickListener {
            runBlocking {
                //下面两个 launch 协程是并行执行的。
                val dispatcher = newSingleThreadContext("double launch")
                GlobalScope.launch(dispatcher) {
                    Log.d("launch", "before")
                    delay(500)
                    Log.d("launch", "after")
                }
                GlobalScope.launch(dispatcher) {
                    Log.d("launch", "before2")
                    delay(500)
                    Log.d("launch", "after2")
                }
            }
        }
        findViewById<View>(R.id.tv_main).setOnClickListener {
            runBlocking {
                GlobalScope.launch(Dispatchers.Main) {
                    logThread("before token")
                    val token = async {
                        return@async requestToken()
                    }.await()
                    logThread("after token")
                    val result = async {
                        return@async createPost(token)
                    }.await()
                    logThread("after result")
                    processPost(result)
                }
                logThread("after call launch")
            }
        }
        findViewById<View>(R.id.tv_default).setOnClickListener {
            runBlocking {
                GlobalScope.launch(Dispatchers.Default) {
                    logThread("before token")
                    val token = async {
                        return@async requestToken()
                    }.await()
                    logThread("after token")
                    val result = async {
                        return@async createPost(token)
                    }.await()
                    logThread("after result")
                    processPost(result)
                }
                logThread("after call launch")
            }
        }
        findViewById<View>(R.id.tv_io).setOnClickListener {
            runBlocking {
                GlobalScope.launch(Dispatchers.IO) {
                    logThread("before token")
                    val token = async {
                        return@async requestToken()
                    }.await()
                    logThread("after token")
                    val result = async {
                        return@async createPost(token)
                    }.await()
                    logThread("after result")
                    processPost(result)
                }
                logThread("after call launch")
            }
        }
        findViewById<View>(R.id.tv_unconfined).setOnClickListener {
            runBlocking {
                GlobalScope.launch(Dispatchers.Unconfined) {
                    logThread("before token")
                    val token = async {
                        return@async requestToken()
                    }.await()
                    logThread("after token")
                    val result = async {
                        return@async createPost(token)
                    }.await()
                    logThread("after result")
                    processPost(result)
                }
                logThread("after call launch")
            }
        }
    }

    private suspend fun requestToken(): String {
        logThread("before requestToken")
        delay(500)
        logThread("after requestToken")
        return "token"
    }

    private suspend fun createPost(token: String): String {
        logThread("before createPost")
        delay(500)
        logThread("after createPost")
        return "receive result by $token"
    }

    private suspend fun processPost(result: String) {
        logThread("processPost, result=$result")
    }

    private fun logThread(tag: String) {
        Log.d("Thread", "$tag, 线程=${Thread.currentThread().name}")
    }
}
