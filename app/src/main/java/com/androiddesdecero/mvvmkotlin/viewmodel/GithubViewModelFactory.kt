package com.androiddesdecero.mvvmkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class GithubViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull() {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknow model class $modelClass")

        try {
            return creator.get() as T
        } catch (e: Exception){
            throw RuntimeException(e)
        }
    }
}