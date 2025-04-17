package com.example.projet_de_stage.fragment.fragmentBarber

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
import com.example.projet_de_stage.adapter.adabterBarber.AcceptedRequestsAdapter
import com.example.projet_de_stage.data.Appointment
import java.time.LocalDate

class AcceptedRequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = AcceptedRequestsAdapter(
        onAcceptClick = { appointment ->
            // Handle additional accept action if needed
        },
        onRejectClick = { appointment ->
            // Handle reject action for already accepted appointment
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accepted_requests, container, false)
        recyclerView = view.findViewById(R.id.list_Accepted)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load your data - example:
        val appointments = listOf(
            Appointment(
                "1",
                "أحمد محمد",
                "10:00 ص",
                "حلاقة + لحية",
                "pending",
                LocalDate.of(2025, 4, 13),
                "shop1",
                "barber1"
            ),
            Appointment(
                "2",
                "محمد علي",
                "11:30 ص",
                "حلاقة فقط",
                "accepted",
                LocalDate.of(2025, 4, 13),
                "shop2",
                "barber2"
            )
        )
        adapter.updateData(appointments)
    }
}