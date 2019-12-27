package com.androiddesdecero.mvvmkotlin.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.androiddesdecero.mvvmkotlin.GithubApp
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

object AppInjector {
    fun init(githubApp: GithubApp){

        githubApp
            .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
                override fun onActivityPaused(activity: Activity?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onActivityResumed(activity: Activity?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onActivityStarted(activity: Activity?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onActivityDestroyed(activity: Activity?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onActivityStopped(activity: Activity?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    handleActivity(activity)
                }

            })
    }

    private fun handleActivity(activity: Activity){
        if(activity is HasSupportFragmentInjector){
            AndroidInjection.inject(activity)
        }
        if(activity is FragmentActivity){
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks(){
                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            if(f is Injectable){
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true
                )
        }
    }
}