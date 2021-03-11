package com.aci.constructionequipment.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aci.constructionequipment.R
import com.aci.constructionequipment.core.BaseActivity
import com.aci.constructionequipment.core.NavActivity
import com.aci.constructionequipment.core.NavEngineerActivity
import com.aci.constructionequipment.core.NavTechnicianActivity
import com.aci.constructionequipment.networking.*

import com.acibd.serviceautonomous.networking.all_data.AllData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


//        LocationHelper().startListeningUserLocation(this , object : LocationHelper.MyLocationListener {
//            override fun onLocationChanged(location: Location) {
//                // Here you got user location :)
//                Log.d("Location","" + location.latitude + "," + location.longitude)
//            }
//        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (!hasNetworkAvailable())
                showsnackbar()

            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager?.let {

                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        //take action when network connection is gained
                        snackbar?.let {
                            if (it.isShown)
                                it.dismiss()
                        }
                    }

                    override fun onLost(network: Network?) {
                        //take action when network connection is lost
                        showsnackbar()
                    }
                })
            }
        }

        btnLogin.setOnClickListener {

            checkValidation()


        }
    }


    private fun checkValidation() {

        if (username.editText!!.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please insert username", Toast.LENGTH_LONG).show()
            return
        }


        if (password.editText!!.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please insert password", Toast.LENGTH_LONG).show()
            return
        }

        if (cbLoginAsTechnitian.isChecked) {
            callAPILoginTech()
        } else {
            callAPILogin()
        }


    }


    private fun callAPILogin() {

        showProgressDialog()

        ApiClient.build()?.login(
            LoginPostBody(
                username.editText!!.text.toString(),
                password.editText!!.text.toString()
            )
        )?.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {


                Log.d("login response", response.code().toString())

                if (response.isSuccessful) {

                    response.body().let {
                        sharedPref?.edit()?.putString("token", "token ${it?.token}")?.apply()
                        sharedPref?.edit()
                            ?.putString("username", username.editText!!.text.toString())?.apply()
                        getFirebaseInstanceId(it?.token!!)
                        getAllData("token ${it.token}", true)
                    }

                } else {
                    hideProgressDialog()
                    Log.d("login response failed", response.errorBody()?.string())
                    Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                hideProgressDialog()
                t.printStackTrace()
                Log.d("login Failed", t.message)
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun callAPILoginTech() {

        showProgressDialog()

        ApiClient.build()?.loginAsTechnician(
            LoginTechPostBody(
                username.editText!!.text.toString(),
                password.editText!!.text.toString()
            )
        )?.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {


                Log.d("login response", response.code().toString())

                if (response.isSuccessful) {

                    response.body().let {
                        sharedPref?.edit()?.putString("token", "token ${it?.token}")?.apply()
                        sharedPref?.edit()
                            ?.putString("username", username.editText!!.text.toString())?.apply()
                        getFirebaseInstanceId(it?.token!!)
                        getAllData("token ${it?.token}", false)
                    }

                } else {
                    hideProgressDialog()
                    Log.d("login response failed", response.errorBody()?.string())
                    Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                hideProgressDialog()
                t.printStackTrace()
                Log.d("login Failed", t.message)
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })

    }


    private fun getAllData(token: String, isUser: Boolean) {
        ApiClient.build()?.allData(
            token = token
        )?.enqueue(object : Callback<AllData> {

            override fun onResponse(call: Call<AllData>, response: Response<AllData>) {
                hideProgressDialog()

                Log.d("alldata response", response.code().toString())

                if (response.isSuccessful) {

                    response.body().let {
                        sharedPref?.edit()?.putString("alldata", Gson().toJson(it))?.apply()

                        if (isUser) {
                            openMainDashboard()
                        } else {
                            getUserInfo(token)
                        }

                    }

                } else {
                    Log.d("alldata response failed", response.errorBody()?.string())
                    Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AllData>, t: Throwable) {
                hideProgressDialog()
                t.printStackTrace()
                Log.d("alldata Failed", t.message)
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun getUserInfo(token: String) {
        ApiClient.build()?.userInfo(
            token = token
        )?.enqueue(object : Callback<UserInfo> {

            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                hideProgressDialog()

                Log.d("alldata response", response.code().toString())

                if (response.isSuccessful) {

                    response.body().let {
                        if (it?.data?.role?.code.equals("technician")) {
                            openMainDashboardTechnician()
                        } else if (it?.data?.role?.code.equals("engineer")) {
                            openMainDashboardEngineer()
                        } else {
                            Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_LONG).show()
                        }
                    }

                } else {
                    Log.d("alldata response failed", response.errorBody()?.string())
                    Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                hideProgressDialog()
                t.printStackTrace()
                Log.d("alldata Failed", t.message)
                Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun openMainDashboard() {
        val intent = Intent(this@LoginActivity, NavActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun openMainDashboardTechnician() {
        val intent = Intent(this@LoginActivity, NavTechnicianActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun openMainDashboardEngineer() {
        val intent = Intent(this@LoginActivity, NavEngineerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    fun showsnackbar() {
        if (snackbar == null) {
            snackbar = Snackbar.make(rlLoginRoot, "ইন্টারনেট সংযোগ নেই", Snackbar.LENGTH_INDEFINITE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setBackgroundTint(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )

        }

        if (!snackbar!!.isShown) {
            snackbar!!.show()
        }
    }


    private fun getFirebaseInstanceId(userToken: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                Log.d("firebase-token", token)


                postFirebaseToken(userToken, DeviceToken(firebase_token = token!!))
                Log.d("MainActivity-token", token)
            })
    }

    fun postFirebaseToken(
        token: String,
        deviceToken: DeviceToken
    ) {


        ApiClient.build()?.deviceToken(token, deviceToken)
            ?.enqueue(object : Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d("postFirebaseToken", Gson().toJson(response.body()))
                        }
                    } else {
                        Log.d("postFirebaseToken", "OnFailure - " + response.errorBody()?.string())

                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {

                    t.printStackTrace()
                    Log.d("postFirebaseToken", "OnFailure - " + t.message)

                }
            })
    }

}
