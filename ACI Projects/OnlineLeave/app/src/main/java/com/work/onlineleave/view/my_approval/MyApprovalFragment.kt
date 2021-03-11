package com.work.onlineleave.view.my_approval

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.work.onlineleave.R
import com.work.onlineleave.adapter.MyApprovalAdapter
import com.work.onlineleave.core.BaseFragment
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.my_approval.Data
import com.work.onlineleave.data.my_approval.MyApproval
import com.work.onlineleave.di.Injection
import com.work.onlineleave.viewmodel.my_approval.MyApprovalViewModel
import kotlinx.android.synthetic.main.dialog_create_leave_success.view.*
import kotlinx.android.synthetic.main.dialog_create_leave_success.view.tvMessage
import kotlinx.android.synthetic.main.dialog_leave_delete_confirmaion_popup.view.*
import kotlinx.android.synthetic.main.fragment_my_approval.*

/**
 * A simple [Fragment] subclass.
 */
class MyApprovalFragment : BaseFragment() {

    private lateinit var adapter: MyApprovalAdapter

    private lateinit var myApprovalViewModel: MyApprovalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_approval, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }


    private fun setupUI(myApproval: MyApproval) {

        adapter = MyApprovalAdapter(myApproval, requireContext())

        adapter.setListener(object : MyApprovalAdapter.OnClickListener {

            override fun onApproveButtonClick(item: Data) {
                Log.d("MyApprovalAdapter", "onApproveButtonClick")
                var bundle = bundleOf("item" to Gson().toJson(item))
                view?.findNavController()
                    ?.navigate(R.id.action_myApprovalFragment_to_myApproveFragment, bundle)
            }

            override fun onRefuseButtonClick(item: Data) {
                Log.d("MyApprovalAdapter", "onRefuseButtonClick")
                postRefuseRequest(item)
            }

            override fun onCancelButtonClick(item: Data) {
                Log.d("MyApprovalAdapter", "onCancelButtonClick")
                postCanelRequest(item)
            }

        })

        recyclerViewMyApproval.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMyApproval.adapter = adapter
    }


    private fun postRefuseRequest(item: Data) {
        var bundle = bundleOf("item" to Gson().toJson(item))
        view?.findNavController()
            ?.navigate(R.id.action_myApprovalFragment_to_myRefuseFragment, bundle)
    }

    private fun postCanelRequest(item: Data) {
        showDialogApproveCancel(item)
    }

    fun showDialogApproveCancel(item: Data) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_leave_cancel_confirmaion_popup, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle("Cancel Leave")
        val mAlertDialog = mBuilder.show()

        mDialogView.btnDialogCancel.setOnClickListener {

            mAlertDialog.dismiss()

        }

        mDialogView.btnDialogDelete.setOnClickListener {

            mAlertDialog.dismiss()

            myApprovalViewModel.getMyApprovalCancelRequest(item.leaverefno)
        }

    }

    fun showDialogCancelResponse(msg: String) {
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
            getMyLeaveList()

        }

    }


    private fun setupViewModel() {
        myApprovalViewModel = ViewModelProvider(
            this,
            Injection.provideMyApprovalViewModelFactory()
        ).get(MyApprovalViewModel::class.java)

        myApprovalViewModel.isViewLoading.observe(getViewLifecycleOwner(), Observer {

        })

        myApprovalViewModel.myApprovalCancelResponse.observe(getViewLifecycleOwner(), Observer {
            showDialogCancelResponse(it.message.note)
        })

        myApprovalViewModel.myApprovalListResponse.observe(getViewLifecycleOwner(), Observer {
            if (!it.data.isNullOrEmpty()) {
                setupUI(it)
            }
        })


        myApprovalViewModel.commonResponseResponse.observe(getViewLifecycleOwner(), Observer {
            it?.message?.note?.let { it1 -> showDialogButtonsResponse(it1) }
        })


        myApprovalViewModel.onMessageError.observe(getViewLifecycleOwner(), Observer {
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        })

        getMyLeaveList()

    }

    fun getMyLeaveList() {
        val login_info = sharedPref?.getString("login_info", null)

        if (login_info.isNullOrEmpty())
            return

        val loginResponse: LoginResponse = Gson().fromJson(login_info, LoginResponse::class.java)

        myApprovalViewModel.getMyApprovalList(loginResponse.empcode)
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


        }

    }
}
