package com.aci.constructionequipment.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.aci.constructionequipment.R

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progressbar_layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun dismiss() {
        try {
            if (isShowing) super.dismiss()
        } catch (ignored: Exception) {
        }
    }

    override fun show() {
        try {
            if (!isShowing) super.show()
        } catch (ignored: Exception) {
        }
    }
}