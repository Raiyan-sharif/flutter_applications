package com.aci.constructionequipment.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aci.constructionequipment.R


class SimpleAdapter (
    context: Context,
    private val items: ArrayList<String>
) : BaseAdapter(){


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
            view = layoutInflater.inflate(R.layout.simple_list_item, parent, false)

            viewHolder = ViewHolder(
                view.findViewById(R.id.text_view)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.textView.text = items[position]

        return view!!
    }

    data class ViewHolder(val textView: TextView)
}