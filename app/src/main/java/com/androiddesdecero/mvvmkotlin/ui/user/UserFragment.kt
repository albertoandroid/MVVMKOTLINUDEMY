package com.androiddesdecero.mvvmkotlin.ui.user


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androiddesdecero.mvvmkotlin.AppExecutors

import com.androiddesdecero.mvvmkotlin.R
import com.androiddesdecero.mvvmkotlin.binding.FragmentDataBindingComponent
import com.androiddesdecero.mvvmkotlin.databinding.FragmentUserBinding
import com.androiddesdecero.mvvmkotlin.di.Injectable
import com.androiddesdecero.mvvmkotlin.ui.common.RepoListAdapter
import com.androiddesdecero.mvvmkotlin.ui.common.RetryCallback
import com.androiddesdecero.mvvmkotlin.utils.autoCleared
import com.androiddesdecero.mvvmkotlin.viewmodel.GithubViewModelFactory
import javax.inject.Inject


class UserFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentUserBinding>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private val userViewModel: UserViewModel by viewModels {
        viewModelFactory
    }

    private val params by navArgs<UserFragmentArgs>()
    private var adapter by autoCleared<RepoListAdapter>()
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user, container, false)

        val dataBinding = DataBindingUtil.inflate<FragmentUserBinding>(
            inflater,
            R.layout.fragment_user,
            container,
            false,
            dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback{
            override fun retry() {
                userViewModel.retry()
            }
        }
        binding = dataBinding
        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)

        //postponeEnterTransition()
        return  dataBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = UserFragmentArgs.fromBundle(arguments!!)
        userViewModel.setLogin(params.login)
        userViewModel.user.observe(this, Observer {
            userResource ->
            binding.user = userResource?.data
            binding.userResource = userResource
        })

        val rvAdapter = RepoListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors,
            showFullName = false
        ){
            repo->
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToRepoFragment(repo.name, repo.owner.login))
        }
        binding.repoList.adapter = rvAdapter
        this.adapter = rvAdapter
        initRepoList()

    }

    private fun initRepoList(){
        userViewModel.repositories.observe(this, Observer {
            repos->
            adapter.submitList(repos?.data)
        })
    }

}
