package com.gaurav.learn.kotlin.coroutines.ui.basic

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gaurav.learn.kotlin.coroutines.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class BasicActivityWithExceptionHandling : AppCompatActivity() {


    private val myActivityScope = CoroutineScope(Dispatchers.Main.immediate)

    companion object {
        private const val TAG = "BasicActivityWithExceptionHandling"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_with_exception_handling)
    }


    override fun onDestroy() {
        super.onDestroy()
        myActivityScope.cancel()
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