package com.gaurav.learn.kotlin.coroutines.ui.basic

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gaurav.learn.kotlin.coroutines.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasicActivity : AppCompatActivity() {

    private val myActivityScope = CoroutineScope(Dispatchers.Main.immediate)

    companion object {
        private const val TAG = "BasicActivity"
    }
    override fun onDestroy() {
        super.onDestroy()
        myActivityScope.cancel()
    }

    /****** Set 1 *********/

    fun testCoroutine(view: View) {
        testCoroutine()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
    }


    fun usingMyActivityScope(view: View) {
        usingMyActivityScope()
    }

    fun testCoroutineWithMain(view: View) {
        testCoroutineWithMain()
    }

    /****** Set 2 *********/

    fun usingGlobalScope(view: View) {
        usingGlobalScope()
    }

    fun globalScopeInsideLifecycleScope(view: View) {
        globalScopeInsideLifecycleScope()
    }

    fun launchInsideLifecycleScope(view: View) {
        launchInsideLifecycleScope()
    }

    /****** Set 3 *********/

    fun testCoroutineEverything(view: View) {
        testCoroutineEverything()
    }

    fun twoLaunches(view: View) {
        twoLaunches()
    }

    fun twoWithContextInsideLifecycleScope(view: View) {
        twoWithContextInsideLifecycleScope()
    }

    fun twoAsyncInsideLifecycleScope(view: View) {
        twoAsyncInsideLifecycleScope()
    }

    fun twoLaunchInsideLifecycleScope(view: View) {
        twoLaunchInsideLifecycleScope()
    }

    /****** ***** *********/

    private fun testCoroutine() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to lifecycleScope.launch(Dispatchers.Main.immediate) { ... }
        lifecycleScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun usingMyActivityScope() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // we have created myActivityScope with Dispatchers.Main.immediate dispatcher
        myActivityScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun testCoroutineWithMain() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun usingGlobalScope() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Default for GlobalScope
        // and it [GlobalScope.launch] will be equivalent to GlobalScope.launch(Dispatchers.Default) { ... }
        GlobalScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            doLongRunningTask()
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun globalScopeInsideLifecycleScope() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to lifecycleScope.launch(Dispatchers.Main.immediate) { ... }
        lifecycleScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            GlobalScope.launch(Dispatchers.Default) {
                Log.d(TAG, "Before Delay on thread: ${Thread.currentThread().name}")
                delay(5000)
                Log.d(TAG, "After Delay on thread: ${Thread.currentThread().name}")
            }
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun launchInsideLifecycleScope() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to lifecycleScope.launch(Dispatchers.Main.immediate) { ... }
        lifecycleScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")
            // NOTE: Here this inner launch will pick the scope from the Parent and Parent is lifecycleScope here.
            // Hence this inner launch will be same as lifecycleScope.launch{ }
            launch(Dispatchers.Default) {
                Log.d(TAG, "Before Delay on thread: ${Thread.currentThread().name}")
                delay(5000)
                Log.d(TAG, "After Delay on thread: ${Thread.currentThread().name}")
            }
            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }
        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }


    private fun testCoroutineEverything() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task 1 on thread: ${Thread.currentThread().name}")
            doOneLongRunningTask()
            Log.d(TAG, "After Task 1 on thread: ${Thread.currentThread().name}")
        }

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d(TAG, "Before Task 2 on thread: ${Thread.currentThread().name}")
            doTwoLongRunningTask()
            Log.d(TAG, "After Task 2 on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }

    private fun twoLaunches() {
        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        lifecycleScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Before Delay 1 on thread: ${Thread.currentThread().name}")
            delay(2000)
            Log.d(TAG, "After Delay 1 on thread: ${Thread.currentThread().name}")
        }

        lifecycleScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Before Delay 2 on thread: ${Thread.currentThread().name}")
            delay(2000)
            Log.d(TAG, "After Delay 2 on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
    }


    private fun twoWithContextInsideLifecycleScope() {

        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to lifecycleScope.launch(Dispatchers.Main.immediate) { ... }
        lifecycleScope.launch {
            Log.d(TAG, "Before Task 1 on thread: ${Thread.currentThread().name}")
            val resultOne = doLongRunningTaskOne()
            Log.d(TAG, "After Task 1 on thread: ${Thread.currentThread().name}")
            Log.d(TAG, "Before Task 2 on thread: ${Thread.currentThread().name}")
            val resultTwo = doLongRunningTaskTwo()
            Log.d(TAG, "After Task 2 on thread: ${Thread.currentThread().name}")
            val result = resultOne + resultTwo
            Log.d(TAG, "result : $result")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")

    }


    private fun twoAsyncInsideLifecycleScope() {

        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to lifecycleScope.launch(Dispatchers.Main.immediate) { ... }
        lifecycleScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")

            val deferredOne = async {
                doLongRunningTaskOne()
            }

            val deferredTwo = async {
                doLongRunningTaskTwo()
            }

            Log.d(TAG, "Before Result on thread: ${Thread.currentThread().name}")

            val result = deferredOne.await() + deferredTwo.await()

            Log.d(TAG, "result : $result")

            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")

    }


    private fun twoLaunchInsideLifecycleScope() {

        Log.d(TAG, "Function Start on thread: ${Thread.currentThread().name}")

        // NOTE: The default dispatcher is Dispatchers.Main.immediate for lifecycleScope & viewModelScope
        // and it [lifecycleScope.launch] will be equivalent to lifecycleScope.launch(Dispatchers.Main.immediate) { ... }
        lifecycleScope.launch {
            Log.d(TAG, "Before Task on thread: ${Thread.currentThread().name}")

            launch {
                doLongRunningTaskOne()
            }

            launch {
                doLongRunningTaskTwo()
            }

            Log.d(TAG, "After Task on thread: ${Thread.currentThread().name}")
        }

        Log.d(TAG, "Function End on thread: ${Thread.currentThread().name}")
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

        Log.d(TAG, "doLongRunningTask-1 End on thread: ${Thread.currentThread().name}")
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


    private suspend fun doLongRunningTaskOne(): Int {
        return withContext(Dispatchers.Default) {
            // your code for doing a long running task
            // Added delay to simulate
            delay(2000)
            return@withContext 10
        }
    }

    private suspend fun doLongRunningTaskTwo(): Int {
        return withContext(Dispatchers.Default) {
            // your code for doing a long running task
            // Added delay to simulate
            delay(3000)
            return@withContext 10
        }
    }

    /************ Helper Methods End ************/


}