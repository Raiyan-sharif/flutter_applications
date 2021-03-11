package com.work.onlineleave.viewmodel.my_approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.applyto.ApplyTo
import com.work.onlineleave.data.my_approval.MyApproval
import com.work.onlineleave.data.my_approval.approve.CommonResponse
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.data.new_leave_days.Leavedays

class MyApprovalViewModel(private val repository: MyApprovalRepository) : ViewModel() {

    private val _myApprovalListResponse = MutableLiveData<MyApproval>()
    val myApprovalListResponse: LiveData<MyApproval> = _myApprovalListResponse


    private val _leaveInfo = MutableLiveData<NewLeaveEditInfo>()
    val leaveInfo: LiveData<NewLeaveEditInfo> = _leaveInfo

    private val _leavedays = MutableLiveData<Leavedays>()
    val leavedays: LiveData<Leavedays> = _leavedays

    private val _myApprovalApproveResponse = MutableLiveData<CommonResponse>()
    val commonResponseResponse: LiveData<CommonResponse> = _myApprovalApproveResponse


    private val _myApprovalRefuseResponse = MutableLiveData<CommonResponse>()
    val myApprovalRefuseResponse: LiveData<CommonResponse> = _myApprovalRefuseResponse

    private val _myApprovalCancelResponse = MutableLiveData<CommonResponse>()
    val myApprovalCancelResponse: LiveData<CommonResponse> = _myApprovalCancelResponse

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    private val _applutoList = MutableLiveData<ApplyTo>()
    val applutoList: LiveData<ApplyTo> = _applutoList


    fun getMyApprovalList(empCode: String) {
        _isViewLoading.postValue(true)
        repository.myApprovallist(empCode, object : OperationCallback<MyApproval> {
            override fun onSuccess(data: MyApproval) {
                _isViewLoading.postValue(false)
                _myApprovalListResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun getMyApprovalCancelRequest(leaverefno: String) {
        _isViewLoading.postValue(true)
        repository.getMyApprovalCancelRequest(leaverefno, object : OperationCallback<CommonResponse> {
            override fun onSuccess(data: CommonResponse) {
                _isViewLoading.postValue(false)
                _myApprovalCancelResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun myApprovalApprove(
        leaveapprovefrom: String,
        leaveapproveto: String,
        empcode: String,
        empname: String,
        deptname: String,
        leaveappdate: String,
        desgname: String,
        leaverefno: String,
        emailid: String,
        reason: String,
        authname: String,
        leavedetails: String,
        leaveappfrom: String,
        leaveappto: String,
        leaveappdays: String,
        approvefrom: String,
        approveto: String,
        leavegrantdays: String,
        reasontorefuse: String,
        authcode: String
    ) {
        _isViewLoading.postValue(true)
        repository.myApprovalApprove(leaveapprovefrom,
            leaveapproveto,
            empcode,
            empname,
            deptname,
            leaveappdate,
            desgname,
            leaverefno,
            emailid,
            reason,
            authname,
            leavedetails,
            leaveappfrom,
            leaveappto,
            leaveappdays,
            approvefrom,
            approveto,
            leavegrantdays,
            reasontorefuse,
            authcode,
            object : OperationCallback<CommonResponse> {
                override fun onSuccess(data: CommonResponse) {
                    _isViewLoading.postValue(false)
                    _myApprovalApproveResponse.postValue(data)
                }

                override fun onError(error: String?) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(error)
                }
            })
    }


    fun myApprovalRefuse(
        leaveapprovefrom: String,
        leaveapproveto: String,
        empcode: String,
        empname: String,
        deptname: String,
        leaveappdate: String,
        desgname: String,
        leaverefno: String,
        emailid: String,
        reason: String,
        authname: String,
        leavedetails: String,
        leaveappfrom: String,
        leaveappto: String,
        leaveappdays: String,
        approvefrom: String,
        approveto: String,
        leavegrantdays: String,
        reasontorefuse: String,
        authcode: String
    ) {
        _isViewLoading.postValue(true)
        repository.myApprovalRefuse(leaveapprovefrom,
            leaveapproveto,
            empcode,
            empname,
            deptname,
            leaveappdate,
            desgname,
            leaverefno,
            emailid,
            reason,
            authname,
            leavedetails,
            leaveappfrom,
            leaveappto,
            leaveappdays,
            approvefrom,
            approveto,
            leavegrantdays,
            reasontorefuse,
            authcode,
            object : OperationCallback<CommonResponse> {
                override fun onSuccess(data: CommonResponse) {
                    _isViewLoading.postValue(false)
                    _myApprovalRefuseResponse.postValue(data)
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
                _leaveInfo.postValue(data)
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


}