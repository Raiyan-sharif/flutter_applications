package com.work.onlineleave.view.new_leave

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.work.onlineleave.R
import com.work.onlineleave.core.BaseFragment
import com.work.onlineleave.data.applyto.ApplyTo
import com.work.onlineleave.data.leavetype.LeaveType
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.new_leave.edit.NewLeaveEditInfo
import com.work.onlineleave.di.Injection
import com.work.onlineleave.viewmodel.newleave.NewLeaveViewModel
import kotlinx.android.synthetic.main.dialog_create_leave_success.view.*
import kotlinx.android.synthetic.main.fragment_new_leave.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class NewLeaveFragment : BaseFragment(), View.OnClickListener {

    private lateinit var newLeaveViewModel: NewLeaveViewModel

    private lateinit var leaveType: LeaveType
    private lateinit var applyTo: ApplyTo
    private var leaveTypeSelectedString = ""
    private var applyToSelectedString = ""
    private var leaverefno = ""
    private var leaveAppDate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_leave, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val arguments = arguments
        if (arguments != null && arguments.containsKey("leaverefno"))
            leaverefno = arguments?.getString("leaverefno").toString()


        setupViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initPersonalInfomation()

        initLeaveInformation()

        btnSubmit.setOnClickListener(this)


    }


    private fun setupViewModel() {
        newLeaveViewModel = ViewModelProvider(
            this,
            Injection.provideNewLeaveViewModelFactory()
        ).get(NewLeaveViewModel::class.java)


        newLeaveViewModel.isViewLoading.observe(
            getViewLifecycleOwner(),
            androidx.lifecycle.Observer {
                Log.d("isViewLoading", "${it}")

                if (it) {
                    btnSubmit.isClickable = false
                    btnSubmit.isEnabled = false
                    btnSubmit.setText("......")
                } else {
                    btnSubmit.isClickable = true
                    btnSubmit.isEnabled = true
                    btnSubmit.setText("Submit")
                }
            })
        newLeaveViewModel.onMessageError.observe(
            getViewLifecycleOwner(),
            androidx.lifecycle.Observer {
                Log.d("onMessageError", it)
                Toast.makeText(
                    requireContext(),
                    it,
                    Toast.LENGTH_LONG
                ).show()
            })
        newLeaveViewModel.applutoList.observe(getViewLifecycleOwner(), androidx.lifecycle.Observer {
            Log.d("ApplyTO", Gson().toJson(it))
            applyTo = it
            arrayAdapterApplyto = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_singlechoice
            )
            for (i in it.data) {
                arrayAdapterApplyto.add(i.authname)
            }

            if (::applyTo.isInitialized && ::leaveType.isInitialized) {
                if (!leaverefno.isNullOrEmpty()) {
                    Log.d("leaverefno 1", leaverefno)
                    newLeaveViewModel.getEditInfo(leaverefno)
                }
            }
        })

        newLeaveViewModel.leavetypeList.observe(
            getViewLifecycleOwner(),
            androidx.lifecycle.Observer {
                Log.d("leavetypeList", Gson().toJson(it))
                leaveType = it
                arrayAdapterLeaveType = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.select_dialog_singlechoice
                )
                for (i in it.data) {
                    arrayAdapterLeaveType.add(i.details)
                }

                if (::applyTo.isInitialized && ::leaveType.isInitialized) {
                    if (!leaverefno.isNullOrEmpty()) {
                        Log.d("leaverefno 2", leaverefno)
                        newLeaveViewModel.getEditInfo(leaverefno)
                    }
                }
            })

        newLeaveViewModel.newLeaveEditInfo.observe(
            getViewLifecycleOwner(),
            androidx.lifecycle.Observer {
                if (it.success) {

                    Handler().postDelayed({
                        addInfoEdit(it)
                    }, 300)

                }
            })

        newLeaveViewModel.createNewLeaveResponse.observe(
            getViewLifecycleOwner(),
            androidx.lifecycle.Observer {
                if (it.success) {
                    showDialogCreateLeaveSuccess(it.message.note)
                }
            })

        newLeaveViewModel.leavedays.observe(
            getViewLifecycleOwner(),
            androidx.lifecycle.Observer {
                etDateApplied.setText(it?.leaveappdays?.toString())
            })


        newLeaveViewModel.leavetype()

        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)

        newLeaveViewModel.applyto(loginResponse.empcode)


    }

    private fun addInfoEdit(newLeaveEditInfo: NewLeaveEditInfo) {
        Log.d("addInfoEdit", "Called")

        etLeaveReason.setText(newLeaveEditInfo.data.reason)
        applyToSelectedString = newLeaveEditInfo.data.authcode
        leaveTypeSelectedString = newLeaveEditInfo.data.leavefor
        if (::applyTo.isInitialized) {
            for (item in applyTo.data) {
                if (item.authcode.equals(newLeaveEditInfo.data.authcode)) {
                    etApplyTo.setText(item.authname)
                }
            }
        }

        etLeaveType.setText(newLeaveEditInfo.data.leavedetails)

        etLeaveFrom.text = newLeaveEditInfo.data.leaveappfrom
        etLeaveTo.text = newLeaveEditInfo.data.leaveappto


        etDateApplied.setText(newLeaveEditInfo.data.leaveappdays)

        if (newLeaveEditInfo.data.lfa.equals("1")) {
            cbLFA.isChecked = true
        }

        val startDateString = newLeaveEditInfo.data.leaveappdate
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdf2 = SimpleDateFormat("yyyy-MM-dd")

        val formatedDate = sdf2.format(sdf.parse(startDateString))


        tvApplicationDate.text = "Application Date: ${formatedDate}"
        leaveAppDate = formatedDate
    }


    private fun initPersonalInfomation() {
        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        tvApplicationDate.text = "Application Date: ${currentDate}"
        tvEmpID.text = "Employee ID: ${loginResponse.empcode}"
        tvEmpName.text = "Employee Name: ${loginResponse.empname}"
        tvEmpDepartment.text = "Department: ${loginResponse.deptname}"
        tvEmpDesignation.text = "Designation: ${loginResponse.desgname}"
        tvEmpEmail.text = "E-mail: ${loginResponse.emailid}"

    }

    private fun initLeaveInformation() {
        imgLeaveFrom.setOnClickListener(this)
        imgLeaveTo.setOnClickListener(this)
        btnHalfDay.setOnClickListener(this)
        imgApplyTo.setOnClickListener(this)
        imgLeaveType.setOnClickListener(this)
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

                if (et.id == R.id.etLeaveFrom){
                    etLeaveTo.setText("")
                    etDateApplied.setText("")

                }


                if (!etLeaveFrom.text.isNullOrEmpty() && !etLeaveTo.text.isNullOrEmpty()) {
//                    setLeaveDateDifference(etLeaveFrom.text.toString(), etLeaveTo.text.toString())
                    newLeaveViewModel.getLeaveAppDays(
                        etLeaveFrom.text.toString(),
                        etLeaveTo.text.toString(),
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

    override fun onClick(v: View?) {

        Log.d("onClick", "CLicked ")
        when (v!!.id) {
            R.id.imgLeaveFrom -> showDatePickerAndSetToPassedView(etLeaveFrom)
            R.id.imgLeaveTo ->
                if (etLeaveFrom.text.isNullOrEmpty())
                    Toast.makeText(
                        requireContext(),
                        "Please select Leave From First",
                        Toast.LENGTH_LONG
                    ).show()
                else
                    showDatePickerAndSetToPassedView(etLeaveTo)
            R.id.btnHalfDay ->
                setHalfDayLeave()
            R.id.imgApplyTo ->
                showApplyToDialog()
            R.id.imgLeaveType ->
                showLeaveTypeDialog()
            R.id.btnSubmit ->
                checkFromValidation()
        }
    }

    lateinit var arrayAdapterLeaveType: ArrayAdapter<String>

    private fun showLeaveTypeDialog() {

        if (arrayAdapterLeaveType == null)
            return

        val builderSingle =
            AlertDialog.Builder(requireContext())
        builderSingle.setTitle("Select leave type")

        builderSingle.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }

        builderSingle.setAdapter(
            arrayAdapterLeaveType
        ) { dialog, which ->
            val strName = arrayAdapterLeaveType.getItem(which)
            leaveTypeSelectedString = leaveType.data[which].leavefor
            if (!etLeaveFrom.text.isNullOrEmpty() && !etLeaveTo.text.isNullOrEmpty()) {
//                    setLeaveDateDifference(etLeaveFrom.text.toString(), etLeaveTo.text.toString())
                newLeaveViewModel.getLeaveAppDays(
                    etLeaveFrom.text.toString(),
                    etLeaveTo.text.toString(),
                    leaveTypeSelectedString
                )
            }
            Log.d("leavetype", strName)
            etLeaveType.setText(strName)
        }
        builderSingle.show()
    }

    lateinit var arrayAdapterApplyto: ArrayAdapter<String>

    private fun showApplyToDialog() {
        if (arrayAdapterApplyto == null)
            return

        val builderSingle =
            AlertDialog.Builder(requireContext())
        builderSingle.setTitle("Select apply to")

        builderSingle.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }

        builderSingle.setAdapter(
            arrayAdapterApplyto
        ) { dialog, which ->
            applyToSelectedString = applyTo.data[which].authcode
            val strName = arrayAdapterApplyto.getItem(which)
            Log.d("applyto", strName)
            etApplyTo.setText(strName)
        }
        builderSingle.show()
    }

    private fun setHalfDayLeave() {
        leaveDiference = -1

//        val sdf = SimpleDateFormat("dd/MM/yyyy")
//        val currentDate = sdf.format(Date())
//
//        etLeaveFrom.text = currentDate
//        etLeaveTo.text = currentDate
        etDateApplied.setText("0.5")
    }


    private fun setLeaveDateDifference(leaveFrom: String, leaveTo: String) {


        val myFormat = "yyyy-MM-dd" // mention the format you need
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)

        try {
            val date1 = simpleDateFormat.parse(leaveFrom)
            val date2 = simpleDateFormat.parse(leaveTo)
            printDifference(date1, date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }


    var leaveDiference: Long = -1

    fun printDifference(startDate: Date, endDate: Date) {

        val different = endDate.time - startDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        var elapsedDays = different / daysInMilli
        elapsedDays = elapsedDays

        if (elapsedDays == 0L) {
            elapsedDays = 1
        }

        if (elapsedDays < 10) {
            val holiday = saturdaysundaycount(startDate, endDate)
            elapsedDays = elapsedDays - holiday
        } else {
            elapsedDays = elapsedDays + 1
        }
        leaveDiference = elapsedDays

        etDateApplied.setText(elapsedDays.toString())


    }

    fun saturdaysundaycount(d1: Date?, d2: Date?): Int {
        val c1 = Calendar.getInstance()
        c1.time = d1
        val c2 = Calendar.getInstance()
        c2.time = d2
        var sundays = 0
        var saturday = 0
        while (!c1.after(c2)) {
            if (c1[Calendar.DAY_OF_WEEK] === Calendar.SATURDAY) {
                saturday++
            }
            if (c1[Calendar.DAY_OF_WEEK] === Calendar.FRIDAY) {
                sundays++
            }
            c1.add(Calendar.DATE, 1)
        }

        return saturday + sundays
    }

    fun showDialogCreateLeaveSuccess(msg: String) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_create_leave_success, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle("New Leave")
        val mAlertDialog = mBuilder.show()

        mDialogView.tvMessage.text = msg

        mDialogView.btnDialogOK.setOnClickListener {

            mAlertDialog.dismiss()
            findNavController().popBackStack()

        }

    }

    private fun checkFromValidation() {
        if (etLeaveReason.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please add leave reason",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (etApplyTo.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please add apply to",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (etLeaveType.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please add leave type",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (etLeaveFrom.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please add leave from",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (etLeaveTo.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please add leave to",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Toast.makeText(
            requireContext(),
            "Validation success",
            Toast.LENGTH_LONG
        ).show()

        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        var lfa  = "false"

        if (cbLFA.isChecked){
            lfa = "true"
        }


        if (!leaverefno.isNullOrEmpty()) {
            // request for update

            newLeaveViewModel.updateNewLeave(
                etLeaveFrom.text.toString(),
                etLeaveTo.text.toString(),
                loginResponse.empcode,
                loginResponse.empname,
                loginResponse.deptname,
                leaveAppDate,
                loginResponse.desgname,
                leaverefno,
                loginResponse.emailid,
                etLeaveReason.text.toString(),
                applyToSelectedString,
                leaveTypeSelectedString,
                etDateApplied.text.toString(),
                loginResponse.deptcode,
                loginResponse.worklocation,
                "",
                lfa
            )
        } else {
            // new create

            newLeaveViewModel.createNewLeave(
                etLeaveFrom.text.toString(),
                etLeaveTo.text.toString(),
                loginResponse.empcode,
                loginResponse.empname,
                loginResponse.deptname,
                currentDate,
                loginResponse.desgname,
                "",
                loginResponse.emailid,
                etLeaveReason.text.toString(),
                applyToSelectedString,
                leaveTypeSelectedString,
                etDateApplied.text.toString(),
                loginResponse.deptcode,
                loginResponse.worklocation,
                "",
                lfa
            )
        }


    }

}
