package com.androiddesdecero.mvvmkotlin.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.androiddesdecero.mvvmkotlin.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
class RepoSearchResult (
    val query: String,
    val repoIds: List<Int>,
    val totalCount: Int,
    val next: Int?
)