package com.work.onlineleave.view.my_leave

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import com.work.onlineleave.R
import com.work.onlineleave.adapter.MyLeaveAdapter
import com.work.onlineleave.adapter.PaginationScrollListener
import com.work.onlineleave.core.BaseFragment
import com.work.onlineleave.data.login.LoginResponse
import com.work.onlineleave.data.my_leave.Data
import com.work.onlineleave.data.my_leave.MyLeaveList
import com.work.onlineleave.di.Injection
import com.work.onlineleave.viewmodel.myleave.MyLeaveViewModel

import kotlinx.android.synthetic.main.dialog_leave_delete_confirmaion_popup.view.*
import kotlinx.android.synthetic.main.fragment_my_leave.*

/**
 * A simple [Fragment] subclass.
 */
class MyLeaveFragment : BaseFragment() {

    private lateinit var adapter: MyLeaveAdapter

    private lateinit var myLeaveViewModel: MyLeaveViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_leave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }


    private fun setupViewModel() {
        myLeaveViewModel = ViewModelProvider(
            this,
            Injection.provideMyLeaveViewModelFactory()
        ).get(MyLeaveViewModel::class.java)

        myLeaveViewModel.isViewLoading.observe(getViewLifecycleOwner(), Observer {

        })

        myLeaveViewModel.myLeaveListResponse.observe(getViewLifecycleOwner(), Observer {
            if (!it.data.isNullOrEmpty()) {
                setupUI(it)
            }
        })

        myLeaveViewModel.myLeaveListLoadMoreResponse.observe(getViewLifecycleOwner(), Observer {
            if (!it.data.isNullOrEmpty()) {
                isLoading = false
                adapter.addData(it)

            } else {
                isLoading = true
                isLastPage = true
            }
        })

        myLeaveViewModel.myLeaveDeleteResponse.observe(getViewLifecycleOwner(), Observer {

            if (it.success) {
                Toast.makeText(
                    requireContext(),
                    "Delete Successful",
                    Toast.LENGTH_LONG
                ).show()
            }
            getMyLeaveList()
        })

        myLeaveViewModel.onMessageError.observe(getViewLifecycleOwner(), Observer {
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
        empCode = loginResponse.empcode
        myLeaveViewModel.getMyLeaveList(loginResponse.empcode)
    }


    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var start = 0
    var empCode = ""

    private fun setupUI(myLeaveList: MyLeaveList) {

        adapter = MyLeaveAdapter(myLeaveList, requireContext())

        adapter.setListener(object : MyLeaveAdapter.OnClickListener {
            override fun onDeleteButtonClick(item: Data) {
                Log.d("onDeleteButtonClick", "Clicked")
                showDialogCreateLeaveSuccess(item)
            }

            override fun onEditButtonClick(item: Data) {
                Log.d("onEditButtonClick", "Clicked")
                var bundle = bundleOf("leaverefno" to item.leaverefno)
                view?.findNavController()
                    ?.navigate(R.id.action_myLeaveFragment_to_newLeaveFragment, bundle)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMyLeave.layoutManager = layoutManager
        recyclerViewMyLeave.adapter = adapter

        recyclerViewMyLeave.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call loadmore items to get more data
                start = start + 25
                myLeaveViewModel.getMyLeaveListLoadMore(empCode, start.toString())

            }
        })
    }

    fun showDialogCreateLeaveSuccess(item: Data) {
        val mDialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_leave_delete_confirmaion_popup, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)
            .setTitle("Delete Leave")
        val mAlertDialog = mBuilder.show()

        mDialogView.btnDialogCancel.setOnClickListener {

            mAlertDialog.dismiss()

        }

        mDialogView.btnDialogDelete.setOnClickListener {

            mAlertDialog.dismiss()
            myLeaveViewModel.myLeaveDelete(item.leaverefno)

        }

    }

}
