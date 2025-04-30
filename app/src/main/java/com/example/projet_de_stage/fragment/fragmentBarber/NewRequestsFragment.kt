package com.example.projet_de_stage.fragment.fragmentBarber

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
import com.example.projet_de_stage.adapter.adabterBarber.NewRequestsAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.BarberViewModel

class NewRequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val brberviewModel = BarberViewModel()
    private var barber: Barber? = null  // <-- هنا التغيير

    private val adapter = NewRequestsAdapter(
        onAcceptClick = { appointment ->
            // Handle additional accept action if needed
        },
        onRejectClick = { appointment ->
            // Handle reject action for already accepted appointment
        }
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_requests, container, false)
        recyclerView = view.findViewById(R.id.list_New)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barber = arguments?.getParcelable("barber")
        Toast.makeText(requireContext(), "Barber ID: ${barber?.uid}", Toast.LENGTH_SHORT).show()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        // استدعاء ViewModel
        brberviewModel.getAppointmentByBarberIdandStatus(
            status = "pending",
            barberId = barber?.uid.toString()
        )

        // بيانات تجريبية

        brberviewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            adapter.updateData(appointments)
        }
        brberviewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(barber: Barber?): NewRequestsFragment {
            val fragment = NewRequestsFragment()
            val args = Bundle()
            args.putParcelable("barber", barber)
            fragment.arguments = args
            return fragment
        }
    }
}
