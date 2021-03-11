package com.work.onlineleave.viewmodel.login

import android.util.Log
import com.work.onlineleave.data.ApiClient
import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository() {

    fun postLogin(
        userName: String,
        password: String,
        loginCallBack: OperationCallback<LoginResponse>
    ) {
        ApiClient.build()?.login(userName, password)
            ?.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { loginCallBack.onSuccess(it) }
                    } else {
                        Log.d("LoginRepository", "OnFailure - " + response.errorBody()?.string())
                        loginCallBack.onError("Failed")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginRepository", "OnFailure - " + t.message)
                    loginCallBack.onError("Failed")
                }
            })
    }

}