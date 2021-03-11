package com.aci.constructionequipment.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.aci.constructionequipment.BuildConfig
import com.aci.constructionequipment.R
import com.aci.constructionequipment.core.BaseActivity
import com.aci.constructionequipment.core.NavActivity
import com.aci.constructionequipment.networking.ApiClient
import com.aci.constructionequipment.networking.ApkVersion
import kotlinx.android.synthetic.main.dialog_update_app.view.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({

            openLoginActivity()

        }, 1000)
    }


    private fun checkTokenAndOpenNext() {
        val token = sharedPref?.getString("token", "")

        if (token.isNullOrEmpty()) {
            openLoginActivity()
        }else {
            openDashboard()
        }
    }

    fun apkVersion() {

        showProgressDialog()
        ApiClient.build()?.apkVersion("http://apps.eacibd.com/application-version-check/32")
            ?.enqueue(object : Callback<ApkVersion> {
                override fun onResponse(
                    call: Call<ApkVersion>,
                    response: Response<ApkVersion>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {

                        response.body().let {
                            if (BuildConfig.VERSION_CODE < it!!.version_code) {
                                showDialogUpdateApp(it.download_page)
                            } else {
                                checkTokenAndOpenNext()
                            }
                        }


                    } else {
                        Log.d("postFirebaseToken", "OnFailure - " + response.errorBody()?.string())
                        checkTokenAndOpenNext()
                    }
                }

                override fun onFailure(call: Call<ApkVersion>, t: Throwable) {
                    hideProgressDialog()
                    checkTokenAndOpenNext()
                    t.printStackTrace()
                    Log.d("postFirebaseToken", "OnFailure - " + t.message)

                }
            })
    }



    private fun openLoginActivity() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    private fun openDashboard() {
        val intent = Intent(this@SplashActivity, NavActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    fun showDialogUpdateApp(url: String) {
        val mDialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_update_app, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle("Update App")
        val mAlertDialog = mBuilder.show()

        mDialogView.tvMessage.text =
            "You are using an old version(v${BuildConfig.VERSION_NAME}) of this app. Please update this app to continue using."

        mDialogView.btnDialogOK.setOnClickListener {

            mAlertDialog.dismiss()
            openNewTabWindow(url)
            finishAndRemoveTask()

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
