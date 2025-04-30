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
import com.example.projet_de_stage.adapter.adabterBarber.HistoryRequestsAdapter
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.BarberViewModel
import java.time.LocalDate

class HistoryRequestsFragment : Fragment() {
    private lateinit var recyclerViewBaber: RecyclerView
    private val adapter = HistoryRequestsAdapter()
    private var barber: Barber? = null  // <-- هنا التغيير
    private val viewModels = BarberViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history_requests, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        barber = arguments?.getParcelable("barber")
        Toast.makeText(requireContext(), "Barber ID: ${barber?.uid}", Toast.LENGTH_SHORT).show()


        recyclerViewBaber = view.findViewById(R.id.list_item_history)
        recyclerViewBaber.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewBaber.adapter = adapter

        loadHistoryData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistoryData() {
        viewModels.getAllAppointmentsByBarberId(barber?.uid.toString())

        viewModels.appointments.observe(viewLifecycleOwner) { appointments ->
            adapter.submitList(appointments)
        }
    }

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