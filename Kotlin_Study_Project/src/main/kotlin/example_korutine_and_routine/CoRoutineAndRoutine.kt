package com.example.example_korutine_and_routine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

//fun main(): Unit = runBlocking {
//    printWithThread("START")
//    launch {
//        newRoutine()
//    }
//    yield()
//    printWithThread("END")
//}
//
//suspend fun newRoutine() {
//    val num1 = 1
//    val num2 = 2
//    yield()
//    printWithThread("${num1 + num2}")
//}
//
//fun printWithThread(str: Any?) {
//    println("[${Thread.currentThread().name}] $str")
//}

suspend fun coroutineFunction(): Int {
    println("시작")
    delay(1000) // 1초 대기 - 논블로킹 (다른 작업 가능)
    println("끝")
    return 42
}

// 사용 예시
fun main() {
    runBlocking {
        val result = coroutineFunction()
        println("결과: $result")
    }
}
