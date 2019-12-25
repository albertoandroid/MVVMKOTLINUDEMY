package com.androiddesdecero.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import com.androiddesdecero.mvvmkotlin.AppExecutors
import com.androiddesdecero.mvvmkotlin.api.ApiResponse
import com.androiddesdecero.mvvmkotlin.api.GithubApi
import com.androiddesdecero.mvvmkotlin.db.GithubDb
import com.androiddesdecero.mvvmkotlin.db.RepoDao
import com.androiddesdecero.mvvmkotlin.model.Repo
import com.androiddesdecero.mvvmkotlin.utils.RateLimiter
import java.security.acl.Owner
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class 31º @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: GithubDb,
    private val repoDao: RepoDao,
    private val githubApi: GithubApi
) {
    private val repoListRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos(owner: String): LiveData<Resource<List<Repo>>> {
        return object: NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors){
            override fun saveCallResult(item: List<Repo>) {
                repoDao.insertRepos(item)
            }

            override fun shouldFetch(data: List<Repo>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimiter.shoulFetch(owner)
            }

            override fun loadFromDb(): LiveData<List<Repo>> = repoDao.loadRepositories(owner)


            override fun createCall(): LiveData<ApiResponse<List<Repo>>> = githubApi.getRepos(owner)


            override fun onFetchFailed(){
                repoListRateLimiter.reset(owner)
            }

        }.asLiveData()
    }
}