package com.androiddesdecero.mvvmkotlin.repository

import androidx.lifecycle.LiveData
import com.androiddesdecero.mvvmkotlin.AppExecutors
import com.androiddesdecero.mvvmkotlin.api.ApiResponse
import com.androiddesdecero.mvvmkotlin.api.GithubApi
import com.androiddesdecero.mvvmkotlin.db.UserDao
import com.androiddesdecero.mvvmkotlin.model.User
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.transform.Templates

@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val githubApi: GithubApi
) {
    fun loadUser(login: String): LiveData<Resource<User>>{
        return object: NetworkBoundResource<User, User>(appExecutors){
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<User> {
                return userDao.findByLogin(login)
            }

            override fun createCall(): LiveData<ApiResponse<User>> {
                return githubApi.getUser(login)
            }
        }.asLiveData()
    }
}