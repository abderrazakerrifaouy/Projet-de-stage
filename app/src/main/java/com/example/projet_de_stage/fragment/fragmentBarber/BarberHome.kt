package com.example.projet_de_stage.fragment.fragmentBarber

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterBarber.HomeRequestsAdapter
import com.example.projet_de_stage.adapter.adapterBarber.ShopAdapterBarber
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.view.admin.CreateShopActivity
import com.example.projet_de_stage.view.barberUser.MyShopActivity
import com.example.projet_de_stage.viewModel.BarberViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.jvm.java

class BarberHome : Fragment() {

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var caretShopOn: CardView
    private lateinit var caretShopOff: CardView
    private lateinit var tvShopStatus: TextView
    private lateinit var tvShopName: TextView
    private lateinit var tvShopLocation: TextView
    private lateinit var ivShopImage: ImageView
    private lateinit var btnViewShop: Button

    // ViewModel & Data
    private val barberViewModel = BarberViewModel()
    private lateinit var barber: Barber
    private var shop: Shop? = null

    // Adapters
    @RequiresApi(Build.VERSION_CODES.O)
    private val homeRequestsAdapter = HomeRequestsAdapter { appointment ->
        cancelAppointment(appointment.id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val shopAdapterBarber = ShopAdapterBarber { shop ->
        sendJoinRequest(shop)
    }

    // View creation
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_barber_home, container, false)
        initViews(view)
        barber = arguments?.getParcelable("barber") ?: throw IllegalArgumentException("Barber not found")
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshUI()
    }

    // ---------------------- UI Initialization ----------------------

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rvTodayAppointments)
        caretShopOn = view.findViewById(R.id.shopDetailsLayoutOn)
        caretShopOff = view.findViewById(R.id.shopDetailsLayoutOff)
        tvShopStatus = view.findViewById(R.id.tvShopStatus)
        tvShopName = view.findViewById(R.id.tvShopName)
        tvShopLocation = view.findViewById(R.id.tvShopLocation)
        ivShopImage = view.findViewById(R.id.ivShopImage)
        btnViewShop = view.findViewById(R.id.btnViewShopBarber)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshUI() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            shop = barberViewModel.getShopByBarber(barber)
            if (shop == null) showNoShopUI()
            else showShopDetailsUI(shop)
        }
    }

    // ---------------------- UI for No Shop ----------------------

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun showNoShopUI() {
        caretShopOn.visibility = View.GONE
        caretShopOff.visibility = View.VISIBLE
        tvShopStatus.text = "Not assigned to any shop yet"

        val allShops = barberViewModel.getAllShops()
        val availableShops = allShops.filter { it.barbers.size < it.numberOfBarbers }
        shopAdapterBarber.updateData(availableShops)
        recyclerView.adapter = shopAdapterBarber
    }

    // ---------------------- UI for Assigned Shop ----------------------

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showShopDetailsUI(shop: Shop?) {
        caretShopOn.visibility = View.VISIBLE
        caretShopOff.visibility = View.GONE

        tvShopName.text = shop?.name
        tvShopLocation.text = shop?.address

        if (!shop?.imageUrl.isNullOrEmpty()) {
            Glide.with(requireContext()).load(shop.imageUrl).into(ivShopImage)
        } else {
            ivShopImage.setImageResource(R.drawable.my_profile)
        }

        loadAcceptedAppointments()
    if (shop != null) {
        setupViewShopButton(shop)
    }

    }

    private fun setupViewShopButton(shop: Shop) {
        btnViewShop.setOnClickListener {
            Toast.makeText(requireContext(), "View Shop button clicked ", Toast.LENGTH_SHORT).show()
            Intent(requireContext(), MyShopActivity::class.java).also { intent ->
                intent.putExtra("shop", shop)
                intent.putExtra("barber", barber)
                startActivity(intent)
            }
        }

    }

    // ---------------------- Appointment Handling ----------------------

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadAcceptedAppointments() {
        barberViewModel.getAppointmentByBarberIdAndStatus("accepted", barber.uid)
        barberViewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            homeRequestsAdapter.updateData(appointments)
            recyclerView.adapter = homeRequestsAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cancelAppointment(appointmentId: String) {
        barberViewModel.updateAppointmentStatus(
            appointmentId = appointmentId,
            newStatus = "canceled",
            onSuccess = {
                Toast.makeText(requireContext(), "Appointment successfully canceled", Toast.LENGTH_SHORT).show()
                refreshUI()
            },
            onFailure = {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // ---------------------- Join Request Handling ----------------------

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendJoinRequest(shop: Shop) {
        val id = FirebaseFirestore.getInstance().collection("joinRequests").document().id
        val joinRequest = JoinRequest(
            id = id,
            experience = barber.experience,
            barberId = barber.uid,
            shopId = shop.id,
            date = LocalDate.now().toString(),
            status = "pending",
            shopOwnerId = shop.ownerId
        )
        barberViewModel.createJoinRequests(joinRequest) { success ->
            if (success) {
                refreshUI()
                Toast.makeText(requireContext(), "Join request sent to: ${shop.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to send join request", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
