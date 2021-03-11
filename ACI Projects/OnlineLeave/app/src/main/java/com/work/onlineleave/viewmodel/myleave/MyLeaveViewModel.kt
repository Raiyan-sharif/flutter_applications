package com.work.onlineleave.viewmodel.myleave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.leave_status.LeaveStatus
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.my_leave.MyLeaveList
import com.work.onlineleave.data.my_leave.delete.MyLeaveDelete

class MyLeaveViewModel(private val repository: MyLeaveRepository) : ViewModel() {

    private val _myLeaveListResponse = MutableLiveData<MyLeaveList>()
    val myLeaveListResponse: LiveData<MyLeaveList> = _myLeaveListResponse


    private val _myLeaveListLoadMoreResponse = MutableLiveData<MyLeaveList>()
    val myLeaveListLoadMoreResponse: LiveData<MyLeaveList> = _myLeaveListLoadMoreResponse

    private val _myLeaveDeleteResponse = MutableLiveData<MyLeaveDelete>()
    val myLeaveDeleteResponse: LiveData<MyLeaveDelete> = _myLeaveDeleteResponse

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    fun getMyLeaveList(empCode: String) {
        _isViewLoading.postValue(true)
        repository.myleavelist(empCode, object : OperationCallback<MyLeaveList> {
            override fun onSuccess(data: MyLeaveList) {
                _isViewLoading.postValue(false)
                _myLeaveListResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }


    fun getMyLeaveListLoadMore(empCode: String, start: String) {
        _isViewLoading.postValue(true)
        repository.myleavelistLoadMore(empCode, start, object : OperationCallback<MyLeaveList> {
            override fun onSuccess(data: MyLeaveList) {
                _isViewLoading.postValue(false)
                _myLeaveListLoadMoreResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun myLeaveDelete(leaverefno: String) {
        _isViewLoading.postValue(true)
        repository.myLeaveDelete(leaverefno, object : OperationCallback<MyLeaveDelete> {
            override fun onSuccess(data: MyLeaveDelete) {
                _isViewLoading.postValue(false)
                _myLeaveDeleteResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

}