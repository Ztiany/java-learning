package analyse

import kotlinx.coroutines.*

suspend fun main() {
    val scope = CoroutineScope(SupervisorJob())
    val context = Job()
    println("provided scope = $scope")
    println("provided job = $context")

    scope.launch(/*context*/) {
        println("parent scope = $scope")//== provided scope
        println("CancelJob job parent: job = ${coroutineContext[Job]}")

        launch {
            println("CancelJob job1: job = ${coroutineContext[Job]}")
            delay(2000L)
            println("CancelJob job1 finished")
            scope.cancel()
        }

        launch {
            println("CancelJob job2: job = ${coroutineContext[Job]}")
            delay(3000L)
            println("CancelJob job2 finished")
        }
    }.join()
}
