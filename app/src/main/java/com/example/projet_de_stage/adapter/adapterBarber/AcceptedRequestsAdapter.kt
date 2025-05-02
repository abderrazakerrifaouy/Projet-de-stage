package com.example.projet_de_stage.adapter.adapterBarber

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.viewModel.BarberViewModel

/**
 * Adapter for displaying accepted appointment requests in a RecyclerView.
 * Allows the barber to see accepted appointments and reject them if needed.
 *
 * @param onRejectClick Callback to handle reject button click for a given appointment.
 */
class AcceptedRequestsAdapter(
    private val onRejectClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AcceptedRequestsAdapter.AcceptedRequestViewHolder>() {

    private val acceptedRequests = mutableListOf<Appointment>()
    private val viewModel = BarberViewModel()

    /**
     * Updates the list of accepted appointments and refreshes the view.
     *
     * @param newList List of all appointments (will filter accepted only).
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Appointment>) {
        acceptedRequests.clear()
        acceptedRequests.addAll(newList.filter { it.status == "accepted" })
        notifyDataSetChanged()
    }

    /**
     * Inflates the layout for a single appointment item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_small, parent, false)
        return AcceptedRequestViewHolder(view)
    }

    /**
     * Binds data to each item in the list.
     */
    override fun onBindViewHolder(holder: AcceptedRequestViewHolder, position: Int) {
        val currentItem = acceptedRequests[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = acceptedRequests.size

    /**
     * ViewHolder class for accepted appointment items.
     */
    inner class AcceptedRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(appointment: Appointment) {
            val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
            val tvDateTime = itemView.findViewById<TextView>(R.id.tvDateTime)
            val tvServices = itemView.findViewById<TextView>(R.id.tvServices)
            val tvCustomerName = itemView.findViewById<TextView>(R.id.tvCustomerName)
            val btnAccept = itemView.findViewById<Button>(R.id.btnAccept)
            val btnReject = itemView.findViewById<Button>(R.id.btnReject)

            tvTime.text = appointment.time
            tvDateTime.text = appointment.date  // Optional: format this if needed
            tvServices.text = appointment.service

            // Load and observe customer name using ViewModel
            viewModel.loadCustomerById(appointment.clientId)
            viewModel.customer.observe(itemView.context as LifecycleOwner) { customer ->
                tvCustomerName.text = customer?.name ?: "Unknown"
            }

            // Disable accept button since it's already accepted
            btnAccept.apply {
                text = "Accepted"
                isEnabled = false
            }

            // Reject button is still active
            btnReject.setOnClickListener {
                onRejectClick(appointment)
            }
        }
    }
}
