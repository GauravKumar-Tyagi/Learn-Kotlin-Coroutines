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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class BasicActivityWithMoreExamples : AppCompatActivity() {

    companion object {
        private const val TAG = "BasicActivityWithMoreExamples"
    }

    private val myActivityScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val myCustomScope_1 = CoroutineScope(Dispatchers.Default + Job())

    private val myCustomScope_2 = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_with_more_examples)
    }

    override fun onDestroy() {
        super.onDestroy()
        myCustomScope_1.cancel()
        myCustomScope_2.cancel()
        myActivityScope.cancel()
    }

    /****** Set 1 *********/

    fun testSuspending(view: View) {
        testSuspending()
    }

    fun testBlocking(view: View) {
        testBlocking()
    }

    /****** Set 2 *********/

    fun myActivityScopeHandlerException(view: View) {
        myActivityScopeWithHandlerException()
    }

    fun testCoroutineExceptionHandler(view: View) {
        //testCoroutineExceptionHandlerOne()
        //testCoroutineExceptionHandlerTwo()
        testCoroutineExceptionHandlerThree()
        //testCoroutineExceptionHandlerFour()
        //testCoroutineExceptionHandlerFive()
    }

    fun testRunBlocking(view: View) {
        testRunBlocking()
    }

    fun testJob(view: View) {
        //testJob()
        testJobWithTryCatch()
    }

    fun testSupervisorJob(view: View) {
        //testSupervisorJob()
        testSupervisorJobWithTryCatch()
    }

    fun testCoroutineScope(view: View) {
        testCoroutineScope()
    }

    fun testSupervisorScope(view: View) {
        testSupervisorScope()
    }


    /****** ***** *********/


    /****** @@@@@ *********/

    /**
     * Use SupervisorJob when you don't want an exception from a child to cancel the parent or the other children,
     * because if a parent fails, then all its children are cancelled.
     *
     * Supervisor Scope is only used as a parent to run children coroutines,
     * and so you can use it as a builder to wrap children coroutines to avoid cancellation of
     * children coroutines if one child is cancelled due to an exception thrown.
     */
    private fun testSupervisorScope() {


        val parentJob = myCustomScope_1.launch(exceptionHandler) {

            Log.d(TAG, "parentJob started...")

            supervisorScope {

                // Launch Task 1
                launch {
                    Log.d(TAG, "Task 1 started")
                    // Simulate some work
                    for (i in 1..5) {
                        Log.d(TAG, "Task 1 is working... $i")
                        delay(500)
                    }
                    Log.d(TAG, "Task 1 completed")
                }

                // Launch Task 2 (this one will throw an exception)
                launch {
                    //try {
                        Log.d(TAG, "Task 2 started")
                        // Simulate some work that will fail
                        delay(1000)
                        throw Exception("Simulated error in Task 2")
                    //} catch (e: Exception) {
                        //Log.d(TAG, "Task 2 failed with exception: ${e.message}")
                    //}
                }

                // Launch Task 3
                launch {
                    Log.d(TAG, "Task 3 started")
                    // Simulate some work
                    for (i in 1..5) {
                        Log.d(TAG, "Task 3 is working... $i")
                        delay(700)
                    }
                    Log.d(TAG, "Task 3 completed")
                }


            }

            delay(300)
            Log.d(TAG, "ParentJob Task Running")
            delay(3000)
            Log.d(TAG, "ParentJob Task Completed")

        }


    }

    private fun testCoroutineScope() {


        val parentJob = lifecycleScope.launch(exceptionHandler) {

            Log.d(TAG, "parentJob started...")

            coroutineScope {


                // Launch Task 1
                launch {
                    Log.d(TAG, "Task 1 started")
                    // Simulate some work
                    for (i in 1..5) {
                        Log.d(TAG, "Task 1 is working... $i")
                        delay(500)
                    }
                    Log.d(TAG, "Task 1 completed")
                }

                // Launch Task 2 (this one will throw an exception)
                launch {
                    //try {
                    Log.d(TAG, "Task 2 started")
                    // Simulate some work that will fail
                    delay(1000)
                    throw Exception("Simulated error in Task 2")
                    //} catch (e: Exception) {
                    //Log.d(TAG, "Task 2 failed with exception: ${e.message}")
                    //}
                }

                // Launch Task 3
                launch {
                    Log.d(TAG, "Task 3 started")
                    // Simulate some work
                    for (i in 1..5) {
                        Log.d(TAG, "Task 3 is working... $i")
                        delay(700)
                    }
                    Log.d(TAG, "Task 3 completed")
                }


            }

            delay(300)
            Log.d(TAG, "ParentJob Task Running")
            delay(3000)
            Log.d(TAG, "ParentJob Task Completed")

        }


    }


    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        Log.d(TAG, "exception handler: $e  on thread: ${Thread.currentThread().name}")
    }

    private val innerExceptionHandler = CoroutineExceptionHandler { _, e ->
        Log.d(TAG, "Internal exception handler: $e  on thread: ${Thread.currentThread().name}")
    }


    /**
     * Use SupervisorJob when you don't want an exception from a child to cancel the parent or the other children,
     * because if a parent fails, then all its children are cancelled.
     *
     * Supervisor Scope is only used as a parent to run children coroutines,
     * and so you can use it as a builder to wrap children coroutines to avoid cancellation of
     * children coroutines if one child is cancelled due to an exception thrown.
     */
    private fun testSupervisorJob() {

        val parentJob = myCustomScope_1.launch(exceptionHandler) {

            Log.d(TAG, "parentJob started...")

            // Launch Task 1
            val child1 = myCustomScope_2.launch {
                Log.d(TAG, "Task 1 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 1 is working... $i")
                    delay(500)
                }
                Log.d(TAG, "Task 1 completed")
            }

            // Launch Task 2 (this one will throw an exception)
            val child2 = myCustomScope_2.launch(innerExceptionHandler) {
                //try {
                    Log.d(TAG, "Task 2 started")
                    // Simulate some work that will fail
                    delay(1000)
                    throw Exception("Simulated error in Task 2")
                //} catch (e: Exception) {
                    //Log.d(TAG, "Task 2 failed with exception: ${e.message}")
                //}
            }

            // Launch Task 3
            val child3 = launch {
                Log.d(TAG, "Task 3 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 3 is working... $i")
                    delay(700)
                }
                Log.d(TAG, "Task 3 completed")
            }

            delay(300)
            Log.d(TAG, "ParentJob Task Running")
            delay(3000)
            Log.d(TAG, "ParentJob Task Completed")

        }

    }

    private fun testSupervisorJobWithTryCatch() {

        val parentJob = myCustomScope_2.launch {

            Log.d(TAG, "parentJob started...")

            // Launch Task 1
            val child1 = launch {
                Log.d(TAG, "Task 1 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 1 is working... $i")
                    delay(500)
                }
                Log.d(TAG, "Task 1 completed")
            }

            // Launch Task 2 (this one will throw an exception)
            val child2 = launch {
                try {
                    Log.d(TAG, "Task 2 started")
                    // Simulate some work that will fail
                    delay(1000)
                    throw Exception("Simulated error in Task 2")
                } catch (e: Exception) {
                    Log.d(TAG, "Task 2 failed with exception: ${e.message}")
                }
            }

            // Launch Task 3
            val child3 = launch {
                Log.d(TAG, "Task 3 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 3 is working... $i")
                    delay(700)
                }
                Log.d(TAG, "Task 3 completed")
            }

            delay(300)
            Log.d(TAG, "ParentJob Task Running")
            delay(3000)
            Log.d(TAG, "ParentJob Task Completed")

        }

    }

    /**
     * In case of using Job:
     * If The previous job's exception may have cancelled the scope (myCustomScope_1), preventing new coroutines
     * from launching. In your code, when an exception is thrown in one of the child coroutines (Task 2),
     * it cancels the scope (myCustomScope_1) because it uses a regular Job.
     * Once cancelled, further launches in this scope will not work until you recreate the scope.
     * To fix this, you can either recreate myCustomScope_1 before launching, or use a SupervisorJob instead,
     * which allows other coroutines to continue even if one fails.
     */
    private fun testJob() {

        val parentJob = myCustomScope_2.launch(exceptionHandler) {

            Log.d(TAG, "parentJob started...")

            // Launch Task 1
            val child1 = myCustomScope_1.launch {
                Log.d(TAG, "Task 1 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 1 is working... $i")
                    delay(500)
                }
                Log.d(TAG, "Task 1 completed")
            }

            // Launch Task 2 (this one will throw an exception)
            val child2 = myCustomScope_1.launch(innerExceptionHandler) {
                //try {
                    Log.d(TAG, "Task 2 started")
                    // Simulate some work that will fail
                    delay(1000)
                    throw Exception("Simulated error in Task 2")
                //} catch (e: Exception) {
                    //Log.d(TAG, "Task 2 failed with exception: ${e.message}")
                //}
            }

            // Launch Task 3
            val child3 = launch {
                Log.d(TAG, "Task 3 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 3 is working... $i")
                    delay(700)
                }
                Log.d(TAG, "Task 3 completed")
            }

            delay(300)
            Log.d(TAG, "ParentJob Task Running")
            delay(3000)
            Log.d(TAG, "ParentJob Task Completed")

        }

    }

    private fun testJobWithTryCatch() {

        val parentJob = myCustomScope_1.launch {

            Log.d(TAG, "parentJob started...")

            // Launch Task 1
            val child1 = launch {
                Log.d(TAG, "Task 1 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 1 is working... $i")
                    delay(500)
                }
                Log.d(TAG, "Task 1 completed")
            }

            // Launch Task 2 (this one will throw an exception)
            val child2 = launch {
                try {
                    Log.d(TAG, "Task 2 started")
                    // Simulate some work that will fail
                    delay(1000)
                    throw Exception("Simulated error in Task 2")
                } catch (e: Exception) {
                    Log.d(TAG, "Task 2 failed with exception: ${e.message}")
                }
            }

            // Launch Task 3
            val child3 = launch {
                Log.d(TAG, "Task 3 started")
                // Simulate some work
                for (i in 1..5) {
                    Log.d(TAG, "Task 3 is working... $i")
                    delay(700)
                }
                Log.d(TAG, "Task 3 completed")
            }

            delay(300)
            Log.d(TAG, "ParentJob Task Running")
            delay(3000)
            Log.d(TAG, "ParentJob Task Completed")

        }

    }

    /**
     * The CoroutineExceptionHandler only handles exceptions for the coroutine
     * it is directly attached to if that coroutine is a root coroutine
     * (i.e., it is launched directly in a scope, not as a child of another coroutine).
     * If you launch child coroutines inside a parent coroutine, exceptions from children are handled
     * by the parent’s handler, not the child’s. Exception handlers on child coroutines are ignored
     * unless the child is a root coroutine itself.
     * So In this case, there is no impact or use of adding
     * CoroutineExceptionHandler (innerExceptionHandler) to child coroutines.
     * So if you use like below then only child exception handler will work because now child is root coroutine.:
     *   lifecycleScope.launch(exceptionHandler) {
     *          myActivityScope.launch(innerExceptionHandler) {
     *              //...
     *          }
     *          OR
     *          lifecycleScope.launch(innerExceptionHandler) {
     *              //...
     *          }
     *    }
     */
    private fun testCoroutineExceptionHandlerOne() {
        lifecycleScope.launch(exceptionHandler) {
            Log.d(TAG, "1. This will run on thread: ${Thread.currentThread().name}")
            launch(innerExceptionHandler) {
                Log.d(TAG, "2. This will run on thread: ${Thread.currentThread().name}")
                delay(100)
                throw Exception("3. Exception from Job")
            }
            delay(500)
            Log.d(TAG, "4. This will run on thread: ${Thread.currentThread().name}")
        }
    }

    /**
     * App will crash.
     * The crash occurs because the parent coroutine does not have a CoroutineExceptionHandler attached.
     * When the child coroutine throws an exception, it is not caught, so it propagates up and crashes the app.
     * Attach the exceptionHandler to the parent coroutine [SAME as testCoroutineExceptionHandlerOne()]
     * to prevent the crash.
     * you can use like: lifecycleScope.launch(innerExceptionHandler) { ... } also for
     * inner coroutine to prevent crash.
     */
    private fun testCoroutineExceptionHandlerTwo() {
        lifecycleScope.launch {
            Log.d(TAG, "1. This will run on thread: ${Thread.currentThread().name}")
            launch(innerExceptionHandler) {
                Log.d(TAG, "2. This will run on thread: ${Thread.currentThread().name}")
                delay(100)
                throw Exception("3. Exception from Job")
            }
            delay(500)
            Log.d(TAG, "4. This will run on thread: ${Thread.currentThread().name}")
        }
    }


    /**
     * In this case, the exception thrown in the child coroutine is caught by the try-catch block,
     * So there is no unhandled exception to propagate to the parent coroutine.
     * So there is no use of adding CoroutineExceptionHandler (exceptionHandler) to parent coroutine.
     * So we can use like: lifecycleScope.launch { ... } also without adding
     * exception handler (exceptionHandler) to parent.
     */
    private fun testCoroutineExceptionHandlerThree() {
        //lifecycleScope.launch(exceptionHandler)
        lifecycleScope.launch {
            Log.d(TAG, "1. This will run on thread: ${Thread.currentThread().name}")
            launch {
                try {
                    Log.d(TAG, "2. This will run on thread: ${Thread.currentThread().name}")
                    delay(100)
                    throw Exception("3. Exception from Job")
                } catch (e: Exception) {
                    Log.d(TAG, "5. Caught exception: $e")
                }

            }
            delay(500)
            Log.d(TAG, "4. This will run on thread: ${Thread.currentThread().name}")
        }
    }

    /**
     * App will crash.
     * The child coroutine is a root coroutine, and its exception is not handled by the
     * parent’s CoroutineExceptionHandler.
     * Hence Unhandled exceptions in root coroutines crash the app.
     */
    private fun testCoroutineExceptionHandlerFour() {
        lifecycleScope.launch(exceptionHandler) {
            Log.d(TAG, "1. This will run on thread: ${Thread.currentThread().name}")
            lifecycleScope.launch {
                Log.d(TAG, "2. This will run on thread: ${Thread.currentThread().name}")
                delay(100)
                throw Exception("3. Exception from Job")
            }
            delay(500)
            Log.d(TAG, "4. This will run on thread: ${Thread.currentThread().name}")
        }
    }

    /**
     * In this case App will NOT crash.
     * The child coroutine is NOT a root coroutine now, and its exception can be handled by the
     * parent’s CoroutineExceptionHandler.
     * Hence App will NOT crash.
     * OUTPUT:
     * 1. This will run on thread: main
     * 2. This will run on thread: main
     * exception handler: java.lang.Exception: 3. Exception from Job  on thread: main
     */
    private fun testCoroutineExceptionHandlerFive() {
        lifecycleScope.launch(exceptionHandler) {
            Log.d(TAG, "1. This will run on thread: ${Thread.currentThread().name}")
            launch {
                Log.d(TAG, "2. This will run on thread: ${Thread.currentThread().name}")
                delay(100)
                throw Exception("3. Exception from Job")
            }
            delay(500)
            Log.d(TAG, "4. This will run on thread: ${Thread.currentThread().name}")
        }
    }


    private fun testRunBlocking() {

        Log.d(TAG, "Before RunBlocking - on thread: ${Thread.currentThread().name}")

        val rootJob = runBlocking {

            val parentJob = launch {

                Log.d(TAG, "Coroutine is active - on thread: ${Thread.currentThread().name}")
                val childJob1 = launch {
                    delay(500)
                    Log.d(TAG, "Child job 1 - on thread: ${Thread.currentThread().name}")
                }
                val childJob2 = launch {
                    delay(1000)
                    Log.d(TAG, "Child job 2 - on thread: ${Thread.currentThread().name}")
                }
                launch() {
                    Log.d(TAG, "rooting : parent job - on thread: ${Thread.currentThread().name}")
                }
            }

            launch() {
                Log.d(
                    TAG,
                    "run blocking main coroutine job BEFORE DELAY - on thread: ${Thread.currentThread().name}"
                )
                delay(2000)
                Log.d(
                    TAG,
                    "run blocking main coroutine job AFTER DELAY - on thread: ${Thread.currentThread().name}"
                )
            }

        }

        Log.d(TAG, "After RunBlocking - on thread: ${Thread.currentThread().name}")
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

    /****** testSuspending & testBlocking *********/

    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private fun testSuspending() {
        lifecycleScope.launch(dispatcher) {
            Log.d(TAG, "testSuspending Before Task 1 on thread: ${Thread.currentThread().name}")
            timeTakingTask()
            Log.d(TAG, "testSuspending After Task 1 on thread: ${Thread.currentThread().name}")
        }
        lifecycleScope.launch(dispatcher) {
            Log.d(TAG, "testSuspending Before Task 2 on thread: ${Thread.currentThread().name}")
            timeTakingTask()
            Log.d(TAG, "testSuspending After Task 2 on thread: ${Thread.currentThread().name}")
        }
    }

    private fun testBlocking() {
        lifecycleScope.launch(dispatcher) {
            runBlocking {
                Log.d(TAG, "testBlocking Before Task 1 on thread: ${Thread.currentThread().name}")
                timeTakingTask()
                Log.d(TAG, "testBlocking After Task 1 on thread: ${Thread.currentThread().name}")
            }
        }
        lifecycleScope.launch(dispatcher) {
            runBlocking {
                Log.d(TAG, "testBlocking Before Task 2 on thread: ${Thread.currentThread().name}")
                timeTakingTask()
                Log.d(TAG, "testBlocking After Task 2 on thread: ${Thread.currentThread().name}")
            }
        }
    }

    /************ Helper Methods Start ************/

    private suspend fun timeTakingTask() {
        withContext(Dispatchers.IO) {
            // your code for doing a time taking task
            // Added delay to simulate
            Thread.sleep(5000)
        }
    }

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