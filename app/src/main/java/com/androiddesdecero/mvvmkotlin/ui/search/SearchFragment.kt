package com.androiddesdecero.mvvmkotlin.ui.search


import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androiddesdecero.mvvmkotlin.AppExecutors

import com.androiddesdecero.mvvmkotlin.R
import com.androiddesdecero.mvvmkotlin.binding.FragmentDataBindingComponent
import com.androiddesdecero.mvvmkotlin.databinding.FragmentSearchBinding
import com.androiddesdecero.mvvmkotlin.di.Injectable
import com.androiddesdecero.mvvmkotlin.ui.common.RepoListAdapter
import com.androiddesdecero.mvvmkotlin.ui.common.RetryCallback
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

    val searchViewModel: SearchViewModel by viewModels{
        viewModelFactory
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setLifecycleOwner (viewLifecycleOwner)
        initRecyclerView()
        val rvAdapter = RepoListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors,
            showFullName = true
        ){
            repo->
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToRepoFragment(repo.name, repo.owner.login)
            )
        }
        binding.query = searchViewModel.queryLD
        binding.repoList.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()

        binding.callback = object: RetryCallback{
            override fun retry() {
                searchViewModel.refresh()
            }
        }
    }

    private fun initSearchInputListener(){
        binding.input.setOnEditorActionListener{view: View, actionId: Int, _: KeyEvent?->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                doSearch(view)
                true
            } else {
                false
            }
        }

        binding.input.setOnKeyListener{view: View, keyCode: Int, event: KeyEvent->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                doSearch(view)
                true
            }else{
                false
            }
        }
    }





}
