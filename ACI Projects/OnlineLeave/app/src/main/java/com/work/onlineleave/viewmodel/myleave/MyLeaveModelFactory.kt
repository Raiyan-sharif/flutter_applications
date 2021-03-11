package com.work.onlineleave.viewmodel.myleave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MyLeaveModelFactory(private val repository: MyLeaveRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyLeaveViewModel(repository) as T
    }
}