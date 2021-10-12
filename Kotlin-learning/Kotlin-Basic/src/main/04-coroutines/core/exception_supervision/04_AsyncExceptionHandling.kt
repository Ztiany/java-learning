package core.exception_supervision

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

fun main(): Unit = runBlocking {
    //demo1()
    //demo2()
    demo3()
    //demo4()
}

//https://stackoverflow.com/questions/53577907/when-to-use-coroutinescope-vs-supervisorscope
private suspend fun demo4() {
    CoroutineScope(EmptyCoroutineContext).launch {
        supervisorScope {
            val color = async { delay(2000); "purple" }
            val height = async<Double> { delay(100); throw Exception() }
            try {
                println("A %s box %.1f inches tall".format(color.await(), height.await()))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }.join()

    println("end")
}

private suspend fun demo3() {
    CoroutineScope(EmptyCoroutineContext).launch {
        val deferred = async {
            throw Exception("test")
        }
        try {
            deferred.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.join()

    println("end")
}

private suspend fun demo2() {
    CoroutineScope(EmptyCoroutineContext).launch {
        coroutineScope {
            val deferred = async {
                throw Exception("test")
            }
            try {
                deferred.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }.join()

    println("end")
}

//https://xuyisheng.top/coroutine_exception
private suspend fun demo1() {
    //async的异常，只能在 supervisorScope 中，使用 try catch 进行捕获。
    CoroutineScope(EmptyCoroutineContext).launch {
        supervisorScope {
            val deferred = async {
                throw Exception("test")
            }
            try {
                deferred.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }.join()

    println("end")
}