package com.gaurav.learn.kotlin.coroutines.ui.basic

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gaurav.learn.kotlin.coroutines.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class BasicActivity : AppCompatActivity() {

    private val myActivityScope = CoroutineScope(Dispatchers.Main.immediate)

    companion object {
        private const val TAG = "BasicActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
    }


    override fun onDestroy() {
        super.onDestroy()
        myActivityScope.cancel()
    }

    fun testCoroutine(view: View) {
        //testCoroutine()
    }

    fun testCoroutineWithMain(view: View) {
        //testCoroutineWithMain()
    }

    fun testCoroutineWithMainImmediate(view: View) {
        //testCoroutineWithMainImmediate()
    }

    fun testCoroutineEverything(view: View) {
        //testCoroutineEverything()
    }

    fun usingGlobalScope(view: View) {
        //usingGlobalScope()
    }

    fun globalScopeInsideLifecycleScope(view: View) {
        //globalScopeInsideLifecycleScope()
    }

    fun launchInsideLifecycleScope(view: View) {
        //launchInsideLifecycleScope()
    }

    fun twoLaunches(view: View) {
        //twoLaunches()
    }

    fun twoAsyncInsideLifecycleScope(view: View) {
        //twoAsyncInsideLifecycleScope()
    }

    fun twoWithContextInsideLifecycleScope(view: View) {
        //twoWithContextInsideLifecycleScope()
    }

    fun usingMyActivityScope(view: View) {
        //usingMyActivityScope()
    }

    fun twoTasks(view: View) {
        //twoTasks()
    }

    fun parentAndChildTaskCancel(view: View) {
        //parentAndChildTaskCancel()
    }

    fun parentAndChildTaskCancelIsActive(view: View) {
        //parentAndChildTaskCancelIsActive()
    }

    fun lifecycleScopeWithHandlerException(view: View) {
        //lifecycleScopeWithHandlerException()
    }

    fun lifecycleScopeWithHandler(view: View) {
        //lifecycleScopeWithHandler()
    }

    fun myActivityScopeWithHandlerException(view: View) {
        //myActivityScopeWithHandlerException()
    }

    fun myActivityScopeWithHandler(view: View) {
        //myActivityScopeWithHandler()
    }

    fun exceptionInLaunchBlock(view: View) {
        //exceptionInLaunchBlock()
    }

    fun exceptionInAsyncBlock(view: View) {
        //exceptionInAsyncBlock()
    }

    fun exceptionInAsyncBlockWithAwait(view: View) {
        //exceptionInAsyncBlockWithAwait()
    }

    fun testSuspending(view: View) {
        //testSuspending()
    }

    fun testBlocking(view: View) {
        //testBlocking()
    }

}