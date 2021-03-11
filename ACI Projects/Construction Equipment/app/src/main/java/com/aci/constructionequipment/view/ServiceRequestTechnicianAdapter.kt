package com.aci.constructionequipment.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aci.constructionequipment.R
import com.aci.constructionequipment.networking.service_request_list.Data
import com.aci.constructionequipment.networking.service_request_list.ServiceRequest
import com.acibd.serviceautonomous.networking.all_data.AllData


class ServiceRequestTechnicianAdapter(
    context: Context,
    private var items: ArrayList<Data>,
    private var allData: AllData
) : RecyclerView.Adapter<ServiceRequestTechnicianAdapter.MViewHolder>() {

    private var listener: OnItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_list_item, parent, false)
        return ServiceRequestTechnicianAdapter.MViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size ?: 0
    }

    fun update(data: ArrayList<Data>, _allData: AllData) {
        items = data
        allData = _allData
        notifyDataSetChanged()
    }

    fun setListener(listener: OnItemClick) {
        this.listener = listener
    }


    override fun onBindViewHolder(holder: MViewHolder, position: Int) {


        if (items.isNullOrEmpty())
            return

        val item = items.get(position)

        if (!item.customer_name.isNullOrEmpty()) {
            holder.tvCustomerName.text = item.customer_name
        }

        if (!item.customer_mobile_no.isNullOrEmpty()) {
            holder.tvCustomerPhone.text = item.customer_mobile_no
        }

        for (brand in allData.data.brands) {
            if (brand.id == item?.brand_id?.toInt()) {
                holder.tvBrand.text = brand.name
                break
            }
        }

        for (model in allData.data.brand_models) {
            if (model.id == item?.brand_model_id?.toInt()) {
                holder.tvModel.text = model.name
                break
            }
        }

        for (district in allData.data.districts) {
            if (district.id == item?.district_id?.toInt()) {
                holder.tvDistrict.text = district.name
                break
            }
        }

        holder.btnAssign.setText("Update")
        holder.btnAssign.visibility = View.VISIBLE

        holder.btnAssign.setOnClickListener {
            listener?.onClick(item)
        }


    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        val tvId: TextView = view.findViewById(R.id.tvId)
        val tvCustomerName: TextView = view.findViewById(R.id.tvCustomerName)
        val tvCustomerPhone: TextView = view.findViewById(R.id.tvCustomerPhone)
        val tvBrand: TextView = view.findViewById(R.id.tvBrand)
        val tvModel: TextView = view.findViewById(R.id.tvModel)
        val tvDistrict: TextView = view.findViewById(R.id.tvDistrict)
        val btnAssign: Button = view.findViewById(R.id.btnAssign)
        val llClick: LinearLayout = view.findViewById(R.id.llClick)
//        val tvStock: TextView = view.findViewById(R.id.tvStock)

    }

    interface OnItemClick {
        fun onClick(item: Data)
    }
}