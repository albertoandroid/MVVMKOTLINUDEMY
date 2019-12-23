package com.androiddesdecero.mvvmkotlin

import android.os.Looper
import android.os.Handler
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppExecutors(
    val diskIO: Executor,
    val networkIO: Executor,
    val mainThread: Executor
    ) {
    @Inject
    constructor(): this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecutor()
    )

    fun diskIO(): Executor{
        return diskIO
    }

    fun networkIO(): Executor{
        return networkIO
    }

    fun mainThread(): Executor{
        return mainThread
    }

    private class  MainThreadExecutor: Executor{
        val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable){
            mainThreadHandler.post(command)
        }
    }

}