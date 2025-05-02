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

/**
 * Adapter for displaying a list of appointments.
 */
class AppointmentAdapter(
    private val appointments: MutableList<Appointment>,
    private val onItemClick: (Appointment) -> Unit = {}
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val viewModel = ClientViewModel()

    /**
     * Inflates the item layout and creates a ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_request_client, parent, false) // Changed to item layout
        return AppointmentViewHolder(view)
    }

    /**
     * Binds the appointment data to the ViewHolder.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
        holder.itemView.setOnClickListener { onItemClick(appointment) }
    }

    /**
     * Returns the size of the appointment list.
     */
    override fun getItemCount(): Int = appointments.size

    /**
     * ViewHolder class for binding the appointment data to views.
     */
    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvBarberShop: TextView = itemView.findViewById(R.id.tvBarberchop) // Renamed for clarity
        private val tvService: TextView = itemView.findViewById(R.id.tvService)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvBarberName: TextView = itemView.findViewById(R.id.tvBarberName)

        /**
         * Binds the appointment data to the views.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(appointment: Appointment) {
            viewModel.getShopById(appointment.shopId)
            viewModel.getBarberById(appointment.barberId)
            tvDate.text = appointment.date.format(dateFormatter)
            tvTime.text = appointment.time
            tvBarberShop.text = appointment.shopId // Consider changing to shopName if available
            tvService.text = appointment.service

            // Observer for barber data
            viewModel.barber.observeForever { barber ->
                tvBarberName.text = barber?.name ?: "Barber Name"
            }

            // Observer for shop data
            viewModel.shop.observeForever { shop ->
                tvBarberShop.text = shop?.name ?: "Shop Name"
            }

            tvStatus.text = appointment.status
            val backgroundRes = when (appointment.status) {
                "completed" -> R.drawable.status_completed_background
                "canceled" -> R.drawable.status_cancelled_background
                "rejected" -> R.drawable.status_cancelled_background
                else -> R.drawable.status_pending_background
            }
            tvStatus.setBackgroundResource(backgroundRes)
        }
    }

    /**
     * Updates the list of appointments and notifies the adapter.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAppointments: List<Appointment>) {
        appointments.clear()
        appointments.addAll(newAppointments)
        notifyDataSetChanged()
    }
}
