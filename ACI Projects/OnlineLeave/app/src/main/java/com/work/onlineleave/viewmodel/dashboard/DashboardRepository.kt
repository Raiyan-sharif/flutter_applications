package com.work.onlineleave.viewmodel.dashboard

import android.util.Log
import com.work.onlineleave.data.ApiClient
import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.leave_status.LeaveStatus
import com.work.onlineleave.data.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository() {

    fun leaveStatus(
        empcode: String,
        loginCallBack: OperationCallback<LeaveStatus>
    ) {
        ApiClient.build()?.leaveStatus(empcode)
            ?.enqueue(object : Callback<LeaveStatus> {
                override fun onResponse(
                    call: Call<LeaveStatus>,
                    response: Response<LeaveStatus>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("leaveStatus", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<LeaveStatus>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("leaveStatus", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }

}