package com.androiddesdecero.mvvmkotlin.ui.search

import androidx.lifecycle.ViewModel
import com.androiddesdecero.mvvmkotlin.repository.RepoRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(repoRepository: RepoRepository): ViewModel() {


    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?){
        private var handleError = false

        val errorMessageIfNotHandled: String?
        get() {
            if(handleError){
                return null
            }
            handleError = true
            return errorMessage
        }
    }
}