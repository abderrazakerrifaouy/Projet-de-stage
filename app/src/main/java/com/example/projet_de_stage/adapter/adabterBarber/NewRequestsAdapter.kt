package com.example.projet_de_stage.adapter.adabterBarber

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

class NewRequestsAdapter(
    private val onAcceptClick: (Appointment) -> Unit,
    private val onRejectClick: (Appointment) -> Unit
) : RecyclerView.Adapter<NewRequestsAdapter.NewRequestViewHolder>()
{

    private val newRequests = mutableListOf<Appointment>()
    private val viewModels = BarberViewModel()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Appointment>) {
        newRequests.clear()
        newRequests.addAll(newList) // Filter for new/pending requests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_small, parent, false) // Using the same layout
        return NewRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewRequestViewHolder, position: Int) {
        holder.bind(newRequests[position])
    }

    override fun getItemCount() = newRequests.size

    inner class NewRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        private val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        private val tvServices: TextView = itemView.findViewById(R.id.tvServices)
        private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        private val btnReject: Button = itemView.findViewById(R.id.btnReject)

        fun bind(appointment: Appointment) {
            viewModels.loadCustomerById(appointment.clientId)
            tvTime.text = appointment.time
            tvDateTime.text = appointment.date // Add date if available in your Appointment class
            tvServices.text = appointment.service

            viewModels.customer.observe(itemView.context as LifecycleOwner) { customer ->
                if (customer != null) {
                    tvCustomerName.text = customer.name
                }
            }

            // Set button texts and behaviors for NEW requests
            btnAccept.text = "قبول"
            btnReject.text = "رفض"

            btnAccept.setOnClickListener { onAcceptClick(appointment) }
            btnReject.setOnClickListener { onRejectClick(appointment) }
        }
    }
}