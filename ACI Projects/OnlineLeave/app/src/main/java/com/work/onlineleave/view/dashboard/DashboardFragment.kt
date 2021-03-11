package com.work.onlineleave.view.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.work.onlineleave.R
import com.work.onlineleave.core.BaseFragment
import com.work.onlineleave.data.leave_status.LeaveStatus
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.di.Injection
import com.work.onlineleave.viewmodel.dashboard.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_leave_status.view.*

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : BaseFragment(), View.OnClickListener {

    val TAG = "DashboardFragment"

    var navController: NavController? = null

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
        setupViewModel()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        navController = Navigation.findNavController(view)


        tvNewLeave.setOnClickListener(this)
        tvMyLeave.setOnClickListener(this)
        tvHolidies.setOnClickListener(this)
        tvMyApprovals.setOnClickListener(this)

        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)

        tvEmpName.text = loginResponse.empname
        tvEmpCode.text = "Emp. Code : ${loginResponse.empcode}"

        for (item in loginResponse.menus) {
            if (item.title.equals("My Approval")) {
                tvMyApprovals.visibility = View.VISIBLE
                break
            }
        }

    }


    private fun setupViewModel() {
        dashboardViewModel = ViewModelProvider(
            this,
            Injection.provideDashboardViewModelFactory()
        ).get(DashboardViewModel::class.java)

        dashboardViewModel.isViewLoading.observe(getViewLifecycleOwner(), Observer {

        })

        dashboardViewModel.leaveStatusResponse.observe(getViewLifecycleOwner(), Observer {
            showLeaveStatusData(it)
        })

        dashboardViewModel.onMessageError.observe(getViewLifecycleOwner(), Observer {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        })

        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)

        dashboardViewModel.leaveStatus(loginResponse.empcode) // hasib vai emp code 05581

    }


    private fun showLeaveStatusData(leaveStatus: LeaveStatus) {
        if (leaveStatus.data.isNullOrEmpty())
            return

        llAddItemStatus.removeAllViews()

        for (item in leaveStatus.data) {
            val itemLeaveStatus = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_leave_status, null)

            itemLeaveStatus.tvIndicator.text = item.INDICATOR
            itemLeaveStatus.tvPrivilegeLeave.text = item.PRIVILEGE_LEAVE
            itemLeaveStatus.tvSickLeave.text = item.SICK_LEAVE

            llAddItemStatus.addView(itemLeaveStatus)
        }


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvNewLeave -> navController!!.navigate(R.id.action_dashboardFragment_to_newLeaveFragment)
            R.id.tvMyLeave -> navController!!.navigate(R.id.action_dashboardFragment_to_myLeaveFragment)
            R.id.tvHolidies -> navController!!.navigate(R.id.action_dashboardFragment_to_govtHolidayFragment)
            R.id.tvMyApprovals -> navController!!.navigate(R.id.action_dashboardFragment_to_myApprovalFragment)
        }
    }

}
