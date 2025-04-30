package com.example.projet_de_stage.adapter.adapterClient

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.viewModel.ClientViewModel
import java.time.format.DateTimeFormatter

class AppointmentAdapter(
    private val appointments: MutableList<Appointment>,
    private val onItemClick: (Appointment) -> Unit = {}
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val  viewModel = ClientViewModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        // Make sure you're inflating the ITEM layout, not fragment layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_request_client, parent, false) // Changed to item layout
        return AppointmentViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
        holder.itemView.setOnClickListener { onItemClick(appointment) }
    }

    override fun getItemCount(): Int = appointments.size

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize all views once in the ViewHolder
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvBarberchop: TextView = itemView.findViewById(R.id.tvBarberchop)
        private val tvService: TextView = itemView.findViewById(R.id.tvService)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvBarberName: TextView = itemView.findViewById(R.id.tvBarberName)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(appointment: Appointment) {
            viewModel.getShopById(appointment.shopId)
            viewModel.getBarberById(appointment.barberId)
            tvDate.text = appointment.date.format(dateFormatter)
            tvTime.text = appointment.time
            tvBarberchop.text = appointment.shopId // Consider changing to shopName if available
            tvService.text = appointment.service
            viewModel.barber.observeForever { barber ->
                tvBarberName.text = barber?.name ?: "Barber Name"
            }
            viewModel.shop.observeForever { shop ->
                tvBarberchop.text = shop?.name ?: "Shop Name"
            }
             // Consider changing to barberName if available

            tvStatus.text = appointment.status
            val backgroundRes = when (appointment.status) {
                "completed" -> R.drawable.status_completed_background
                "canceled" -> R.drawable.status_cancelled_background
                else -> R.drawable.status_pending_background
            }
            tvStatus.setBackgroundResource(backgroundRes)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAppointments: List<Appointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        notifyDataSetChanged()
    }
}