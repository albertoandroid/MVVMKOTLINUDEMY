package com.androiddesdecero.mvvmkotlin.binding

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean){
        view.visibility = if(show) View.VISIBLE else View.GONE
    }
}