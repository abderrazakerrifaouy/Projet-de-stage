package com.example.projet_de_stage.fragment.fragmentBarber

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.projet_de_stage.viewModel.BarberViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * BarberHome Fragment that displays the barber's information and appointment requests.
 */
class BarberHome : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var caretShopOn: CardView
    private lateinit var caretShopOff: CardView
    private lateinit var tvShopStatus: TextView
    private lateinit var tvShopName: TextView
    private lateinit var tvShopLocation: TextView
    private lateinit var ivShopImage: ImageView

    private val barberViewModel = BarberViewModel()
    private lateinit var barber: Barber
    private var shop: Shop? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private val homeRequestsAdapter = HomeRequestsAdapter { appointment ->
        barberViewModel.updateAppointmentStatus(
            appointmentId = appointment.id,
            newStatus = "canceled",
            onSuccess = {
                Toast.makeText(requireContext(), "Appointment successfully canceled", Toast.LENGTH_SHORT).show()
                refreshUI() // Refresh the UI after cancellation
            },
            onFailure = {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val shopAdapterBarber = ShopAdapterBarber { shop ->
        onRequestJoinShop(shop) // Handle join shop request
    }

    /**
     * Called when the fragment view is created. Initializes the UI components.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_barber_home, container, false)

        recyclerView = view.findViewById(R.id.rvTodayAppointments)
        caretShopOn = view.findViewById(R.id.shopDetailsLayoutOn)
        caretShopOff = view.findViewById(R.id.shopDetailsLayoutOff) // Corrected variable name
        tvShopStatus = view.findViewById(R.id.tvShopStatus)
        tvShopName = view.findViewById(R.id.tvShopName)
        tvShopLocation = view.findViewById(R.id.tvShopLocation)
        ivShopImage = view.findViewById(R.id.ivShopImage)

        barber = arguments?.getParcelable("barber") ?: throw IllegalArgumentException("Barber not found")

        return view
    }

    /**
     * Called when the fragment's view is fully created. Triggers UI refresh.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshUI() // Refresh the UI with the latest data
    }

    /**
     * Refreshes the UI by loading barber's shop details or available shops for joining.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshUI() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            shop = barberViewModel.getShopByBarber(barber)
            setupUI() // Setup the UI based on whether the barber is assigned to a shop
        }
    }

    /**
     * Sets up the UI based on the barber's shop status (joined or not).
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun setupUI() {
        if (shop == null) {
            // Barber is not assigned to any shop
            showNoShopUI()
        } else {
            // Barber is assigned to a shop
            showShopDetailsUI(shop)
        }
    }

    /**
     * Displays UI when the barber is not assigned to any shop.
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun showNoShopUI() {
        caretShopOn.visibility = View.GONE
        caretShopOff.visibility = View.VISIBLE
        tvShopStatus.text = "Not assigned to any shop yet"

        val allShops = barberViewModel.getAllShops()
        shopAdapterBarber.updateData(allShops.filter { it.barbers.size < it.numberOfBarbers })
        recyclerView.adapter = shopAdapterBarber
    }

    /**
     * Displays UI when the barber is assigned to a shop.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showShopDetailsUI(shop: Shop?) {
        caretShopOn.visibility = View.VISIBLE
        caretShopOff.visibility = View.GONE

        tvShopName.text = shop?.name
        tvShopLocation.text = shop?.address
        if (shop?.imageUrl?.isNotEmpty() == true) {
            Glide.with(requireContext())
                .load(shop.imageUrl)
                .into(ivShopImage)
        } else {
            ivShopImage.setImageResource(R.drawable.my_profile)
        }

        // Load the accepted appointments for the barber
        barberViewModel.getAppointmentByBarberIdAndStatus("accepted", barber.uid)

        // Observe the appointments live data and update the adapter when data changes
        barberViewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            homeRequestsAdapter.updateData(appointments)
            recyclerView.adapter = homeRequestsAdapter
        }
    }

    /**
     * Handles the join request for a shop. Creates a JoinRequest object and submits it.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onRequestJoinShop(shop: Shop) {
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
                refreshUI() // Refresh UI after the join request is sent
                Toast.makeText(requireContext(), "Join request sent to: ${shop.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to send join request", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
