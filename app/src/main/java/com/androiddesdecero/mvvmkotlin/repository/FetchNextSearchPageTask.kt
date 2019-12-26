package com.androiddesdecero.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddesdecero.mvvmkotlin.api.*
import com.androiddesdecero.mvvmkotlin.db.GithubDb
import com.androiddesdecero.mvvmkotlin.model.RepoSearchResult
import java.io.IOException

class FetchNextSearchPageTask constructor(
    private val query: String,
    private val githubApi: GithubApi,
    private val db: GithubDb
): Runnable  {
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        val current = db.repoDao().findSearchResult(query)
        if(current == null){
            _liveData.postValue(null)
            return
        }
        val nextPage = current.next
        if(nextPage == null){
            _liveData.postValue(Resource.success(false))
            return
        }
        val newValue = try {
            val response = githubApi.searchRepos(query, nextPage).execute()
            val apiResponse = ApiResponse.create(response)
            when (apiResponse){
                is ApiSuccessResponse->{
                    val ids = arrayListOf<Int>()
                    ids.addAll(current.repoIds)
                    ids.addAll(apiResponse.body.items.map { it.id })
                    val merged = RepoSearchResult(query, ids, apiResponse.body.total, apiResponse.nextPage)

                    try{
                        db.beginTransaction()
                        db.repoDao().insert(merged)
                        db.repoDao().insertRepos(apiResponse.body.items)
                        db.setTransactionSuccessful()
                    }finally {
                        db.endTransaction()
                    }
                    Resource.success(apiResponse.nextPage != null)
                }
                is ApiEmptyResponse->{
                    Resource.success(false)
                }
                is ApiErrorResponse->{
                    Resource.error(apiResponse.errorMessage, true)
                }
            }
        }catch (e: IOException){
            Resource.error(e.message!!, true)
        }
        _liveData.postValue(newValue)
    }
}