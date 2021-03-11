package com.work.onlineleave.viewmodel.govtholiday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class GovtHolidayModelFactory(private val repository: GovtHolidayRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GovtHolidayViewModel(repository) as T
    }
}