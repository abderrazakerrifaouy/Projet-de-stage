package com.example.projet_de_stage.fragment.fragmentClient

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.AppointmentAdapter
import com.example.projet_de_stage.data.Appointment
import java.time.LocalDate

class HistoryFragmentClient : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history_requests_client, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.list_item_history) // Make sure you have this ID in your layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Create sample data - replace with your actual data source
        val sampleAppointments = listOf(
            Appointment(
                id = "1",
                clientId = "Ahmed Mohamed",
                time = "10:00 ص",
                service = "حلاقة + لحية",
                status = "مكتمل",
                date = LocalDate.now().minusDays(2),
                shopId = "shop1",
                barberId = "barber1"
            ),
            Appointment(
                id = "2",
                clientId = "Mohamed Ali",
                time = "02:30 م",
                service = "حلاقة فقط",
                status = "ملغي",
                date = LocalDate.now().minusDays(5),
                shopId = "shop2",
                barberId = "barber2"
            ),
            Appointment(
                id = "3",
                clientId = "Youssef Ahmed",
                time = "11:15 ص",
                service = "تسريحة شعر",
                status = "مكتمل",
                date = LocalDate.now().minusDays(7),
                shopId = "shop3",
                barberId = "barber3"
            )
        )

        // Initialize adapter
        adapter = AppointmentAdapter(sampleAppointments) { appointment ->
            // Handle item click if needed
            // Example: navigate to details or show dialog
        }

        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = HistoryFragmentClient()
    }
}