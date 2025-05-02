package com.example.projet_de_stage.fragment.fragmentAdmin

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterAdmin.AppointmentsAdapter
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.viewModel.AdminViewModel

/**
 * Fragment to display and manage appointments for the shop owner.
 */
class AppointmentsFragment : Fragment() {
    private val adminViewModel = AdminViewModel() // Changed from 'admineViewModel' for consistency.

    /**
     * This method is called when the fragment's view is created. It initializes the RecyclerView
     * and observes the appointments list to update the UI.
     */
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appointments, container, false)

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.appointmentsRecyclerView)
        val shopOwnerUid = arguments?.getString("shopOwner")

        // Fetch appointments for the specific shop owner
        adminViewModel.getAppointmentsByShopOwnerId(shopOwnerUid ?: "")
        val appointments = mutableListOf<Appointment>()

        // Observe the appointments LiveData from the ViewModel and update the RecyclerView
        adminViewModel.appointments.observe(viewLifecycleOwner) { newAppointments ->
            appointments.clear()
            appointments.addAll(newAppointments)
            recyclerView.adapter?.notifyDataSetChanged() // Update the adapter when data changes
        }

        // Set the RecyclerView adapter with a lambda for handling appointment status changes
        recyclerView.adapter = AppointmentsAdapter(appointments) { id, _ ->
            // Update appointment status to 'canceled'
            adminViewModel.updateAppointmentStatus(
                appointmentId = id,
                newStatus = "canceled",
                onSuccess = {
                    // Show a success message when the appointment is canceled
                    Toast.makeText(requireContext(), "Appointment canceled successfully", Toast.LENGTH_SHORT).show()
                    refreshUi() // Refresh the UI to reflect the changes
                },
                onFailure = {
                    // Show an error message if the update fails
                    Toast.makeText(requireContext(), "Error occurred while canceling the appointment", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Set the layout manager for RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    /**
     * Refreshes the UI by fetching the latest appointments for the shop owner.
     */
    private fun refreshUi() {
        val shopOwnerUid = arguments?.getString("shopOwner")
        if (!shopOwnerUid.isNullOrEmpty()) {
            adminViewModel.getAppointmentsByShopOwnerId(shopOwnerUid)
        }
    }
}
