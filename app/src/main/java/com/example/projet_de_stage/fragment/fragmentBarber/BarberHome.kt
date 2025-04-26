package com.example.projet_de_stage.fragment.fragmentBarber

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterBarber.HomeRequestsAdapter
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.viewModel.BarberViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class BarberHome : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val adapter = HomeRequestsAdapter()
    private val barberViewModel = BarberViewModel()
    private lateinit var caretShopOn : CardView
    private lateinit var caretShopOf : CardView
    private lateinit var barber : Barber
    private  var shop : Shop? = null
    private lateinit var tvShopStatus : TextView

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_barber_home, container, false)
        recyclerView = view.findViewById(R.id.rvTodayAppointments)
        caretShopOn = view.findViewById(R.id.shopDetailsLayoutOn)
        caretShopOf = view.findViewById(R.id.shopDetailsLayoutOn)
        tvShopStatus = view.findViewById(R.id.tvShopStatus)
        barber = arguments?.getParcelable("barber")!!
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch{
             shop = barberViewModel.getShopByBarber(barber)
        }
        when(shop){
             null -> {
                caretShopOn.visibility = View.GONE
                caretShopOf.visibility = View.VISIBLE
                 tvShopStatus.text = "لم تنضم لأي محل بعد"
            }
            else -> {
                caretShopOn.visibility = View.VISIBLE
                caretShopOf.visibility = View.GONE
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter

                // Load your data - example:
                val appointments = listOf(
                    Appointment(
                        "1",
                        "أxد محمد",
                        "10:00 ص",
                        "حلاقة + لحية",
                        "accepted",
                        LocalDate.of(2025, 4, 13),
                        "shop1",
                        "barber1"
                    ),
                )
                adapter.updateData(appointments)
            }

        }
    }


}