package com.example.projet_de_stage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.AppointmentsAdapter
import com.example.projet_de_stage.data.Appointment

class AppointmentsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_appointments, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.appointmentsRecyclerView)
        val appointments = listOf(
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار"),
            Appointment("1", "أحمد محمد", "10:00", "حلاقة + لحية", "في الانتظار")
        )

        recyclerView.adapter = AppointmentsAdapter(appointments) { id, accepted ->
            Toast.makeText(requireContext(), "الموعد $id ${if (accepted) "تم قبوله" else "تم رفضه"}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}

