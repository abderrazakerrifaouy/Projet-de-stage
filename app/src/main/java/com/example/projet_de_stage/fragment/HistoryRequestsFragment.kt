package com.example.projet_de_stage.fragment

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
import com.example.projet_de_stage.adapter.HistoryRequestsAdapter
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
            Appointment("1", "أحمد محمد", "10:00 ص", "حلاقة + لحية", "completed", LocalDate.of(2025, 4, 13)),
            Appointment("2", "محمد علي", "11:30 ص", "حلاقة فقط", "canceled", LocalDate.of(2025, 1, 13)),
            Appointment("3", "سعيد عبدالله", "02:00 م", "لحية + تنظيف", "rejected", LocalDate.of(2025, 3, 13)),
            Appointment("4", "خالد حسن", "03:30 م", "حلاقة أطفال", "completed", LocalDate.of(2025, 2, 13))
        )

        adapter.submitList(historyData)
    }
}