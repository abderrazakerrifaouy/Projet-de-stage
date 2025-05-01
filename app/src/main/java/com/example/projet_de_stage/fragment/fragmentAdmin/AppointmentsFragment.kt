package com.example.projet_de_stage.fragment.fragmentAdmin

import android.annotation.SuppressLint
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
import com.example.projet_de_stage.viewModel.AdmineViewModel
import java.time.LocalDate

class AppointmentsFragment : Fragment() {
    private val admineViewModel = AdmineViewModel()

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_appointments, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.appointmentsRecyclerView)
        val shopOwnerUid = arguments?.getString("shopOwner")

        admineViewModel.getAppointmentsByShopOwnrId(shopOwnerUid ?: "")
        val appointments = mutableListOf<Appointment>()
        admineViewModel.appointments.observe(viewLifecycleOwner) { newAppointments ->
            appointments.clear()
            appointments.addAll(newAppointments)
            recyclerView.adapter?.notifyDataSetChanged()
        }


        recyclerView.adapter = AppointmentsAdapter(appointments) { id, _ ->
            admineViewModel.updateAppointmentStatus(
                appointmentId = id,
                newStatus = "canceled" ,
                onSuccess = {

                    Toast.makeText(requireContext(), "تم إلغاء الحجز بنجاح", Toast.LENGTH_SHORT).show()
                    reflecheUi()
                },
                onFailure = {
                    Toast.makeText(requireContext(), "حدث خطأ أثناء إلغاء الحجز", Toast.LENGTH_SHORT).show()
                }
            )
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    private fun reflecheUi() {
        val shopOwnerUid = arguments?.getString("shopOwner")
        if (!shopOwnerUid.isNullOrEmpty()) {
            admineViewModel.getAppointmentsByShopOwnrId(shopOwnerUid)
        }
    }


}