package com.example.projet_de_stage.adapter


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import java.time.LocalDate

class HomeRequestsAdapter() : RecyclerView.Adapter<HomeRequestsAdapter.NewRequestViewHolder>()
{

    private val newRequests = mutableListOf<Appointment>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Appointment>) {
        newRequests.clear()
        newRequests.addAll(newList.filter { it.status == "accepted" && it.date == LocalDate.now() }) // Filter for new/pending requests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_item_appointment, parent, false) // Using the same layout
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

        fun bind(appointment: Appointment) {
            tvTime.text = appointment.time
            tvDateTime.text = "" // Add date if available in your Appointment class
            tvCustomerName.text = appointment.clientName
            tvServices.text = appointment.service

            // Set button texts and behaviors for NEW requests
        }
    }


}