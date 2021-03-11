package com.work.onlineleave.viewmodel.newleave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class NewLeaveViewModelFactory(private val repository: NewLeaveRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewLeaveViewModel(repository) as T
    }
}