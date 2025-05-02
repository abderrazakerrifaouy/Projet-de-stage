package com.example.projet_de_stage.view.barberUser

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.fragment.fragmentBarber.AppointmentsFragmentBarber
import com.example.projet_de_stage.fragment.fragmentBarber.BarberHome
import com.example.projet_de_stage.fragment.fragmentBarber.ProfileFragment
import com.example.projet_de_stage.viewModel.BarberViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch

/**
 * Main activity for barbers after login.
 * Handles navigation and incoming appointments.
 */
class BarberActivityHome : AppCompatActivity() {

    private lateinit var barber: Barber
    private val barberViewModel = BarberViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber_home)

        // Retrieve barber data passed from previous activity
        barber = intent.getParcelableExtra("barber")!!

        setupToolbar()
        observeNewAppointments()
        setupBottomNavigation(findViewById(R.id.bottom_navigation))

        // Load default home fragment
        if (savedInstanceState == null) {
            loadFragment(BarberHome())
        }
    }

    /**
     * Sets up the top app bar with greeting.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Welcome, ${barber.name}!"
    }

    /**
     * Listens to new real-time appointments and shows them in a bottom sheet.
     */
    private fun observeNewAppointments() {
        barberViewModel.listenToNewRealtimeAppointments(
            barberId = barber.uid,
            onNewAppointment = { appointment ->
                runOnUiThread {
                    showAppointmentBottomSheet(appointment)
                }
            },
            onError = { errorMessage ->
                Toast.makeText(this, "Connection error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    /**
     * Sets up the bottom navigation menu with fragment switching.
     */
    private fun setupBottomNavigation(bottomNav: BottomNavigationView) {
        lifecycleScope.launch {
            val shop = barberViewModel.getShopByBarber(barber)
            bottomNav.menu.findItem(R.id.nav_appointments).isVisible = shop != null
        }

        bottomNav.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(BarberHome())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.nav_appointments -> {
                    loadFragment(AppointmentsFragmentBarber())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Loads the selected fragment into the main container.
     * Passes the barber as argument to fragment.
     */
    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val bundle = Bundle().apply {
            putParcelable("barber", barber)
        }
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            if (addToBackStack) addToBackStack(fragment::class.java.simpleName)
            commit()
        }
    }

    /**
     * Displays the appointment in a bottom sheet with action buttons.
     */
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    private fun showAppointmentBottomSheet(appointment: Appointment) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_appointment, null)

        val serviceText = view.findViewById<TextView>(R.id.serviceTextView)
        val timeText = view.findViewById<TextView>(R.id.timeTextView)
        val acceptButton = view.findViewById<Button>(R.id.acceptButton)
        val rejectButton = view.findViewById<Button>(R.id.rejectButton)
        val pendingButton = view.findViewById<Button>(R.id.pendingButton)
        val customerNameTextView = view.findViewById<TextView>(R.id.NameCustomer)
        val serviceDateTextView = view.findViewById<TextView>(R.id.dateService)

        barberViewModel.loadCustomerById(appointment.clientId)
        barberViewModel.customer.observe(this) { customer ->
            customerNameTextView.text = "Customer: ${customer?.name}"
        }

        serviceDateTextView.text = "Service Date: ${appointment.date}"
        serviceText.text = "Service: ${appointment.service}"
        timeText.text = "Time: ${appointment.time}"

        acceptButton.setOnClickListener {
            updateAppointmentStatus(appointment, "accepted", "Appointment accepted")
            bottomSheetDialog.dismiss()
        }

        rejectButton.setOnClickListener {
            updateAppointmentStatus(appointment, "rejected", "Appointment rejected")
            bottomSheetDialog.dismiss()
        }

        pendingButton.setOnClickListener {
            Toast.makeText(this, "Appointment put on hold", Toast.LENGTH_SHORT).show()
            deleteAppointmentInRealTimeDatabase(appointment)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    /**
     * Updates the appointment status and removes it from real-time DB.
     */
    private fun updateAppointmentStatus(appointment: Appointment, status: String, toastMessage: String) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        barberViewModel.updateAppointmentStatus(
            appointment.id, status,
            onSuccess = {
                Toast.makeText(this, "Status updated", Toast.LENGTH_SHORT).show()
            },
            onFailure = {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
        deleteAppointmentInRealTimeDatabase(appointment)
    }

    /**
     * Deletes the appointment from Firebase Realtime Database.
     */
    private fun deleteAppointmentInRealTimeDatabase(appointment: Appointment) {
        barberViewModel.deleteAppointmentInRealtimeDatabase(
            appointment.id,
            onSuccess = { check ->
                if (check) {
                    Toast.makeText(this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error deleting appointment", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
