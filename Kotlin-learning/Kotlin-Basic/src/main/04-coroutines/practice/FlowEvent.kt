package practice

import analyse.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect

suspend fun main() {

    val sharedFlow = MutableSharedFlow<String>(0, 10, BufferOverflow.SUSPEND)

    val job = CoroutineScope(Dispatchers.Default).launch {
        log("collect")
        sharedFlow.collect {
            log(it)
            delay(1000)
        }
    }

    delay(1000)
    sharedFlow.tryEmit("1")
    sharedFlow.tryEmit("2")
    sharedFlow.tryEmit("3")
    sharedFlow.tryEmit("4")
    sharedFlow.tryEmit("5")
    log("sent")

    job.join()
}