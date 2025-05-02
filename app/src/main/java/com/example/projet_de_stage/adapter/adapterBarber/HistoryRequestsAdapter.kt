package com.example.projet_de_stage.adapter.adapterBarber

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.viewModel.BarberViewModel

/**
 * Adapter to display a list of past appointments (history) for a barber.
 */
class HistoryRequestsAdapter : RecyclerView.Adapter<HistoryRequestsAdapter.HistoryViewHolder>() {

    private var historyList = listOf<Appointment>()
    private val viewModel = BarberViewModel()

    /**
     * Updates the list of appointments and refreshes the adapter.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Appointment>) {
        historyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_request, parent, false)
        return HistoryViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    /**
     * ViewHolder that binds an Appointment to the item view.
     */
    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvCustomer: TextView = itemView.findViewById(R.id.tvCustomer)
        private val tvService: TextView = itemView.findViewById(R.id.tvService)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(appointment: Appointment) {
            // Set static appointment data
            tvDate.text = appointment.date
            tvTime.text = appointment.time
            tvService.text = appointment.service

            // Observe customer name from ViewModel
            viewModel.loadCustomerById(appointment.clientId)
            viewModel.customer.observe(itemView.context as FragmentActivity) { customer ->
                tvCustomer.text = customer?.name ?: "Unknown"
            }

            // Display appointment status with corresponding color
            when (appointment.status) {
                "pending" -> {
                    tvStatus.text = "Pending"
                    tvStatus.setTextColor("#FF9800".toColorInt()) // Orange
                }
                "completed" -> {
                    tvStatus.text = "Completed"
                    tvStatus.setTextColor("#4CAF50".toColorInt()) // Green
                }
                "canceled" -> {
                    tvStatus.text = "Canceled"
                    tvStatus.setTextColor("#F44336".toColorInt()) // Red
                }
                "rejected" -> {
                    tvStatus.text = "Rejected"
                    tvStatus.setTextColor("#FF9800".toColorInt()) // Orange
                }
                else -> {
                    tvStatus.text = "Accepted"
                    tvStatus.setTextColor("#9E9E9E".toColorInt()) // Gray
                }
            }
        }
    }
}
