package com.work.onlineleave.adapter

import android.content.Context
import android.graphics.Color
import android.text.format.DateUtils
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
import com.work.onlineleave.data.govt_holiday.GovtHoliday
import java.text.SimpleDateFormat
import java.util.*


class GovtHolidaysAdapter(
    private var items: GovtHoliday,
    private var currentDate: Date,
    private var context: Context
) :
    RecyclerView.Adapter<GovtHolidaysAdapter.TestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GovtHolidaysAdapter.TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.govt_holiday_adapter_item, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GovtHolidaysAdapter.TestViewHolder, position: Int) {
        setAnimation(holder.cardItem);

        holder.tvGovtHolidayName.text = items[position].remarks
        holder.tvGovtHolidayDate.text = items[position].hdate
        holder.tvappto.text = items[position].appto

        val myFormat = "dd/MM/yyyy" // mention the format you need
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)

        val date1 = simpleDateFormat.parse(items[position].hdate)


        if (date1.before(currentDate) || isToday(date1)) {
            holder.tvGovtHolidayName.setTextColor(Color.parseColor("#000000"))
            holder.tvGovtHolidayDate.setTextColor(Color.parseColor("#000000"))
            holder.tvappto.setTextColor(Color.parseColor("#000000"))
            holder.tvGovtHolidayName.alpha = 0.5f
            holder.tvGovtHolidayDate.alpha = 0.5f
            holder.tvappto.alpha = 0.5f
            holder.llItem.setBackgroundColor(Color.parseColor("#F8F8F8"));
        } else {
            if (isThisMonth(date1)) {
                holder.tvGovtHolidayName.setTextColor(Color.parseColor("#C71585"))
                holder.tvGovtHolidayDate.setTextColor(Color.parseColor("#C71585"))
                holder.tvappto.setTextColor(Color.parseColor("#C71585"))
            } else {
                holder.tvGovtHolidayName.setTextColor(Color.parseColor("#000000"))
                holder.tvGovtHolidayDate.setTextColor(Color.parseColor("#000000"))
                holder.tvappto.setTextColor(Color.parseColor("#000000"))
            }
            holder.tvGovtHolidayName.alpha = 1.0f
            holder.tvGovtHolidayDate.alpha = 1.0f
            holder.tvappto.alpha = 1.0f
            holder.llItem.setBackgroundColor(Color.parseColor("#E8E8E8"));
        }

    }

    fun isToday(date: Date?): Boolean {
        val today = Calendar.getInstance()
        val specifiedDate = Calendar.getInstance()
        specifiedDate.time = date
        return today[Calendar.DAY_OF_MONTH] === specifiedDate[Calendar.DAY_OF_MONTH] && today[Calendar.MONTH] === specifiedDate[Calendar.MONTH] && today[Calendar.YEAR] === specifiedDate[Calendar.YEAR]
    }

    fun isThisMonth(date: Date?): Boolean {
        val today = Calendar.getInstance()
        val specifiedDate = Calendar.getInstance()
        specifiedDate.time = date
        return today[Calendar.MONTH] === specifiedDate[Calendar.MONTH] && today[Calendar.YEAR] === specifiedDate[Calendar.YEAR]
    }

    override fun onViewDetachedFromWindow(holder: TestViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    class TestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvGovtHolidayName: TextView = view.findViewById(R.id.tvGovtHolidayName)
        val tvGovtHolidayDate: TextView = view.findViewById(R.id.tvGovtHolidayDate)
        val tvappto: TextView = view.findViewById(R.id.tvappto)
        val llItem: LinearLayout = view.findViewById(R.id.llItem)
        val cardItem: CardView = view.findViewById(R.id.cardItem)


    }


    private fun setAnimation(viewToAnimate: View) {
        // If the bound view wasn't previously displayed on screen, it's animated
        val animation: Animation =
            AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        viewToAnimate.startAnimation(animation)
    }

    fun getDateStart(): Date {
        var begining: Date
        run {
            val calendar: Calendar = getCalendarForNow()
            calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
            setTimeToBeginningOfDay(calendar)
            begining = calendar.time
        }

        return begining
    }

    fun getDateEnd(): Date {
        var end: Date

        run {
            val calendar: Calendar = getCalendarForNow()
            calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            setTimeToEndofDay(calendar)
            end = calendar.time
        }
        return end
    }

    private fun getCalendarForNow(): Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    private fun setTimeToBeginningOfDay(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
    }

    private fun setTimeToEndofDay(calendar: Calendar) {
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
    }
}