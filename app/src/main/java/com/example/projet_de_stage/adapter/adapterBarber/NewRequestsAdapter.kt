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
 * Adapter to display new/pending appointment requests for the barber.
 *
 * @param onAcceptClick Callback for when the user accepts a request.
 * @param onRejectClick Callback for when the user rejects a request.
 */
class NewRequestsAdapter(
    private val onAcceptClick: (Appointment) -> Unit,
    private val onRejectClick: (Appointment) -> Unit
) : RecyclerView.Adapter<NewRequestsAdapter.NewRequestViewHolder>() {

    private val newRequests = mutableListOf<Appointment>()
    private val viewModel = BarberViewModel()

    /**
     * Updates the list of requests shown in the RecyclerView.
     * Expected to be used with unfiltered requests (filter handled externally).
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Appointment>) {
        newRequests.clear()
        newRequests.addAll(newList) // Caller should provide only new/pending appointments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_small, parent, false)
        return NewRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewRequestViewHolder, position: Int) {
        holder.bind(newRequests[position])
    }

    override fun getItemCount(): Int = newRequests.size

    /**
     * ViewHolder that binds appointment data for new requests.
     */
    inner class NewRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        private val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        private val tvServices: TextView = itemView.findViewById(R.id.tvServices)
        private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        private val btnReject: Button = itemView.findViewById(R.id.btnReject)

        /**
         * Binds a single appointment item to the UI.
         *
         * @param appointment The appointment to display.
         */
        fun bind(appointment: Appointment) {
            tvTime.text = appointment.time
            tvDateTime.text = appointment.date
            tvServices.text = appointment.service

            viewModel.loadCustomerById(appointment.clientId)
            viewModel.customer.observe(itemView.context as LifecycleOwner) { customer ->
                tvCustomerName.text = customer?.name ?: "Unknown"
            }

            btnAccept.text = "قبول"
            btnReject.text = "رفض"

            btnAccept.setOnClickListener { onAcceptClick(appointment) }
            btnReject.setOnClickListener { onRejectClick(appointment) }
        }
    }
}
