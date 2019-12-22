package com.androiddesdecero.mvvmkotlin.model

import com.google.gson.annotations.SerializedName

class RepoSearchResponse (
    @SerializedName("total_count")
    val total: Int = 0,
    @SerializedName("items")
    val items: List<Repo>
){
    var nextPage: Int? = null
}