package com.example.projet_de_stage.fragment.fragmentAdmin

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
import com.example.projet_de_stage.adapter.adabterAdmin.AppointmentsAdapter
import com.example.projet_de_stage.data.Appointment
import java.time.LocalDate

class AppointmentsFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_appointments, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.appointmentsRecyclerView)
        val appointments = listOf(
            Appointment(
                "1",
                "أحمد محمد",
                "10:00 ص",
                "حلاقة + لحية",
                "pending",
                LocalDate.of(2025, 4, 13),
                "abdo",
                "hsduihc"
            ),
        )

        recyclerView.adapter = AppointmentsAdapter(appointments) { id, accepted ->
            Toast.makeText(
                requireContext(),
                "الموعد $id ${if (accepted) "تم قبوله" else "تم رفضه"}",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}