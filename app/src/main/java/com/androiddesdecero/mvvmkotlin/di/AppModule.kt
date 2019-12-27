package com.androiddesdecero.mvvmkotlin.di

import android.app.Application
import androidx.room.Room
import com.androiddesdecero.mvvmkotlin.api.GithubApi
import com.androiddesdecero.mvvmkotlin.db.GithubDb
import com.androiddesdecero.mvvmkotlin.db.RepoDao
import com.androiddesdecero.mvvmkotlin.db.UserDao
import com.androiddesdecero.mvvmkotlin.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideGithubApi(): GithubApi{
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(GithubApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): GithubDb{
        return Room.databaseBuilder(app, GithubDb::class.java, "github.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: GithubDb): UserDao{
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideRepoDao(db: GithubDb): RepoDao{
        return db.repoDao()
    }
}