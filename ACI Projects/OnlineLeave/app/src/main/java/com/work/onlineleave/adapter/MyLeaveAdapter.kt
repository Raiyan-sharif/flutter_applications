package com.work.onlineleave.adapter

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.work.onlineleave.R
import com.work.onlineleave.data.my_leave.Data
import com.work.onlineleave.data.my_leave.MyLeaveList


class MyLeaveAdapter(private var myLeaveList: MyLeaveList, private var context: Context) :
    RecyclerView.Adapter<MyLeaveAdapter.TestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyLeaveAdapter.TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_leave_adapter_item, parent, false)
        return TestViewHolder(view)
    }


    private var listener: OnClickListener? = null

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onEditButtonClick(item: Data)
        fun onDeleteButtonClick(item: Data)
    }

    override fun getItemCount(): Int {
        return myLeaveList.data.size
    }

    fun addData(listItems: MyLeaveList) {
        var size = this.myLeaveList.data.size
        Log.d("addData-before", "size ${myLeaveList.data.size}")
        this.myLeaveList.data.addAll(listItems.data)
        Log.d("addData-after", "size ${myLeaveList.data.size}")
        var sizeNew = this.myLeaveList.data.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun onBindViewHolder(holder: MyLeaveAdapter.TestViewHolder, position: Int) {
        setAnimation(holder.cardItem);

        holder.tvRefNumber.text = myLeaveList.data[position].leaverefno

        holder.tvLeaveType.text = myLeaveList.data[position].leavefor

        holder.tvAppDate.text = myLeaveList.data[position].leaveappdate

        holder.tvApDateFrom.text = myLeaveList.data[position].leaveappfrom

        holder.tvDateTo.text = myLeaveList.data[position].leaveappto

        holder.tvDays.text = myLeaveList.data[position].leaveappdays

        holder.tvIsLFA.text = myLeaveList.data[position].lfa

        holder.tvStatus.text = myLeaveList.data[position].status

        if (myLeaveList.data[position].status.equals("Pending")) {
            holder.lleditdelete.visibility = View.VISIBLE

            holder.tvEdit.setOnClickListener(View.OnClickListener {
                listener?.onEditButtonClick(myLeaveList.data[position])
            })
            holder.tvDelete.setOnClickListener(View.OnClickListener {
                listener?.onDeleteButtonClick(myLeaveList.data[position])
            })
        } else {
            holder.lleditdelete.visibility = View.GONE
        }

    }

    override fun onViewDetachedFromWindow(holder: TestViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()

    }

    class TestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvRefNumber: TextView = view.findViewById(R.id.tvRefNumber)
        val tvLeaveType: TextView = view.findViewById(R.id.tvLeaveType)
        val tvAppDate: TextView = view.findViewById(R.id.tvAppDate)
        val tvApDateFrom: TextView = view.findViewById(R.id.tvApDateFrom)
        val tvDateTo: TextView = view.findViewById(R.id.tvDateTo)
        val tvDays: TextView = view.findViewById(R.id.tvDays)
        val tvIsLFA: TextView = view.findViewById(R.id.tvIsLFA)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val lleditdelete: LinearLayout = view.findViewById(R.id.lleditdelete)
        val tvEdit: TextView = view.findViewById(R.id.tvEdit)
        val tvDelete: TextView = view.findViewById(R.id.tvDelete)


        val cardItem: CardView = view.findViewById(R.id.cardItem)
    }


    private fun setAnimation(viewToAnimate: View) {
        // If the bound view wasn't previously displayed on screen, it's animated
        val animation: Animation =
            AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        viewToAnimate.startAnimation(animation)
    }
}