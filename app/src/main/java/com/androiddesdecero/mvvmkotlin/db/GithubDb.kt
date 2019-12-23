package com.androiddesdecero.mvvmkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androiddesdecero.mvvmkotlin.model.Contributor
import com.androiddesdecero.mvvmkotlin.model.Repo
import com.androiddesdecero.mvvmkotlin.model.RepoSearchResult
import com.androiddesdecero.mvvmkotlin.model.User

@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class
    ],
    version = 1
)
abstract class GithubDb: RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao
}