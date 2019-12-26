package com.androiddesdecero.mvvmkotlin.ui.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.androiddesdecero.mvvmkotlin.AppExecutors
import com.androiddesdecero.mvvmkotlin.R
import com.androiddesdecero.mvvmkotlin.model.Contributor
import com.androiddesdecero.mvvmkotlin.ui.common.DataBoundListAdapter

class ContributorAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Contributor)->Unit)?
): DataBoundListAdapter<Contributor, com.androiddesdecero.mvvmkotlin.databinding.ContributorItemBinding> (
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Contributor>(){
        override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
            return oldItem.avatarUrl == newItem.avatarUrl && oldItem.contributions == newItem.contributions
        }

    }
){
    override fun createBinding(parent: ViewGroup): com.androiddesdecero.mvvmkotlin.databinding.ContributorItemBinding {
        val binding = DataBindingUtil
            .inflate<com.androiddesdecero.mvvmkotlin.databinding.ContributorItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.contributor_item,
                parent,
                false,
                dataBindingComponent
            )
        binding.root.setOnClickListener{
            binding.contributor?.let{
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: com.androiddesdecero.mvvmkotlin.databinding.ContributorItemBinding, item: Contributor) {
        binding.contributor = item
    }
}