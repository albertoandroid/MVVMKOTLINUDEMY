package com.androiddesdecero.mvvmkotlin.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.androiddesdecero.mvvmkotlin.AppExecutors
import com.androiddesdecero.mvvmkotlin.api.ApiEmptyResponse
import com.androiddesdecero.mvvmkotlin.api.ApiResponse
import com.androiddesdecero.mvvmkotlin.api.ApiSuccessResponse

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors){
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource){data->
            result.removeSource(dbSource)
            if(shouldFetch(data)){
                fetchFromNetwork(dbSource)
            } else{
                result.addSource(dbSource){newData->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>){
        if(result.value != newValue){
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>){
        val apiResponse = createCall()
        result.addSource(dbSource){newData->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse){response->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when(response){
                is ApiSuccessResponse->{
                    appExecutors.diskIO().execute{
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute{
                            result.addSource(loadFromDb()){newData->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse->{
                    appExecutors.mainThread().execute{
                        result.addSource(loadFromDb()){newData->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiEmptyResponse->{
                    onFetchFailed()
                    result.addSource(dbSource){newData->
                        setValue(Resource.error(response, newData))
                    }
                }
            }

        }
    }

    protected open fun onFetchFailed(){}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}