package com.androiddesdecero.mvvmkotlin.di

import com.androiddesdecero.mvvmkotlin.ui.repo.RepoFragment
import com.androiddesdecero.mvvmkotlin.ui.search.SearchFragment
import com.androiddesdecero.mvvmkotlin.ui.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeRepoFragment(): RepoFragment

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}