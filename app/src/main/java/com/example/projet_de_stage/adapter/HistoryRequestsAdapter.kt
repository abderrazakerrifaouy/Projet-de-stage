package com.example.projet_de_stage.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.R


class HistoryRequestsAdapter : RecyclerView.Adapter<HistoryRequestsAdapter.HistoryViewHolder>() {

    private var historyList = listOf<Appointment>()

    fun submitList(newList: List<Appointment>) {
        historyList = newList.filter {
            it.status != "pending" && it.status != "accepted"
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_request, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount() = historyList.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvCustomer: TextView = itemView.findViewById(R.id.tvCustomer)
        private val tvService: TextView = itemView.findViewById(R.id.tvService)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(appointment: Appointment) {
            tvDate.text = appointment.date.toString()
            tvTime.text = appointment.time
            tvCustomer.text = appointment.clientName
            tvService.text = appointment.service

            when (appointment.status) {
                "completed" -> {
                    tvStatus.text = "مكتمل"
                    tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Green
                }
                "canceled" -> {
                    tvStatus.text = "ملغي"
                    tvStatus.setTextColor(Color.parseColor("#F44336")) // Red
                }
                "rejected" -> {
                    tvStatus.text = "مرفوض"
                    tvStatus.setTextColor(Color.parseColor("#FF9800")) // Orange
                }
                else -> {
                    tvStatus.text = appointment.status
                    tvStatus.setTextColor(Color.parseColor("#9E9E9E")) // Gray
                }
            }
        }
    }
}