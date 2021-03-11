package com.aci.constructionequipment.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import com.aci.constructionequipment.R
import com.aci.constructionequipment.core.BaseFragment
import com.aci.constructionequipment.networking.*
import com.acibd.serviceautonomous.networking.all_data.AllData
import com.google.gson.Gson
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.username
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : BaseFragment(), View.OnClickListener {

    private lateinit var allData: AllData

    var brand_id = -1
    var model_id = -1
    var district_id = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
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
        btnSelectBrand.setOnClickListener(this)
        btnSelectModel.setOnClickListener(this)
        btnSelectDistrict.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSelectBrand -> {
                showBrandDialog()
            }

            R.id.btnSelectModel -> {
                if (brand_id != -1) {
                    showModelDialog()
                } else {
                    Toast.makeText(context, "Please Select Brand First", Toast.LENGTH_LONG).show()
                }
            }

            R.id.btnSelectDistrict -> {
                showDistrictDialog()
            }

            R.id.btnSubmit -> {
                checkValidation()
            }

        }
    }

    fun showBrandDialog() {

        if (!this::allData.isInitialized) {
            Log.d("alldata", "null")
            return
        }


        val itemListString = ArrayList<String>()

        for (item in allData.data.brands) {
            itemListString.add(item.name)
        }


        val adapter = SimpleAdapter(requireContext(), itemListString)

        val dialog = DialogPlus.newDialog(requireContext())
            .setAdapter(adapter)
            .setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    dialog: DialogPlus?,
                    item: Any?,
                    view: View?,
                    position: Int
                ) {
                    tvBrand.text = itemListString.get(position)
                    brand_id = allData.data.brands.get(position).id
                    model_id = -1
                    tvModel.text = ""
                    dialog?.dismiss()
                }

            })
            .setExpanded(false)
            .setPadding(20, 50, 20, 50)
            .setGravity(Gravity.CENTER)
            // This will enable the expand feature, (similar to android L share dialog)
            .create()
        dialog.show()
    }

    fun showModelDialog() {

        if (!this::allData.isInitialized) {
            Log.d("alldata", "null")
            return
        }


        val itemListString = ArrayList<String>()
        val itemListID = ArrayList<Int>()

        for (item in allData.data.brand_models) {
            if (item.brand_id.equals(brand_id.toString())) {
                itemListString.add(item.name)
                itemListID.add(item.id)
            }

        }

        if (itemListString.isEmpty()) {
            Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            return
        }


        val adapter = SimpleAdapter(requireContext(), itemListString)

        val dialog = DialogPlus.newDialog(requireContext())
            .setAdapter(adapter)
            .setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    dialog: DialogPlus?,
                    item: Any?,
                    view: View?,
                    position: Int
                ) {
                    tvModel.text = itemListString.get(position)
                    model_id = itemListID.get(position)
                    dialog?.dismiss()
                }

            })
            .setExpanded(false)
            .setPadding(20, 50, 20, 50)
            .setGravity(Gravity.CENTER)
            // This will enable the expand feature, (similar to android L share dialog)
            .create()
        dialog.show()
    }

    fun showDistrictDialog() {

        if (!this::allData.isInitialized) {
            Log.d("alldata", "null")
            return
        }


        val itemListString = ArrayList<String>()

        for (item in allData.data.districts) {
            itemListString.add(item.name)
        }


        val adapter = SimpleAdapter(requireContext(), itemListString)

        val dialog = DialogPlus.newDialog(requireContext())
            .setAdapter(adapter)
            .setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    dialog: DialogPlus?,
                    item: Any?,
                    view: View?,
                    position: Int
                ) {
                    tvDistrict.text = itemListString.get(position)
                    district_id = allData.data.districts.get(position).id

                    dialog?.dismiss()
                }

            })
            .setExpanded(false)
            .setPadding(20, 50, 20, 50)
            .setGravity(Gravity.CENTER)
            // This will enable the expand feature, (similar to android L share dialog)
            .create()
        dialog.show()
    }

    fun checkValidation() {
        if(brand_id == -1){
            Toast.makeText(context, "Please select Brand", Toast.LENGTH_LONG).show()
            return
        }
        if (model_id == -1){
            Toast.makeText(context, "Please select Model", Toast.LENGTH_LONG).show()
            return
        }
        if (district_id == -1){
            Toast.makeText(context, "Please select District", Toast.LENGTH_LONG).show()
            return
        }

        if (username.editText?.text.toString().isEmpty()){
            Toast.makeText(context, "Please insert Name", Toast.LENGTH_LONG).show()
            return
        }

        if (mobile.editText?.text.toString().isEmpty()){
            Toast.makeText(context, "Please insert Mobile", Toast.LENGTH_LONG).show()
            return
        }

        callAPIRequestCreate()
    }

    private fun callAPIRequestCreate() {

        val token = sharedPref?.getString("token", "")

        if (token.isNullOrEmpty()) return

        showProgressDialog()

        val body = CustomerRequestCreateBody(brand_id, model_id, remarks.editText?.text.toString(),
            district_id, mobile.editText?.text.toString(), username.editText?.text.toString())

        ApiClient.build()?.customerRequest(
            token,
            body
        )?.enqueue(object : Callback<CustomerRequestResponse> {

            override fun onResponse(call: Call<CustomerRequestResponse>, response: Response<CustomerRequestResponse>) {


                Log.d("login response", response.code().toString())

                if (response.isSuccessful) {
                    hideProgressDialog()
                    brand_id = -1
                    model_id = -1
                    district_id = -1
                    tvDistrict.text = ""
                    tvModel.text = ""
                    tvBrand.text = ""
                    username.editText?.setText("")
                    mobile.editText?.setText("")
                    remarks.editText?.setText("")

                    Toast.makeText(context, "Request Created Successfully", Toast.LENGTH_LONG).show()

                } else {
                    hideProgressDialog()
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
