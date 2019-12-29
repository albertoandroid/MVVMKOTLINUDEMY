package com.androiddesdecero.mvvmkotlin.ui.repo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.androiddesdecero.mvvmkotlin.AppExecutors

import com.androiddesdecero.mvvmkotlin.R
import com.androiddesdecero.mvvmkotlin.binding.FragmentDataBindingComponent
import com.androiddesdecero.mvvmkotlin.databinding.FragmentRepoBinding
import com.androiddesdecero.mvvmkotlin.di.Injectable
import com.androiddesdecero.mvvmkotlin.utils.autoCleared
import java.lang.NumberFormatException
import javax.inject.Inject


class RepoFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val repoViewModel: RepoViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentRepoBinding>()

    private val params by navArgs<RepoFragmentArgs>()
    private var adapter by autoCleared<ContributorAdapter>()

    private fun initContributorList(viewModel: RepoViewModel){
        viewModel.contributors.observe(viewLifecycleOwner, Observer {
            listResource->
            if(listResource?.data != null){
                adapter.submitList(listResource.data)
            } else {
                adapter.submitList(emptyList())
            }
        })
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo, container, false)
    }


}
