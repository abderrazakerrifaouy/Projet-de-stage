package com.example.projet_de_stage.fragment.fragmentBarber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterBarber.AcceptedRequestsAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.BarberViewModel

/**
 * Fragment to display accepted appointment requests for a specific barber.
 */
class AcceptedRequestsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val barberViewModel = BarberViewModel()
    private var barber: Barber? = null  // Barber object passed from the previous fragment
    private val adapter = AcceptedRequestsAdapter(
        onRejectClick = { appointment ->
            barberViewModel.updateAppointmentStatus(
                appointmentId = appointment.id,
                newStatus = "canceled",
                onSuccess = {
                    Toast.makeText(requireContext(), "Appointment successfully canceled", Toast.LENGTH_SHORT).show()
                    refreshUI() // Refresh the UI after cancellation
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                })
        }
    )

    /**
     * Called when the fragment view is created. Sets up RecyclerView and UI components.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accepted_requests, container, false)
        recyclerView = view.findViewById(R.id.list_Accepted)
        return view
    }

    /**
     * Called when the fragment's view has been created. Loads barber data and appointment requests.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the barber object passed via arguments
        barber = arguments?.getParcelable("barber")
        Toast.makeText(requireContext(), "Barber ID: ${barber?.uid}", Toast.LENGTH_SHORT).show()
        refreshUI() // Refresh the UI with the barber's data

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load accepted appointment requests for this barber
        barberViewModel.getAppointmentByBarberIdAndStatus(
            status = "accepted",
            barberId = barber?.uid.toString()
        )

        // Observe the appointments live data and update the adapter when data changes
        barberViewModel.appointments.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        // Observe errors and show a toast message in case of failure
        barberViewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Creates a new instance of this fragment with a barber object passed as an argument.
     */
    companion object {
        fun newInstance(barber: Barber?): AcceptedRequestsFragment {
            val fragment = AcceptedRequestsFragment()
            val args = Bundle()
            args.putParcelable("barber", barber)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Refreshes the UI by loading the latest accepted appointments for the barber.
     */
    private fun refreshUI() {
        barber?.uid?.let { barberId ->
            barberViewModel.getAppointmentByBarberIdAndStatus(
                status = "accepted",
                barberId = barberId
            )
        } ?: Toast.makeText(requireContext(), "Unable to refresh UI: Barber not available", Toast.LENGTH_SHORT).show()
    }
}
