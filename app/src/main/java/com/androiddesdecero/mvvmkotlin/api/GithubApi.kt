package com.androiddesdecero.mvvmkotlin.api

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.androiddesdecero.mvvmkotlin.model.Contributor
import com.androiddesdecero.mvvmkotlin.model.Repo
import com.androiddesdecero.mvvmkotlin.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("user/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): LiveData<ApiResponse<List<Repo>>>

    @GET("repos/{owner}/{name}")
    fun getRepo(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): LiveData<ApiResponse<Repo>>

    @GET("repos/{owner}/{name}/contributors")
    fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): LiveData<ApiResponse<List<Contributor>>>
}