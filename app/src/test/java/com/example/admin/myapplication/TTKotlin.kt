package com.example.admin.myapplication

import bolts.Task.delay
import kotlinx.coroutines.*
import org.junit.Test
import kotlin.system.measureTimeMillis


class TTKotlin {

    @Test
    fun t() {

        runBlocking {
//            val startTime = System.currentTimeMillis()
//            val job = launch(Dispatchers.Default) {
//                var nextPrintTime = startTime
//                var i = 0
//                while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
//                    // 每秒打印消息两次
//                    if (System.currentTimeMillis() >= nextPrintTime) {
//                        println("job: I'm sleeping ${i++} ...")
//                        nextPrintTime += 500L
//                    }
//                }
//            }
//            delay(1300L) // 等待一段时间
//            println("main: I'm tired of waiting!")
//            job.cancelAndJoin() // 取消一个作业并且等待它结束
//            println("main: Now I can quit.")

        }
    }

    @Test
    fun t0() = runBlocking<Unit> {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 15
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 25
    }
}