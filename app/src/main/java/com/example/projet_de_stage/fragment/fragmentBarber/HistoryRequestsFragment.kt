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
import com.example.projet_de_stage.adapter.adabterBarber.HistoryRequestsAdapter
import com.example.projet_de_stage.data.Appointment
import java.time.LocalDate

class HistoryRequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = HistoryRequestsAdapter()

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

        recyclerView = view.findViewById(R.id.list_item_history)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadHistoryData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistoryData() {
        val historyData = listOf(
            Appointment(
                "1",
                "أحمد محمد",
                "10:00 ص",
                "حلاقة + لحية",
                "completed",
                LocalDate.of(2025, 4, 13),
                "shop1",
                "barber1"
            ),
        )

        adapter.submitList(historyData)
    }
}