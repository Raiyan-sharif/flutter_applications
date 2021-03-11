package com.aci.constructionequipment.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aci.constructionequipment.R
import com.aci.constructionequipment.core.BaseFragment
import com.aci.constructionequipment.networking.*
import com.aci.constructionequipment.networking.service_request_list.Data
import com.aci.constructionequipment.networking.service_request_list.ServiceRequest
import com.acibd.serviceautonomous.networking.all_data.AllData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard_engineer.*
import kotlinx.android.synthetic.main.layout_common_dialog.view.*
import kotlinx.android.synthetic.main.layout_common_dialog.view.btnDialogOK
import kotlinx.android.synthetic.main.layout_dialog_technician_update.*
import kotlinx.android.synthetic.main.layout_dialog_technician_update.view.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class DashboardTechnicianFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var allData: AllData
    var adapter: ServiceRequestTechnicianAdapter? = null
    var servieTypeID = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_technician, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val alldataString = sharedPref?.getString("alldata", "")

        if (!alldataString.isNullOrEmpty()) {
            allData = Gson().fromJson(alldataString, AllData::class.java)

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val token = sharedPref?.getString("token", "")

        if (token.isNullOrEmpty()) return

        getServiceRequestList(token)

    }

    private fun getServiceRequestList(token: String) {
        ApiClient.build()?.serviceRequest(
            token = token
        )?.enqueue(object : Callback<ServiceRequest> {

            override fun onResponse(
                call: Call<ServiceRequest>,
                response: Response<ServiceRequest>
            ) {
                hideProgressDialog()

                Log.d("alldata response", response.code().toString())

                if (response.isSuccessful) {

                    response.body().let {
                        if (it != null) {
                            initList(it)
                        }
                    }

                } else {
                    Log.d("alldata response failed", response.errorBody()?.string())
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ServiceRequest>, t: Throwable) {
                hideProgressDialog()
                t.printStackTrace()
                Log.d("alldata Failed", t.message)
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun initList(serviceRequest: ServiceRequest) {
        val alldataString = sharedPref?.getString("alldata", "")

        val allData = Gson().fromJson(alldataString, AllData::class.java)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ServiceRequestTechnicianAdapter(requireContext(), serviceRequest.data, allData)

        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        adapter?.notifyDataSetChanged()
        adapter?.update(serviceRequest.data, allData)

        adapter?.setListener(object : ServiceRequestTechnicianAdapter.OnItemClick {
            override fun onClick(item: Data) {
                showConfirmDialog(item)
            }
        })
    }


    fun showConfirmDialog(
        item: Data
    ) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_dialog_technician_update, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle("Update request")
        val mAlertDialog = mBuilder.show()

        mDialogView.tvUsername.text = "Customer: ${item.customer_name}"


        var serviceTypeList = ArrayList<String>()
        serviceTypeList.add("Select Service Type")
        for (type in allData.data.service_types) {
            serviceTypeList.add(type.name)
        }


        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                serviceTypeList
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        mDialogView.serviceType.onItemSelectedListener = this
        mDialogView.serviceType.adapter = adapter


        mDialogView.btnDialogOK.setOnClickListener {

            val rating = mDialogView.simpleRatingBar.rating.toInt()

            if (servieTypeID == -1){
                Toast.makeText(requireContext(), "Please Select Service Type", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (rating == 0) {
                Toast.makeText(requireContext(), "Please add customer rating", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            val chassisNo = mAlertDialog.chessis.editText?.text.toString()

            if (chassisNo.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please add Chassis No", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val body = TechnicianUpdate(
                item.id,
                item.customer_name,
                item.customer_mobile_no,
                item.customer_id.toInt(),
                item.brand_id.toInt(),
                item.brand_model_id.toInt(),
                item.district_id.toInt(),
                item.customer_remarks,
                chassisNo,
                mDialogView.remarks.editText?.text.toString(),
                rating,
                servieTypeID

            )

            Log.d("EngineerUpdate", Gson().toJson(body))

            UpdateAPI(body)

            mAlertDialog.dismiss()


        }

        mDialogView.btnDialogCancel.setOnClickListener {

            mAlertDialog.dismiss()

        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (position == 0) {
            servieTypeID = -1
        } else {
            servieTypeID = allData.data.service_types.get(position - 1).id
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun UpdateAPI(body: TechnicianUpdate) {

        val token = sharedPref?.getString("token", "")

        if (token.isNullOrEmpty()) return

        ApiClient.build()?.technicianUpdate(
            token,
            body
        )?.enqueue(object : Callback<CustomerRequestResponse> {

            override fun onResponse(
                call: Call<CustomerRequestResponse>,
                response: Response<CustomerRequestResponse>
            ) {
                hideProgressDialog()

                Log.d("login response", response.code().toString())

                if (response.isSuccessful) {

                    getServiceRequestList(token)

                    Toast.makeText(context, "Request Updated Successfully", Toast.LENGTH_LONG)
                        .show()

                } else {
                    Log.d("login response failed", response.errorBody()?.string())
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CustomerRequestResponse>, t: Throwable) {
                hideProgressDialog()
                t.printStackTrace()
                Log.d("login Failed", t.message)
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
    }

}
