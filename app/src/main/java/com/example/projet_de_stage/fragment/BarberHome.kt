package com.example.projet_de_stage.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.HomeRequestsAdapter
import com.example.projet_de_stage.data.Appointment
import java.time.LocalDate

class BarberHome : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = HomeRequestsAdapter()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_barber_home, container, false)
        recyclerView = view.findViewById(R.id.rvTodayAppointments)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load your data - example:
        val appointments = listOf(
            Appointment("1", "أxد محمد", "10:00 ص", "حلاقة + لحية", "accepted" , LocalDate.of(2025, 4, 13) , "shop1", "barber1"),
        )
        adapter.updateData(appointments)
    }
}