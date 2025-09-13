package com.gaurav.learn.kotlin.coroutines.ui.basic

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gaurav.learn.kotlin.coroutines.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasicActivityWithExceptionHandling : AppCompatActivity() {

    companion object {
        private const val TAG = "BasicActivityWithExceptionHandling"
    }

    private val myActivityScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_with_exception_handling)
    }

    override fun onDestroy() {
        super.onDestroy()
        myActivityScope.cancel()
    }

    /****** Set 1 *********/

    fun twoTasks(view: View) {
        twoTasks()
    }

    fun parentAndChildTaskCancel(view: View) {
        parentAndChildTaskCancel()
    }

    fun parentAndChildTaskCancelIsActive(view: View) {
        parentAndChildTaskCancelIsActive()
    }

    /****** Set 2 *********/

    fun lifecycleScopeWithHandlerException(view: View) {
        lifecycleScopeWithHandlerException()
    }

    fun lifecycleScopeWithHandler(view: View) {
        lifecycleScopeWithHandler()
    }

    fun myActivityScopeWithHandlerException(view: View) {
        myActivityScopeWithHandlerException()
    }

    fun myActivityScopeWithHandler(view: View) {
        myActivityScopeWithHandler()
    }

    /****** Set 3 *********/

    fun exceptionInLaunchBlock(view: View) {
        exceptionInLaunchBlock()
        //exceptionInLaunchBlockHandleProperly()
    }

    fun exceptionInAsyncBlock(view: View) {
        exceptionInAsyncBlock()
    }

    fun exceptionInAsyncBlockWithAwait(view: View) {
        exceptionInAsyncBlockWithAwait()
    }

    /****** ***** *********/


    private fun twoTasks() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        val job = lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task 1 on thread: ${Thread.currentThread().name}")
            doOneLongRunningTask()
            Log.d(TAG, "After Task 1 on thread: ${Thread.currentThread().name}")
        }

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task 2 on thread: ${Thread.currentThread().name}")
            job.cancel()
            doTwoLongRunningTask()
            Log.d(TAG, "After Task 2 on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }


    private fun parentAndChildTaskCancel() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            childTask(coroutineContext[Job]!!)
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        /*
            The above launch block is equivalent to below code:

            val job = lifecycleScope.launch(Dispatchers.Main) {
                Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
                childTask(job)
                Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
            }

         */

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private suspend fun childTask(parent: Job) {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "childTask start on thread: ${Thread.currentThread().name}")
            parent.cancel()
            Log.d(TAG, "childTask parent cancel on thread: ${Thread.currentThread().name}")
            // your code for doing a long running task
            // Added delay to simulate
            delay(2000)
            Log.d(TAG, "childTask end on thread: ${Thread.currentThread().name}")
        }
    }


    private fun parentAndChildTaskCancelIsActive() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            childTaskWithIsActive(coroutineContext[Job]!!)
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        /*
            The above launch block is equivalent to below code:

            val job = lifecycleScope.launch(Dispatchers.Main) {
                Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
                childTaskWithIsActive(job)
                Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
            }

         */

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private suspend fun childTaskWithIsActive(parent: Job) {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "childTask start on thread: ${Thread.currentThread().name}")
            parent.cancel()
            if (isActive) { // isActive to check the specific Coroutine is active or not
                Log.d(TAG, "childTask parent cancel on thread: ${Thread.currentThread().name}")
            }
            // your code for doing a long running task
            // Added delay to simulate
            delay(2000)
            Log.d(TAG, "childTask end on thread: ${Thread.currentThread().name}")
        }
    }

    /**
     * Whatever the exception will come, it will be handled in this handler.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        Log.d(TAG, "exception handler: $e  on thread: ${Thread.currentThread().name}")
    }

    /**
     * functions lifecycleScopeWithHandlerException(), lifecycleScopeWithHandler(),
     * myActivityScopeWithHandlerException(), & myActivityScopeWithHandler() are
     * exactly same except throw Exception("Some Exception") part
     */
    private fun lifecycleScopeWithHandlerException() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to
        // lifecycleScope.launch( exceptionHandler + Dispatchers.Main.immediate ) { ... }
        lifecycleScope.launch(exceptionHandler) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            throw Exception("Some Exception")
            Log.d(TAG, "After Task")  // Unreachable Code
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun lifecycleScopeWithHandler() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")
        lifecycleScope.launch(exceptionHandler) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun myActivityScopeWithHandlerException() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")
        myActivityScope.launch(exceptionHandler) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            throw Exception("Some Exception")
            Log.d(TAG, "After Task")  // Unreachable Code
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun myActivityScopeWithHandler() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")
        myActivityScope.launch(exceptionHandler) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    /**
     * Above 4 functions lifecycleScopeWithHandlerException(), lifecycleScopeWithHandler(),
     * myActivityScopeWithHandlerException(), & myActivityScopeWithHandler() are
     * exactly same except throw Exception("Some Exception") part
     */

    private fun exceptionInLaunchBlock() {

        lifecycleScope.launch {
            doSomethingAndThrowException()
        }

    }

    private fun exceptionInLaunchBlockHandleProperly() {

        lifecycleScope.launch {
            try {
                doSomethingAndThrowException()
            } catch (e: Exception) {
                Log.d(TAG, "Caught Exception: $e   on thread: ${Thread.currentThread().name} ")
            }
        }

    }

    private fun exceptionInAsyncBlock() {
        lifecycleScope.async {
            doSomethingAndThrowException()
        }
    }

    private fun exceptionInAsyncBlockWithAwait() {
        lifecycleScope.launch {
            val deferred = lifecycleScope.async(Dispatchers.Default) {
                doSomethingAndThrowException()
                return@async 10
            }
            try {
                val result = deferred.await()
                Log.d(TAG, "Result is $result   on thread: ${Thread.currentThread().name}  ")
            } catch (e: Exception) {
                Log.d(TAG, "exception handler: $e    on thread: ${Thread.currentThread().name}  ")
            }
        }
    }


    /************ Helper Methods Start ************/

    /**
     *  functions doOneLongRunningTask, doTwoLongRunningTask, & doLongRunningTask are
     *  exactly same except logs to differentiate
     */

    private suspend fun doOneLongRunningTask() {
        Log.d(TAG, "doLongRunningTask-1 Start on thread: ${Thread.currentThread().name}")

        withContext(Dispatchers.Default) {
            // your code for doing a long running task
            // Added delay to simulate
            Log.d(TAG, "Before Delay-1 on thread: ${Thread.currentThread().name}")
            delay(5000)
            Log.d(TAG, "After Delay-1 on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "doLongRunningTask-1 End on thread: ${Thread.currentThread().name}"
        )
    }

    private suspend fun doTwoLongRunningTask() {
        Log.d(TAG, "doLongRunningTask-2 Start on thread: ${Thread.currentThread().name}")

        withContext(Dispatchers.Default) {
            // your code for doing a long running task
            // Added delay to simulate
            Log.d(TAG, "Before Delay-2 on thread: ${Thread.currentThread().name}")
            delay(8000)
            Log.d(TAG, "After Delay-2 on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "doLongRunningTask-2 End on thread: ${Thread.currentThread().name}")
    }

    private suspend fun doLongRunningTask() {
        Log.d(TAG, "doLongRunningTask Start on thread: ${Thread.currentThread().name}")

        withContext(Dispatchers.Default) {
            // your code for doing a long running task
            // Added delay to simulate
            Log.d(TAG, "Before Delay on thread: ${Thread.currentThread().name}")
            delay(5000)
            Log.d(TAG, "After Delay on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "doLongRunningTask End on thread: ${Thread.currentThread().name}")
    }

    // Above 3 functions doOneLongRunningTask, doTwoLongRunningTask, & doLongRunningTask are
    // exactly same except logs to differentiate


    private fun doSomethingAndThrowException() {
        throw Exception("Some Exception")
    }

    /************ Helper Methods End ************/


}