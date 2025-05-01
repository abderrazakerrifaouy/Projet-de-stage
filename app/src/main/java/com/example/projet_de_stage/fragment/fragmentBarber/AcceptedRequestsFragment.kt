package com.example.projet_de_stage.fragment.fragmentBarber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterBarber.AcceptedRequestsAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.BarberViewModel

class AcceptedRequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val barberViewModel = BarberViewModel()
    private var barber: Barber? = null  // <-- هنا التغيير
    private val adapter = AcceptedRequestsAdapter(
        onRejectClick = { appointment ->
            barberViewModel.updateAppointmentStatus(
                appointmentId = appointment.id,
                newStatus = "canceled",
                onSuccess = {
                    Toast.makeText(requireContext(), "تم إلغاء المواعيد بنجاح", Toast.LENGTH_SHORT).show()
                    refreshUI()
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        barber = arguments?.getParcelable("barber")
        Toast.makeText(requireContext(), "Barber ID: ${barber?.uid}", Toast.LENGTH_SHORT).show()
        refreshUI()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load your data - example:
        barberViewModel.getAppointmentByBarberIdandStatus(
            status = "accepted",
            barberId = barber?.uid.toString()
        )

        barberViewModel.appointments.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
        barberViewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
        }

    }
    }

    companion object {
        fun newInstance(barber: Barber?): AcceptedRequestsFragment {
            val fragment = AcceptedRequestsFragment()
            val args = Bundle()
            args.putParcelable("barber", barber)
            fragment.arguments = args
            return fragment
        }
    }

    private fun refreshUI() {
        barber?.uid?.let { barberId ->
            barberViewModel.getAppointmentByBarberIdandStatus(
                status = "accepted",
                barberId = barberId
            )
        } ?: Toast.makeText(requireContext(), "لا يمكن تحديث الواجهة: الحلاق غير متوفر", Toast.LENGTH_SHORT).show()
    }

}