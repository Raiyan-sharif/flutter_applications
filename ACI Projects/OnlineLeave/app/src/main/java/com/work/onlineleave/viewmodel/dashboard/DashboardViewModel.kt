package com.work.onlineleave.viewmodel.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.leave_status.LeaveStatus
import com.work.onlineleave.data.login.LoginResponse

class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val _leaveStatusResponse = MutableLiveData<LeaveStatus>()
    val leaveStatusResponse: LiveData<LeaveStatus> = _leaveStatusResponse

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    fun leaveStatus(empCode: String) {
        _isViewLoading.postValue(true)
        repository.leaveStatus(empCode, object : OperationCallback<LeaveStatus> {
            override fun onSuccess(data: LeaveStatus) {
                _isViewLoading.postValue(false)
                _leaveStatusResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

}