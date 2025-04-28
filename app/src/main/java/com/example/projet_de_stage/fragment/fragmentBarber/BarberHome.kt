package com.example.projet_de_stage.fragment.fragmentBarber

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterBarber.HomeRequestsAdapter
import com.example.projet_de_stage.adapter.adapterBarber.ShopAdapterBarber
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.viewModel.BarberViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate

class BarberHome : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var caretShopOn: CardView
    private lateinit var caretShopOff: CardView
    private lateinit var tvShopStatus: TextView

    private val homeRequestsAdapter = HomeRequestsAdapter()
    @RequiresApi(Build.VERSION_CODES.O)
    private val shopAdapterBarber = ShopAdapterBarber { shop ->
        onRequestJoinShop(shop)
    }

    private val barberViewModel = BarberViewModel()
    private lateinit var barber: Barber
    private var shop: Shop? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_barber_home, container, false)

        recyclerView = view.findViewById(R.id.rvTodayAppointments)
        caretShopOn = view.findViewById(R.id.shopDetailsLayoutOn)
        caretShopOff = view.findViewById(R.id.shopDetailsLayoutOff) // كنت كاتبها غلط
        tvShopStatus = view.findViewById(R.id.tvShopStatus)

        barber = arguments?.getParcelable("barber") ?: throw IllegalArgumentException("Barber not found")

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshUI()


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshUI() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            shop = barberViewModel.getShopByBarber(barber)
            setupUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun setupUI() {
        if (shop == null) {
            // Barber ما منضم حتى شي محل
            showNoShopUI()
        } else {
            // Barber منضم لمحل
            showShopDetailsUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun showNoShopUI() {
        caretShopOn.visibility = View.GONE
        caretShopOff.visibility = View.VISIBLE
        tvShopStatus.text = "لم تنضم لأي محل بعد"

        val allShops = barberViewModel.getAllShops()
        shopAdapterBarber.updateData(allShops.filter { it.barbers.size < it.nbarbers })
        recyclerView.adapter = shopAdapterBarber
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showShopDetailsUI() {
        caretShopOn.visibility = View.VISIBLE
        caretShopOff.visibility = View.GONE
        recyclerView.adapter = homeRequestsAdapter

        val appointments = listOf(
            Appointment(
                id = "1",
                clientName = "أ. محمد",
                time = "10:00 ص",
                service = "حلاقة + لحية",
                status = "accepted",
                date = LocalDate.of(2025, 4, 13),
                shopId = "${shop!!.id}",
                barberId = "${barber.uid}"
            ),
            // ممكن تزيد مواعيد أخرى هنا
        )

        homeRequestsAdapter.updateData(appointments)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onRequestJoinShop(shop: Shop) {
        val id = FirebaseFirestore.getInstance().collection("joinRequests").document().id
        val joinRequest = JoinRequest(
            id = id,
            experience = barber.experience,
            idBarber = barber.uid,
            idShop = shop.id,
            date = LocalDate.now().toString(),
            status = "pending" ,
            idShopOwner = shop.idOwner
        )
        barberViewModel.createJoinRequests(joinRequest) { success ->
            if (success) {
                refreshUI()
                Toast.makeText(requireContext(), "تم إرسال طلب الانضمام إلى: ${shop.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "لم يتم إرسال طلب الانضمام", Toast.LENGTH_SHORT).show()
            }
        }
    }

}