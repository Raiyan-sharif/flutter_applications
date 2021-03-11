package com.aci.constructionequipment.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aci.constructionequipment.R
import com.aci.constructionequipment.networking.technician_list.Data


class TechnicianListAdapter(
    context: Context,
    private val items: ArrayList<Data>
) : BaseAdapter() {


    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view: View? = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.technician_list_item, parent, false)

            viewHolder = ViewHolder(
                view.findViewById(R.id.tvName),
                view.findViewById(R.id.tvMobile),
                view.findViewById(R.id.tvArea)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.tvName.text = items[position].name
        viewHolder.tvMobile.text = items[position].mobile
        viewHolder.tvArea.text = "Area: ${items[position].area_name}"

        return view!!
    }

    data class ViewHolder(
        val tvName: TextView,
        val tvMobile: TextView,
        val tvArea: TextView
    )
}