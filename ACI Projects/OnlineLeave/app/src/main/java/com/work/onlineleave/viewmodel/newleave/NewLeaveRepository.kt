package com.work.onlineleave.viewmodel.newleave

import android.util.Log
import com.work.onlineleave.data.ApiClient
import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.applyto.ApplyTo
import com.work.onlineleave.data.leavetype.LeaveType
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.new_leave.NewLeaveResponse
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.data.new_leave_days.Leavedays
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewLeaveRepository() {

    fun getapplytoList(
        empcode: String,
        loginCallBack: OperationCallback<ApplyTo>
    ) {
        ApiClient.build()?.applyto(empcode)
            ?.enqueue(object : Callback<ApplyTo> {
                override fun onResponse(
                    call: Call<ApplyTo>,
                    response: Response<ApplyTo>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("NewLeaveRepository", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<ApplyTo>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("NewLeaveRepository", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }


    fun getLeavetypeList(
        loginCallBack: OperationCallback<LeaveType>
    ) {
        ApiClient.build()?.leavetype()
            ?.enqueue(object : Callback<LeaveType> {
                override fun onResponse(
                    call: Call<LeaveType>,
                    response: Response<LeaveType>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("NewLeaveRepository", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<LeaveType>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("NewLeaveRepository", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }

    fun getnewleaveeditinfo(
        leaverefno: String,
        loginCallBack: OperationCallback<NewLeaveEditInfo>
    ) {
        ApiClient.build()?.getNewLeaveEditInfo(leaverefno)
            ?.enqueue(object : Callback<NewLeaveEditInfo> {
                override fun onResponse(
                    call: Call<NewLeaveEditInfo>,
                    response: Response<NewLeaveEditInfo>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d(
                            "getnewleaveeditinfo",
                            "OnFailure - " + response.errorBody()?.string()
                        )
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<NewLeaveEditInfo>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("getnewleaveeditinfo", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }




    fun getLeaveAppDays(
        leaveappfrom: String,
        leaveappto: String,
        leavefor: String,
        loginCallBack: OperationCallback<Leavedays>
    ) {
        ApiClient.build()?.leavedays(leaveappfrom, leaveappto, leavefor)
            ?.enqueue(object : Callback<Leavedays> {
                override fun onResponse(
                    call: Call<Leavedays>,
                    response: Response<Leavedays>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("getLeaveAppDays", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<Leavedays>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("getLeaveAppDays", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
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
        lfa: String,
        loginCallBack: OperationCallback<NewLeaveResponse>
    ) {
        ApiClient.build()?.newLeaveCreate(
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
                lfa
            )
            ?.enqueue(object : Callback<NewLeaveResponse> {
                override fun onResponse(
                    call: Call<NewLeaveResponse>,
                    response: Response<NewLeaveResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("createNewLeave", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<NewLeaveResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("createNewLeave", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
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
        lfa: String,
        loginCallBack: OperationCallback<NewLeaveResponse>
    ) {
        ApiClient.build()?.newLeaveUpdate(
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
                lfa
            )
            ?.enqueue(object : Callback<NewLeaveResponse> {
                override fun onResponse(
                    call: Call<NewLeaveResponse>,
                    response: Response<NewLeaveResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("createNewLeave", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<NewLeaveResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("createNewLeave", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }

}