package com.aci.constructionequipment.core

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.aci.constructionequipment.R
import com.aci.constructionequipment.utils.ProgressDialog
import com.aci.constructionequipment.view.LoginActivity
import kotlinx.android.synthetic.main.layout_common_dialog.view.*


open class BaseFragment : Fragment() {

    protected var sharedPref: SharedPreferences? = null

    protected var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("Service_Autonomous", 0)
    }


    protected fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(requireContext())
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

    protected fun hasNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = requireContext().getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }

    fun showCommonDialog(title: String, msg: String) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_common_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle(title)
        val mAlertDialog = mBuilder.show()

        mDialogView.tvMessage.text = msg

        mDialogView.btnDialogOK.setOnClickListener {

            mAlertDialog.dismiss()

        }

    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun openLoginActivity() {

        sharedPref?.edit()?.putString("token", "")?.apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        requireActivity().finish()
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}