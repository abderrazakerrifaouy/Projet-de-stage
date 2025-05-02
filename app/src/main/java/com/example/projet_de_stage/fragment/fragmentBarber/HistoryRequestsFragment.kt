package com.example.projet_de_stage.fragment.fragmentBarber

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
import com.example.projet_de_stage.adapter.adapterBarber.HistoryRequestsAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.BarberViewModel

/**
 * Fragment to display the history of appointments for a specific barber.
 */
class HistoryRequestsFragment : Fragment() {

    private lateinit var recyclerViewBarber: RecyclerView // Renamed for clarity
    private val adapter = HistoryRequestsAdapter()
    private var barber: Barber? = null  // Barber object passed from the previous fragment
    private val viewModels = BarberViewModel()

    /**
     * Called when the fragment view is created. Sets up RecyclerView and UI components.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history_requests, container, false)
    }

    /**
     * Called when the fragment's view has been created. Loads barber data and appointment history.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the barber object passed via arguments
        barber = arguments?.getParcelable("barber")
        Toast.makeText(requireContext(), "Barber ID: ${barber?.uid}", Toast.LENGTH_SHORT).show()

        // Set up the RecyclerView
        recyclerViewBarber = view.findViewById(R.id.list_item_history)
        recyclerViewBarber.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewBarber.adapter = adapter

        // Load the history of appointments for this barber
        loadHistoryData()
    }

    /**
     * Loads the history of appointments for the specific barber.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistoryData() {
        barber?.uid?.let { barberId ->
            viewModels.getAllAppointmentsByBarberId(barberId)

            // Observe the appointments and update the adapter
            viewModels.appointments.observe(viewLifecycleOwner) { appointments ->
                adapter.submitList(appointments)
            }
        } ?: Toast.makeText(requireContext(), "Unable to load history: Barber not available", Toast.LENGTH_SHORT).show()
    }

    /**
     * Creates a new instance of this fragment with a barber object passed as an argument.
     */
    companion object {
        fun newInstance(barber: Barber?): HistoryRequestsFragment {
            val fragment = HistoryRequestsFragment()
            val args = Bundle()
            args.putParcelable("barber", barber)
            fragment.arguments = args
            return fragment
        }
    }
}
