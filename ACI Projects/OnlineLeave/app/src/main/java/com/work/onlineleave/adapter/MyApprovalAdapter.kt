package com.work.onlineleave.adapter

import android.content.Context
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
import com.work.onlineleave.data.my_approval.Data
import com.work.onlineleave.data.my_approval.MyApproval


class MyApprovalAdapter(private var myApproval: MyApproval, private var context: Context) :
    RecyclerView.Adapter<MyApprovalAdapter.TestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyApprovalAdapter.TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_approval_adapter_item, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myApproval.data.size
    }

    private var listener: OnClickListener? = null

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onApproveButtonClick(item: Data)
        fun onRefuseButtonClick(item: Data)
        fun onCancelButtonClick(item: Data)
    }

    override fun onBindViewHolder(holder: MyApprovalAdapter.TestViewHolder, position: Int) {
        setAnimation(holder.cardItem);

        holder.tvRefNumber.text = myApproval.data[position].leaverefno
        holder.tvAppName.text = myApproval.data[position].appname
        holder.tvAppDate.text = myApproval.data[position].leaveappdate
        holder.tvApDateFrom.text = myApproval.data[position].leaveappfrom
        holder.tvDateTo.text = myApproval.data[position].leaveappto
        holder.tvDays.text = myApproval.data[position].leaveappdays
        holder.tvStatus.text = myApproval.data[position].status

        if (myApproval.data[position].status.equals("Awaiting")) {
            holder.tvApprove.visibility = View.VISIBLE
            holder.tvRefuse.visibility = View.VISIBLE
            holder.tvCancel.visibility = View.GONE
            holder.llButtons.visibility = View.VISIBLE
        } else if (myApproval.data[position].status.equals("Refused")) {
            holder.llButtons.visibility = View.GONE
        } else if (myApproval.data[position].status.equals("Granted")) {
            holder.tvApprove.visibility = View.GONE
            holder.tvRefuse.visibility = View.GONE
            holder.tvCancel.visibility = View.VISIBLE
            holder.llButtons.visibility = View.VISIBLE
        } else if (myApproval.data[position].status.equals("Posted")) {
            holder.llButtons.visibility = View.GONE
        }


        holder.tvApprove.setOnClickListener {
            listener?.onApproveButtonClick(myApproval.data[position])
        }

        holder.tvRefuse.setOnClickListener {
            listener?.onRefuseButtonClick(myApproval.data[position])
        }

        holder.tvCancel.setOnClickListener {
            listener?.onCancelButtonClick(myApproval.data[position])
        }
    }

    override fun onViewDetachedFromWindow(holder: TestViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()

    }

    class TestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvRefNumber: TextView = view.findViewById(R.id.tvRefNumber)
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvAppDate: TextView = view.findViewById(R.id.tvAppDate)
        val tvApDateFrom: TextView = view.findViewById(R.id.tvApDateFrom)
        val tvDateTo: TextView = view.findViewById(R.id.tvDateTo)
        val tvDays: TextView = view.findViewById(R.id.tvDays)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)

        val tvApprove: TextView = view.findViewById(R.id.tvApprove)
        val tvRefuse: TextView = view.findViewById(R.id.tvRefuse)
        val tvCancel: TextView = view.findViewById(R.id.tvCancel)

        val cardItem: CardView = view.findViewById(R.id.cardItem)
        val llButtons: LinearLayout = view.findViewById(R.id.llButtons)
    }


    private fun setAnimation(viewToAnimate: View) {
        // If the bound view wasn't previously displayed on screen, it's animated
        val animation: Animation =
            AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        viewToAnimate.startAnimation(animation)
    }
}