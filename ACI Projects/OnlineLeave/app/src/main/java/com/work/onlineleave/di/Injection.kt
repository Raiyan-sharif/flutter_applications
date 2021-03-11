package com.work.onlineleave.di

import androidx.lifecycle.ViewModelProvider
import com.work.onlineleave.viewmodel.dashboard.DashboardModelFactory
import com.work.onlineleave.viewmodel.dashboard.DashboardRepository
import com.work.onlineleave.viewmodel.govtholiday.GovtHolidayModelFactory
import com.work.onlineleave.viewmodel.govtholiday.GovtHolidayRepository
import com.work.onlineleave.viewmodel.login.LoginRepository
import com.work.onlineleave.viewmodel.login.LoginViewModelFactory
import com.work.onlineleave.viewmodel.my_approval.MyApprovalModelFactory
import com.work.onlineleave.viewmodel.my_approval.MyApprovalRepository
import com.work.onlineleave.viewmodel.myleave.MyLeaveModelFactory
import com.work.onlineleave.viewmodel.myleave.MyLeaveRepository
import com.work.onlineleave.viewmodel.newleave.NewLeaveRepository
import com.work.onlineleave.viewmodel.newleave.NewLeaveViewModelFactory

object Injection {

    // login

    private val loginRepository: LoginRepository =
        LoginRepository()
    private val loginViewModelFactory =
        LoginViewModelFactory(loginRepository)

    fun provideLoginViewModelFactory(): ViewModelProvider.Factory {
        return loginViewModelFactory
    }

    // new leave

    private val newLeaveRepository: NewLeaveRepository =
        NewLeaveRepository()
    private val newLeaveViewModelFactory =
        NewLeaveViewModelFactory(newLeaveRepository)

    fun provideNewLeaveViewModelFactory(): ViewModelProvider.Factory {
        return newLeaveViewModelFactory
    }


    // dashboard

    private val dashboardRepository: DashboardRepository =
        DashboardRepository()
    private val dashboardModelFactory =
        DashboardModelFactory(dashboardRepository)

    fun provideDashboardViewModelFactory(): ViewModelProvider.Factory {
        return dashboardModelFactory
    }

    // my leave

    private val myLeaveRepository: MyLeaveRepository =
        MyLeaveRepository()
    private val myLeaveModelFactory =
        MyLeaveModelFactory(myLeaveRepository)

    fun provideMyLeaveViewModelFactory(): ViewModelProvider.Factory {
        return myLeaveModelFactory
    }


    // govt holiday

    private val govtHolidayRepository: GovtHolidayRepository =
        GovtHolidayRepository()
    private val govtHolidayModelFactory =
        GovtHolidayModelFactory(govtHolidayRepository)

    fun provideGovtHolidayViewModelFactory(): ViewModelProvider.Factory {
        return govtHolidayModelFactory
    }


    // my approval

    private val myApprovalRepository: MyApprovalRepository =
        MyApprovalRepository()
    private val myApprovalModelFactory =
        MyApprovalModelFactory(myApprovalRepository)

    fun provideMyApprovalViewModelFactory(): ViewModelProvider.Factory {
        return myApprovalModelFactory
    }


}