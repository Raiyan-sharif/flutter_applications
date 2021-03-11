package com.work.onlineleave.viewmodel.my_approval

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MyApprovalModelFactory(private val repository: MyApprovalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyApprovalViewModel(repository) as T
    }
}