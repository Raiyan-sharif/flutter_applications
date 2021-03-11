package com.work.onlineleave.core

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    protected var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("ONLINE_LEAVE", 0)
    }

}