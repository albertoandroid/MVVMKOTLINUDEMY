package com.androiddesdecero.mvvmkotlin.ui.search


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androiddesdecero.mvvmkotlin.AppExecutors

import com.androiddesdecero.mvvmkotlin.R
import com.androiddesdecero.mvvmkotlin.binding.FragmentDataBindingComponent
import com.androiddesdecero.mvvmkotlin.databinding.FragmentSearchBinding
import com.androiddesdecero.mvvmkotlin.di.Injectable
import com.androiddesdecero.mvvmkotlin.ui.common.RepoListAdapter
import com.androiddesdecero.mvvmkotlin.utils.autoCleared
import com.androiddesdecero.mvvmkotlin.viewmodel.GithubViewModelFactory
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSearchBinding>()

    var adapter by autoCleared<RepoListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search,
            container,
            false,
            dataBindingComponent)

        return binding.root
    }



}
