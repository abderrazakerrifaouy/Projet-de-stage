package com.example.projet_de_stage.fragment.fragmentBarber

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
import com.example.projet_de_stage.adapter.adapterBarber.NewRequestsAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.BarberViewModel

/**
 * Fragment to display new appointment requests for a barber.
 */
class NewRequestsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val barberViewModel = BarberViewModel()
    private var barber: Barber? = null  // Barber object passed from the previous fragment

    @RequiresApi(Build.VERSION_CODES.O)
    private val adapter = NewRequestsAdapter(
        onAcceptClick = { appointment ->
            barberViewModel.updateAppointmentStatus(
                appointmentId = appointment.id,
                newStatus = "accepted",
                onSuccess = {
                    Toast.makeText(requireContext(), "Appointment accepted successfully", Toast.LENGTH_SHORT).show()
                    refreshUI() // Refresh the UI after accepting the appointment
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        },
        onRejectClick = { appointment ->
            barberViewModel.updateAppointmentStatus(
                appointmentId = appointment.id,
                newStatus = "canceled",
                onSuccess = {
                    Toast.makeText(requireContext(), "Appointment canceled successfully", Toast.LENGTH_SHORT).show()
                    refreshUI() // Refresh the UI after rejecting the appointment
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    )

    /**
     * Called when the fragment view is created. Sets up RecyclerView and UI components.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_requests, container, false)
        recyclerView = view.findViewById(R.id.list_New)
        return view
    }

    /**
     * Called when the fragment's view has been created. Loads barber data and appointment requests.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the barber object passed via arguments
        barber = arguments?.getParcelable("barber")
        Toast.makeText(requireContext(), "Barber ID: ${barber?.uid}", Toast.LENGTH_SHORT).show()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load pending appointment requests for this barber
        barberViewModel.getAppointmentByBarberIdAndStatus(
            status = "pending",
            barberId = barber?.uid.toString()
        )

        // Observe the appointments live data and update the adapter when data changes
        barberViewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            adapter.updateData(appointments)
        }

        // Observe errors and show a toast message in case of failure
        barberViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Creates a new instance of this fragment with a barber object passed as an argument.
     */
    companion object {
        fun newInstance(barber: Barber?): NewRequestsFragment {
            val fragment = NewRequestsFragment()
            val args = Bundle()
            args.putParcelable("barber", barber)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Refreshes the UI by loading the latest pending appointments for the barber.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshUI() {
        barber?.uid?.let { barberId ->
            barberViewModel.getAppointmentByBarberIdAndStatus(
                status = "pending",
                barberId = barberId
            )
        } ?: Toast.makeText(requireContext(), "Unable to refresh UI: Barber not available", Toast.LENGTH_SHORT).show()
    }
}
