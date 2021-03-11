package com.work.onlineleave.viewmodel.myleave

import android.util.Log
import com.work.onlineleave.data.ApiClient
import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.my_leave.MyLeaveList
import com.work.onlineleave.data.my_leave.delete.MyLeaveDelete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyLeaveRepository() {

    fun myleavelist(
        empcode: String,
        loginCallBack: OperationCallback<MyLeaveList>
    ) {
        ApiClient.build()?.getMyLeaveList(empcode, "0", "25")
            ?.enqueue(object : Callback<MyLeaveList> {
                override fun onResponse(
                    call: Call<MyLeaveList>,
                    response: Response<MyLeaveList>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("myleavelist", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<MyLeaveList>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("myleavelist", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }

    fun myleavelistLoadMore(
        empcode: String,
        start: String,
        loginCallBack: OperationCallback<MyLeaveList>
    ) {
        ApiClient.build()?.getMyLeaveList(empcode, start, "25")
            ?.enqueue(object : Callback<MyLeaveList> {
                override fun onResponse(
                    call: Call<MyLeaveList>,
                    response: Response<MyLeaveList>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("myleavelistLoadMore", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<MyLeaveList>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("myleavelistLoadMore", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }


    fun myLeaveDelete(
        leaverefno: String,
        loginCallBack: OperationCallback<MyLeaveDelete>
    ) {

        ApiClient.build()?.myLeaveDelete(leaverefno)
            ?.enqueue(object : Callback<MyLeaveDelete> {
                override fun onResponse(
                    call: Call<MyLeaveDelete>,
                    response: Response<MyLeaveDelete>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("myLeaveDelete", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<MyLeaveDelete>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("myLeaveDelete", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }

}

//http://dashboard.acigroup.info/emisaci/leave/myleave/delete?leaverefno=157739
//http://dashboard.acigroup.info/emisaci/leave/myleave/delete?leaverefno=157741