package com.aci.constructionequipment.core

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aci.constructionequipment.R
import com.aci.constructionequipment.utils.ProgressDialog

open class BaseActivity : AppCompatActivity() {

    protected var sharedPref: SharedPreferences? = null

    protected var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("Service_Autonomous", 0)


    }


    protected fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }

        if (!progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    protected fun hasNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }
}