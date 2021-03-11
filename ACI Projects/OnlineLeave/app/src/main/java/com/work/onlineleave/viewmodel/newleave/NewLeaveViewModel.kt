package com.work.onlineleave.viewmodel.newleave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.applyto.ApplyTo
import com.work.onlineleave.data.leavetype.LeaveType
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.new_leave.NewLeaveResponse
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.data.new_leave_days.Leavedays

class NewLeaveViewModel(private val repository: NewLeaveRepository) : ViewModel() {

    private val _applutoList = MutableLiveData<ApplyTo>()
    val applutoList: LiveData<ApplyTo> = _applutoList

    private val _leavetypeList = MutableLiveData<LeaveType>()
    val leavetypeList: LiveData<LeaveType> = _leavetypeList

    private val _newLeaveEditInfo = MutableLiveData<NewLeaveEditInfo>()
    val newLeaveEditInfo: LiveData<NewLeaveEditInfo> = _newLeaveEditInfo



    private val _leavedays = MutableLiveData<Leavedays>()
    val leavedays: LiveData<Leavedays> = _leavedays


    private val _createNewLeaveResponse = MutableLiveData<NewLeaveResponse>()
    val createNewLeaveResponse: LiveData<NewLeaveResponse> = _createNewLeaveResponse

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    fun applyto(empcode: String) {
        _isViewLoading.postValue(true)
        repository.getapplytoList(empcode, object : OperationCallback<ApplyTo> {
            override fun onSuccess(data: ApplyTo) {
                _isViewLoading.postValue(false)
                _applutoList.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun leavetype() {
        _isViewLoading.postValue(true)
        repository.getLeavetypeList(object : OperationCallback<LeaveType> {
            override fun onSuccess(data: LeaveType) {
                _isViewLoading.postValue(false)
                _leavetypeList.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun getEditInfo(leaverefno: String) {
        _isViewLoading.postValue(true)
        repository.getnewleaveeditinfo(leaverefno, object : OperationCallback<NewLeaveEditInfo> {
            override fun onSuccess(data: NewLeaveEditInfo) {
                _isViewLoading.postValue(false)
                _newLeaveEditInfo.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun getLeaveAppDays(
        leaveappfrom: String,
        leaveappto: String,
        leavefor: String
    ) {
        _isViewLoading.postValue(true)
        repository.getLeaveAppDays(
            leaveappfrom,
            leaveappto,
            leavefor,
            object : OperationCallback<Leavedays> {
                override fun onSuccess(data: Leavedays) {
                    _isViewLoading.postValue(false)
                    _leavedays.postValue(data)
                }

                override fun onError(error: String?) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(error)
                }
            })
    }



    fun createNewLeave(
        leaveappfrom: String,
        leaveappto: String,
        empcode: String,
        empname: String,
        deptname: String,
        leaveappdate: String,
        desgname: String,
        leaverefno: String,
        emailid: String,
        reason: String,
        authcode: String,
        leavefor: String,
        leaveappdays: String,
        deptcode: String,
        worklocation: String,
        appworklocation: String,
        lfa: String
    ) {

        repository.createNewLeave(
            leaveappfrom,
            leaveappto,
            empcode,
            empname,
            deptname,
            leaveappdate,
            desgname,
            leaverefno,
            emailid,
            reason,
            authcode,
            leavefor,
            leaveappdays,
            deptcode,
            worklocation,
            appworklocation,
            lfa,
            object : OperationCallback<NewLeaveResponse> {
                override fun onSuccess(data: NewLeaveResponse) {
                    _isViewLoading.postValue(false)
                    _createNewLeaveResponse.postValue(data)
                }

                override fun onError(error: String?) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(error)
                }
            }
        )
    }


    fun updateNewLeave(
        leaveappfrom: String,
        leaveappto: String,
        empcode: String,
        empname: String,
        deptname: String,
        leaveappdate: String,
        desgname: String,
        leaverefno: String,
        emailid: String,
        reason: String,
        authcode: String,
        leavefor: String,
        leaveappdays: String,
        deptcode: String,
        worklocation: String,
        appworklocation: String,
        lfa: String
    ) {

        repository.updateNewLeave(
            leaveappfrom,
            leaveappto,
            empcode,
            empname,
            deptname,
            leaveappdate,
            desgname,
            leaverefno,
            emailid,
            reason,
            authcode,
            leavefor,
            leaveappdays,
            deptcode,
            worklocation,
            appworklocation,
            lfa,
            object : OperationCallback<NewLeaveResponse> {
                override fun onSuccess(data: NewLeaveResponse) {
                    _isViewLoading.postValue(false)
                    _createNewLeaveResponse.postValue(data)
                }

                override fun onError(error: String?) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(error)
                }
            }
        )
    }


}