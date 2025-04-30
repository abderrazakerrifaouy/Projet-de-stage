package com.example.projet_de_stage.adapter.adabterBarber

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.viewModel.BarberViewModel
import java.time.LocalDate

class HomeRequestsAdapter(
    private val onAcceptRequestClick: (Appointment) -> Unit
) : RecyclerView.Adapter<HomeRequestsAdapter.NewRequestViewHolder>() {

    private val newRequests = mutableListOf<Appointment>()
    private val viewModels = BarberViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Appointment>) {
        newRequests.clear()
        newRequests.addAll(newList.filter { it.status == "accepted" && it.date == LocalDate.now().toString() })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
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
        private val buttonAcceptRequest: Button = itemView.findViewById(R.id.btnInAccept)

        @SuppressLint("SetTextI18n")
        fun bind(appointment: Appointment) {
            tvTime.text = appointment.time
            tvDateTime.text = appointment.date.toString() // Display the date
            tvServices.text = appointment.service
            viewModels.loadCustomerById(appointment.clientId)
            viewModels.customer.observe(itemView.context as LifecycleOwner) { customer ->
                tvCustomerName.text = customer?.name
            }


            // Handle button click for accepting the request
            buttonAcceptRequest.setOnClickListener {
                onAcceptRequestClick(appointment)
            }
        }
    }
}
