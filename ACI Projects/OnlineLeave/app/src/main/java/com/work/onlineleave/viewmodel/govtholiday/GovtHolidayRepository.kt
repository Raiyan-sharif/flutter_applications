package com.work.onlineleave.viewmodel.govtholiday

import android.util.Log
import com.work.onlineleave.data.ApiClient
import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.govt_holiday.GovtHoliday
import com.work.onlineleave.data.my_leave.MyLeaveList
import com.work.onlineleave.data.my_leave.delete.MyLeaveDelete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GovtHolidayRepository() {

    fun getGovtHolidayList(
        worklocation: String,
        loginCallBack: OperationCallback<GovtHoliday>
    ) {
        ApiClient.build()?.getGovtHolidayList(worklocation)
            ?.enqueue(object : Callback<GovtHoliday> {
                override fun onResponse(
                    call: Call<GovtHoliday>,
                    response: Response<GovtHoliday>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("getGovtHolidayList", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<GovtHoliday>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("getGovtHolidayList", "OnFailure - " + t.message)
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