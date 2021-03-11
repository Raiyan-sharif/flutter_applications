package com.work.onlineleave.view.login


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.work.onlineleave.BuildConfig
import com.work.onlineleave.R
import com.work.onlineleave.core.BaseActivity
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.di.Injection
import com.work.onlineleave.view.dashboard.MainActivity
import com.work.onlineleave.viewmodel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_update_app.view.*

// username : 05581  password:  ra@hasib

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        setupViewModel()
        checkFirebaseCOnfig()


        btnLogin.isClickable = true
        btnLogin.isEnabled = true


        btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {


                if (!hasNetworkAvailable()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "No Internet Connection",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                if (!username.text.isNullOrBlank() && !password.text.isNullOrBlank()) {

                    loginViewModel.postLogin(
                        userName = username.text.toString(),
                        password = password.text.toString()
                    )

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Pease insert Username & Password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun checkFirebaseCOnfig() {

        loginViewModel.isViewLoading(true)

        var mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        mFirebaseRemoteConfig?.setConfigSettings(configSettings)


        var cacheExpiration: Long = 3600

        //setting the default values for the UI
        mFirebaseRemoteConfig?.setDefaults(R.xml.remote_config_defaults)

        mFirebaseRemoteConfig?.fetch(cacheExpiration)?.addOnCompleteListener(this) { task ->

            loginViewModel.isViewLoading(false)

            if (task.isSuccessful) {

                mFirebaseRemoteConfig?.activateFetched()

                val isForced =
                    mFirebaseRemoteConfig?.getBoolean("online_leave_force_update_required")

                val url =
                    mFirebaseRemoteConfig?.getString("online_leave_store_url")

                val currentVersion =
                    mFirebaseRemoteConfig?.getString("online_leave_current_version")

                if (!currentVersion.equals(BuildConfig.VERSION_NAME)) {
                    showDialogUpdateApp(isForced, url)
                }

                Log.d("isForced", "${isForced}")
                Log.d("url", "${url}")
                Log.d("currentVersion", "${currentVersion}")
            }
            //changing the textview and backgorund color
        }

        mFirebaseRemoteConfig?.fetch(cacheExpiration)?.addOnCanceledListener(this) {
            Log.d("mFirebaseRemoteConfig", "cancel")
            loginViewModel.isViewLoading(false)
        }

        mFirebaseRemoteConfig?.fetch(cacheExpiration)?.addOnFailureListener {
            Log.d("mFirebaseRemoteConfig", "failure")
            loginViewModel.isViewLoading(false)
        }
    }


    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            Injection.provideLoginViewModelFactory()
        ).get(LoginViewModel::class.java)

        loginViewModel.isViewLoading.observe(this, isLoading)
        loginViewModel.onMessageError.observe(this, onMessageError)
        loginViewModel.loginResponse.observe(this, loginResponseObserver)
    }

    private val loginResponseObserver = Observer<LoginResponse> {
        Log.d("loginResponseObserver", "${it}")
        Toast.makeText(
            this@LoginActivity,
            "${it.empname}",
            Toast.LENGTH_LONG
        ).show()
        sharedPref?.edit()?.putString("login_info", Gson().toJson(it))?.apply()

        openMainActivity()
    }

    private val isLoading = Observer<Boolean> {
        Log.d("isLoading", "${it}")

        if (it) {
            btnLogin.isClickable = false
            btnLogin.isEnabled = false
            btnLogin.text = "loading . . ."
        } else {
            btnLogin.isClickable = true
            btnLogin.isEnabled = true
            btnLogin.text = "Login"
        }
    }


    private val onMessageError = Observer<String> {
        Log.d("onMessageError", "${it}")
        Toast.makeText(
            this@LoginActivity,
            "Failed. Please check username & password",
            Toast.LENGTH_LONG
        ).show()
    }


    private fun openMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
    }

    private fun hasNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }

    fun showDialogUpdateApp(isForced: Boolean, link: String) {
        val mDialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_update_app, null)

        if (!isForced) {
            mDialogView.btnDialogNotNow.visibility = View.VISIBLE
        } else {
            mDialogView.btnDialogNotNow.visibility = View.GONE
        }
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setCancelable(!isForced)
            .setTitle("Update App")
        val mAlertDialog = mBuilder.show()

        mDialogView.btnDialogOK.setOnClickListener {

            mAlertDialog.dismiss()
            openNewTabWindow(link)

        }

        mDialogView.btnDialogNotNow.setOnClickListener {

            mAlertDialog.dismiss()

        }

    }

    fun openNewTabWindow(urls: String) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        startActivity(intents)
        finish()
    }
}
