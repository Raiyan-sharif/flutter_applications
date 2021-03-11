package com.aci.constructionequipment.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aci.constructionequipment.R
import com.aci.constructionequipment.core.BaseFragment
import com.aci.constructionequipment.networking.ApiClient
import com.aci.constructionequipment.networking.CustomerRequestResponse
import com.aci.constructionequipment.networking.EnginnerUpdate
import com.aci.constructionequipment.networking.service_request_list.Data
import com.aci.constructionequipment.networking.service_request_list.ServiceRequest
import com.aci.constructionequipment.networking.technician_list.TechnicianList
import com.acibd.serviceautonomous.networking.all_data.AllData
import com.google.gson.Gson
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard_engineer.*
import kotlinx.android.synthetic.main.layout_common_dialog.view.*
import kotlinx.android.synthetic.main.layout_common_dialog.view.btnDialogOK
import kotlinx.android.synthetic.main.layout_dialog_engineer_update.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardEngineerFragment : BaseFragment() {

    private lateinit var allData: AllData
    private lateinit var technicianList: TechnicianList
    var adapter: ServiceRequestAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_engineer, container, false)
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

        getServiceRequestList(token, true)

    }


    private fun getServiceRequestList(token: String, shouldLoadTechnician: Boolean) {
        ApiClient.build()?.serviceRequest(
            token = token
        )?.enqueue(object : Callback<ServiceRequest> {

            override fun onResponse(
                call: Call<ServiceRequest>,
                response: Response<ServiceRequest>
            ) {
                if (shouldLoadTechnician) {
                    getTechnicianList(token)
                } else {
                    hideProgressDialog()
                }

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

    private fun getTechnicianList(token: String) {
        ApiClient.build()?.technicianList(
            token = token
        )?.enqueue(object : Callback<TechnicianList> {

            override fun onResponse(
                call: Call<TechnicianList>,
                response: Response<TechnicianList>
            ) {
                hideProgressDialog()

                Log.d("alldata response", response.code().toString())

                if (response.isSuccessful) {

                    response.body().let {
                        if (it != null) {
                            technicianList = it
                        }
                    }

                } else {
                    Log.d("alldata response failed", response.errorBody()?.string())
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<TechnicianList>, t: Throwable) {
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
        adapter = ServiceRequestAdapter(requireContext(), serviceRequest.data, allData)

        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        adapter?.notifyDataSetChanged()
        adapter?.update(serviceRequest.data, allData)

        adapter?.setListener(object : ServiceRequestAdapter.OnItemClick {
            override fun onClick(item: Data) {
                showTechnicianList(item)
            }
        })
    }


    fun showTechnicianList(itemRequest: Data) {

        if (!this::technicianList.isInitialized) {
            Log.d("technicianList", "null")
            return
        }

        val adapter = TechnicianListAdapter(requireContext(), technicianList.data)

        val dialog = DialogPlus.newDialog(requireContext())
            .setAdapter(adapter)
            .setHeader(R.layout.dialog_header)
            .setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    dialog: DialogPlus?,
                    item: Any?,
                    view: View?,
                    position: Int
                ) {
                    dialog?.dismiss()
                    showConfirmDialog(itemRequest, technician = technicianList.data.get(position))
                }

            })
            .setExpanded(false)
            .setPadding(20, 50, 20, 50)
            .setGravity(Gravity.CENTER)
            // This will enable the expand feature, (similar to android L share dialog)
            .create()
        dialog.show()
    }

    fun showConfirmDialog(
        item: Data,
        technician: com.aci.constructionequipment.networking.technician_list.Data
    ) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_dialog_engineer_update, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle("Update request")
        val mAlertDialog = mBuilder.show()

        mDialogView.tvUsername.text = "Customer: ${item.customer_name}"
        mDialogView.tvTechnicianname.text = "Technician: ${technician.name}"

        mDialogView.btnDialogOK.setOnClickListener {


            val body = EnginnerUpdate(
                item.id,
                item.customer_name,
                item.customer_mobile_no,
                item.customer_id.toInt(),
                item.brand_id.toInt(),
                item.brand_model_id.toInt(),
                item.district_id.toInt(),
                item.customer_remarks,
                technician.id,
                mDialogView.remarks.editText?.text.toString()
            )

            Log.d("EngineerUpdate", Gson().toJson(body))

            mAlertDialog.dismiss()

            engineerUpdateAPI(body)
        }

        mDialogView.btnDialogCancel.setOnClickListener {

            mAlertDialog.dismiss()

        }

    }

    private fun engineerUpdateAPI(body: EnginnerUpdate) {

        val token = sharedPref?.getString("token", "")

        if (token.isNullOrEmpty()) return

        ApiClient.build()?.engineerUpdate(
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

                    getServiceRequestList(token, false)

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
