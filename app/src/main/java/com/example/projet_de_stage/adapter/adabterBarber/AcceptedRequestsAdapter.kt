package com.example.projet_de_stage.adapter.adabterBarber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment

class AcceptedRequestsAdapter(
    private val onAcceptClick: (Appointment) -> Unit,
    private val onRejectClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AcceptedRequestsAdapter.AcceptedRequestViewHolder>() {

    private val acceptedRequests = mutableListOf<Appointment>()

    fun updateData(newList: List<Appointment>) {
        acceptedRequests.clear()
        acceptedRequests.addAll(newList.filter { it.status == "accepted" })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_small, parent, false)
        return AcceptedRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: AcceptedRequestViewHolder, position: Int) {
        val currentItem = acceptedRequests[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = acceptedRequests.size

    inner class AcceptedRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(appointment: Appointment) {
            itemView.findViewById<TextView>(R.id.tvTime).text = appointment.time
            itemView.findViewById<TextView>(R.id.tvDateTime).text = "" // Add date if available
            itemView.findViewById<TextView>(R.id.tvCustomerName).text = appointment.clientId
            itemView.findViewById<TextView>(R.id.tvServices).text = appointment.service

            // Since these are already accepted requests, we might want to change button behavior
            itemView.findViewById<Button>(R.id.btnAccept).apply {
                text = "تم القبول"
                isEnabled = false
                // Or hide it if not needed: visibility = View.GONE
            }

            itemView.findViewById<Button>(R.id.btnReject).setOnClickListener {
                onRejectClick(appointment)
            }
        }
    }
}