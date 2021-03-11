package com.work.onlineleave.view.govt_holiday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.work.onlineleave.R
import com.work.onlineleave.adapter.GovtHolidaysAdapter
import com.work.onlineleave.core.BaseFragment
import com.work.onlineleave.data.govt_holiday.GovtHoliday
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.di.Injection
import com.work.onlineleave.viewmodel.govtholiday.GovtHolidayViewModel
import kotlinx.android.synthetic.main.fragment_govt_holiday.*
import kotlinx.android.synthetic.main.fragment_my_leave.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class GovtHolidayFragment : BaseFragment() {

    private lateinit var adapter: GovtHolidaysAdapter

    private lateinit var govtHolidayViewModel: GovtHolidayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_govt_holiday, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }


    private fun setupViewModel() {
        govtHolidayViewModel = ViewModelProvider(
            this,
            Injection.provideGovtHolidayViewModelFactory()
        ).get(GovtHolidayViewModel::class.java)

        govtHolidayViewModel.isViewLoading.observe(getViewLifecycleOwner(), Observer {

        })

        govtHolidayViewModel.govtHolidayListResponse.observe(getViewLifecycleOwner(), Observer {
            if (!it.isNullOrEmpty()) {
                setupUI(it)
            }
        })

        govtHolidayViewModel.onMessageError.observe(getViewLifecycleOwner(), Observer {
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

        govtHolidayViewModel.govtHolidayList(loginResponse.worklocation)
    }

    private fun setupUI(govtHoliday: GovtHoliday) {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDateString = sdf.format(Date())

        val myFormat = "dd/MM/yyyy" // mention the format you need
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        val currentDate = simpleDateFormat.parse(currentDateString)

        adapter = GovtHolidaysAdapter(govtHoliday, currentDate, requireContext())
        recyclerGovtHoliday.layoutManager = LinearLayoutManager(requireContext())
        recyclerGovtHoliday.adapter = adapter
    }

}
