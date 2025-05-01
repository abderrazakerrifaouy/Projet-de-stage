package com.example.projet_de_stage.adapter.adabterAdmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.viewModel.AdmineViewModel
import com.example.projet_de_stage.viewModel.BarberViewModel

class AppointmentsAdapter(
    private val appointments: List<Appointment>,
    private val onAction: (String, Boolean) -> Unit
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    private val viewModel = AdmineViewModel()

    inner class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClientName = view.findViewById<TextView>(R.id.tvCustomerName)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
        val tvService = view.findViewById<TextView>(R.id.tvServices)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val btnAccept = view.findViewById<Button>(R.id.btnInAccept)
        val tvDateTime = view.findViewById<TextView>(R.id.tvDateTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val item = appointments[position]
        viewModel.loadCustomerById(item.clientId)
        viewModel.customer.observeForever {
            holder.tvClientName.text = it?.name
        }
        holder.tvDateTime.text = item.date
        holder.tvTime.text = item.time
        holder.tvService.text = item.service
        holder.tvStatus.text = item.status

        holder.btnAccept.setOnClickListener { onAction(item.id, true) }
    }

    override fun getItemCount(): Int = appointments.size


}

