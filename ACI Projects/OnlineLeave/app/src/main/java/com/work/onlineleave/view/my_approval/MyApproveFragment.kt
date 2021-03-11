package com.work.onlineleave.view.my_approval

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.work.onlineleave.R
import com.work.onlineleave.core.BaseFragment
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.my_approval.Data
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.di.Injection
import com.work.onlineleave.viewmodel.my_approval.MyApprovalViewModel
import kotlinx.android.synthetic.main.dialog_create_leave_success.view.*
import kotlinx.android.synthetic.main.dialog_create_leave_success.view.tvMessage
import kotlinx.android.synthetic.main.fragment_my_approve.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MyApproveFragment : BaseFragment() {

    private lateinit var myApprovalViewModel: MyApprovalViewModel

    private lateinit var item: Data
    private lateinit var newLeaveEditInfo: NewLeaveEditInfo

    private var leaveTypeSelectedString = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_approve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemString = arguments?.getString("item").toString()

        item = Gson().fromJson(itemString, Data::class.java)

        setupViewModel()
    }


    private fun setupViewModel() {
        myApprovalViewModel = ViewModelProvider(
            this,
            Injection.provideMyApprovalViewModelFactory()
        ).get(MyApprovalViewModel::class.java)

        myApprovalViewModel.isViewLoading.observe(getViewLifecycleOwner(), Observer {

        })

        myApprovalViewModel.leavedays.observe(getViewLifecycleOwner(), Observer {
            etDateAppliedGranted.setText(it.leaveappdays.toString())
        })


        myApprovalViewModel.commonResponseResponse.observe(getViewLifecycleOwner(), Observer {
            it?.message?.note?.let { it1 -> showDialogButtonsResponse(it1) }
        })


        myApprovalViewModel.leaveInfo.observe(getViewLifecycleOwner(), Observer {
            Log.d("leaveInfo", Gson().toJson(it))
            newLeaveEditInfo = it
            setupUI(it)
            myApprovalViewModel.applyto(item.appcode)
        })

        myApprovalViewModel.applutoList.observe(getViewLifecycleOwner(), Observer {
            Log.d("applutoList", Gson().toJson(it))


            for (item in it.data) {
                if (item.authcode.equals(newLeaveEditInfo.data.authcode)) {
                    etApplyTo.setText(item.authname)
                    break
                }
            }

        })


        myApprovalViewModel.onMessageError.observe(getViewLifecycleOwner(), Observer {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        })

        myApprovalViewModel.getEditInfo(item.leaverefno)
    }

    fun setupUI(newLeaveEditInfo: NewLeaveEditInfo) {


        leaveTypeSelectedString = newLeaveEditInfo.data.leavefor

        btnApprove.setOnClickListener(View.OnClickListener {
            postApprovalRequest()
        })

        if (newLeaveEditInfo.data.lfa.equals("1")) {
            cbLFA.isChecked = true
        }

        etLeaveReason.setText(newLeaveEditInfo.data.reason)
        etLeaveFrom.text = newLeaveEditInfo.data.leaveappfrom
        etLeaveTo.text = newLeaveEditInfo.data.leaveappto
        etDateApplied.setText(newLeaveEditInfo.data.leaveappdays)
        etLeaveType.setText(newLeaveEditInfo.data.leavedetails)


        etLeaveApproveFrom.text = newLeaveEditInfo.data.leaveappfrom
        etLeaveApproveTo.text = newLeaveEditInfo.data.leaveappto
        etDateAppliedGranted.setText(newLeaveEditInfo.data.leaveappdays)

        imgLeaveApproveFrom.setOnClickListener(View.OnClickListener {
            showDatePickerAndSetToPassedView(etLeaveApproveFrom)
        })

        imgLeaveApproveTo.setOnClickListener(View.OnClickListener {
            showDatePickerAndSetToPassedView(etLeaveApproveTo)
        })
    }


    private fun showDatePickerAndSetToPassedView(et: TextView) {
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                et.setText(sdf.format(cal.time))

                if (et.id == R.id.etLeaveApproveFrom) {
                    etLeaveApproveTo.setText("")
                    etDateAppliedGranted.setText("")

                }


                if (!etLeaveApproveFrom.text.isNullOrEmpty() && !etLeaveApproveTo.text.isNullOrEmpty()) {
//                    setLeaveDateDifference(etLeaveFrom.text.toString(), etLeaveTo.text.toString())
                    myApprovalViewModel.getLeaveAppDays(
                        etLeaveApproveFrom.text.toString(),
                        etLeaveApproveTo.text.toString(),
                        leaveTypeSelectedString
                    )
                }

            }

        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )


        if (et.id == R.id.etLeaveTo) {
            if (etLeaveFrom.text.isNullOrEmpty())
                return
            val myFormat = "yyyy-MM-dd" // mention the format you need
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)

            try {
                val leaveFromFormat = simpleDateFormat.parse(etLeaveFrom.text.toString())
                datePickerDialog.datePicker.minDate = leaveFromFormat.time

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            datePickerDialog.show()
        } else {
            datePickerDialog.show()
        }


    }


    private fun postApprovalRequest() {

        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)



        myApprovalViewModel.myApprovalApprove(
            etLeaveApproveFrom.text.toString(),
            etLeaveApproveTo.text.toString(),
            item.appcode,
            item.appname,
            loginResponse.deptname,
            item.leaveappdate,
            loginResponse.desgname,
            item.leaverefno,
            loginResponse.emailid,
            newLeaveEditInfo.data.reason,
            loginResponse.empname,
            item.leavefor,
            item.leaveappfrom,
            item.leaveappto,
            item.leaveappdays,
            setDateFormate(etLeaveApproveFrom.text.toString()),
            setDateFormate(etLeaveApproveTo.text.toString()),
            etDateAppliedGranted.text.toString(),
            "",
            newLeaveEditInfo.data.authcode
        )
    }


    private fun setDateFormate(date: String): String {


        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val sdf2 = SimpleDateFormat("dd/MM/yyyy")

        val formatedDate = sdf2.format(sdf.parse(date))

        Log.d("setDateFormate", formatedDate)

        return formatedDate
    }


    fun showDialogButtonsResponse(msg: String) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_create_leave_success, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
        val mAlertDialog = mBuilder.show()

        mDialogView.tvMessage.text = msg

        mDialogView.btnDialogOK.setOnClickListener {

            mAlertDialog.dismiss()
            findNavController().popBackStack()


        }

    }




}
