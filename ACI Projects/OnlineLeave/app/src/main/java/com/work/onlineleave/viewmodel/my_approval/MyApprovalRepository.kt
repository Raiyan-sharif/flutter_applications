package com.work.onlineleave.viewmodel.my_approval

import android.util.Log
import com.work.onlineleave.data.ApiClient
import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.applyto.ApplyTo
import com.work.onlineleave.data.my_approval.MyApproval
import com.work.onlineleave.data.my_approval.approve.CommonResponse
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.data.new_leave_days.Leavedays
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyApprovalRepository() {

    fun myApprovallist(
        empcode: String,
        loginCallBack: OperationCallback<MyApproval>
    ) {
        ApiClient.build()?.getMyApprovalList(empcode, "0", "25")
            ?.enqueue(object : Callback<MyApproval> {
                override fun onResponse(
                    call: Call<MyApproval>,
                    response: Response<MyApproval>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("myApprovallist", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<MyApproval>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("myApprovallist", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }


    fun getMyApprovalCancelRequest(
        leaverefno: String,
        loginCallBack: OperationCallback<CommonResponse>
    ) {
        ApiClient.build()?.getMyApprovalCancelRequest(leaverefno)
            ?.enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d(
                            "getMyApprovalCancelRequest",
                            "OnFailure - " + response.errorBody()?.string()
                        )
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("getMyApprovalCancelRequest", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
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
        authcode: String,
        loginCallBack: OperationCallback<CommonResponse>
    ) {
        ApiClient.build()?.newApprovalApprove(
                leaveapprovefrom,
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
                authcode
            )
            ?.enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("myApprovallist", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("myApprovallist", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
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
        authcode: String,
        loginCallBack: OperationCallback<CommonResponse>
    ) {
        ApiClient.build()?.newApprovalRefuse(
                leaveapprovefrom,
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
                authcode
            )
            ?.enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("myApprovalRefuse", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("myApprovalRefuse", "OnFailure - " + t.message)
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


}

//http://dashboard.acigroup.info/emisaci/leave/myleave/delete?leaverefno=157739
//http://dashboard.acigroup.info/emisaci/leave/myleave/delete?leaverefno=157741