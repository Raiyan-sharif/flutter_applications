package com.work.onlineleave.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DashboardModelFactory(private val repository: DashboardRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository) as T
    }
}