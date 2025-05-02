package com.example.projet_de_stage.adapter.adapterBarber

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

/**
 * Adapter for displaying today's accepted appointments on the barber's home screen.
 *
 * @param onAcceptRequestClick Callback invoked when the "Accept" button is clicked.
 */
class HomeRequestsAdapter(
    private val onAcceptRequestClick: (Appointment) -> Unit
) : RecyclerView.Adapter<HomeRequestsAdapter.NewRequestViewHolder>() {

    private val newRequests = mutableListOf<Appointment>()
    private val viewModel = BarberViewModel()

    /**
     * Updates the adapter's data by filtering today's accepted appointments.
     *
     * @param newList Full list of appointments.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Appointment>) {
        newRequests.clear()
        newRequests.addAll(
            newList.filter {
                it.status == "accepted" && it.date == LocalDate.now().toString()
            }
        )
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

    override fun getItemCount(): Int = newRequests.size

    /**
     * ViewHolder for appointment items.
     */
    inner class NewRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        private val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        private val tvServices: TextView = itemView.findViewById(R.id.tvServices)
        private val btnAcceptRequest: Button = itemView.findViewById(R.id.btnInAccept)

        /**
         * Binds the appointment data to UI elements.
         *
         * @param appointment The appointment to display.
         */
        @SuppressLint("SetTextI18n")
        fun bind(appointment: Appointment) {
            tvTime.text = appointment.time
            tvDateTime.text = appointment.date
            tvServices.text = appointment.service

            viewModel.loadCustomerById(appointment.clientId)
            viewModel.customer.observe(itemView.context as LifecycleOwner) { customer ->
                tvCustomerName.text = customer?.name
            }

            btnAcceptRequest.setOnClickListener {
                onAcceptRequestClick(appointment)
            }
        }
    }
}
