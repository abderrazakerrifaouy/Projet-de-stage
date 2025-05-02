package com.example.projet_de_stage.adapter.adapterAdmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.viewModel.AdminViewModel

/**
 * Adapter for displaying appointments in a RecyclerView.
 * This adapter is responsible for binding data to each appointment item view.
 */
class AppointmentsAdapter(
    private val appointments: List<Appointment>,  // List of appointments to display
    private val onAction: (String, Boolean) -> Unit // Callback for action button click (e.g., accept)
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    private val viewModel = AdminViewModel()

    /**
     * ViewHolder for each appointment item.
     * Holds references to UI elements in the item layout.
     */
    inner class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClientName: TextView = view.findViewById(R.id.tvCustomerName)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvService: TextView = view.findViewById(R.id.tvServices)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAccept: Button = view.findViewById(R.id.btnInAccept)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
    }

    /**
     * Creates and returns a new ViewHolder object to represent a single appointment item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    /**
     * Binds data to the views in the ViewHolder for a specific appointment.
     * Updates the client name and other details like time, service, and status.
     */
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val item = appointments[position]

        // Load customer data using ViewModel
        viewModel.loadCustomerById(item.clientId)
        viewModel.customer.observeForever {
            holder.tvClientName.text = it?.name // Display the customer's name
        }

        // Set appointment details
        holder.tvDateTime.text = item.date
        holder.tvTime.text = item.time
        holder.tvService.text = item.service
        holder.tvStatus.text = item.status

        // Handle the accept button click
        holder.btnAccept.setOnClickListener { onAction(item.id, true) }
    }

    /**
     * Returns the total number of items in the appointments list.
     */
    override fun getItemCount(): Int = appointments.size
}
