package com.work.onlineleave.viewmodel.govtholiday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.govt_holiday.GovtHoliday
import com.work.onlineleave.data.leave_status.LeaveStatus
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.my_leave.MyLeaveList
import com.work.onlineleave.data.my_leave.delete.MyLeaveDelete

class GovtHolidayViewModel(private val repository: GovtHolidayRepository) : ViewModel() {

    private val _govtHolidayListResponse = MutableLiveData<GovtHoliday>()
    val govtHolidayListResponse: LiveData<GovtHoliday> = _govtHolidayListResponse


    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    fun govtHolidayList(worklocation: String) {
        _isViewLoading.postValue(true)
        repository.getGovtHolidayList(worklocation, object : OperationCallback<GovtHoliday> {
            override fun onSuccess(data: GovtHoliday) {
                _isViewLoading.postValue(false)
                _govtHolidayListResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }


}