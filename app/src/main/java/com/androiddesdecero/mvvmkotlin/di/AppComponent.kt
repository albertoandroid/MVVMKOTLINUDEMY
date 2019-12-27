package com.androiddesdecero.mvvmkotlin.di

import android.app.Application
import com.androiddesdecero.mvvmkotlin.GithubApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    MainActivityModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(githubApp: GithubApp)
}