package com.example.projet_de_stage.view.barberUser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.fragment.fragmentBarber.AppointmentsFragmentBareber
import com.example.projet_de_stage.fragment.fragmentBarber.BarberHome
import com.example.projet_de_stage.fragment.fragmentBarber.ProfileFragment
import com.example.projet_de_stage.viewModel.BarberViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch

class BarberActivityHome : AppCompatActivity() {
    private lateinit var barber: Barber
    private val barberViewModel = BarberViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber_home)
        // Replace with your actual layout file name

         barber = intent.getParcelableExtra<Barber>("barber")!!

        // Initialize toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.title = "مرحباً، ${barber.name}!"



        barberViewModel.listenToNewRealtimeAppointments(
            barberId = barber.uid,
            onNewAppointment = { appointment ->
                runOnUiThread {
                    showAppointmentBottomSheet(appointment)
                }
            },
            onError = { errorMessage ->
                Toast.makeText(this, "خطأ في الاتصال: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )




        // Initialize bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        lifecycleScope.launch {
            val shop = barberViewModel.getShopByBarber(barber)
            bottomNavigation.menu.findItem(R.id.nav_appointments).isVisible = shop != null

        }
        setupBottomNavigation(bottomNavigation)
        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(BarberHome())
        }
    }


    private fun setupBottomNavigation(bottomNav: BottomNavigationView) {
        bottomNav.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    loadFragment(BarberHome())
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.nav_appointments -> {
                    Toast.makeText(this, "Appointments clicked", Toast.LENGTH_SHORT).show()
                    loadFragment(AppointmentsFragmentBareber())
                    true
                }
                // Add more cases for other menu items
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val bundle = Bundle()
        bundle.putParcelable("barber", barber)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            if (addToBackStack) {
                addToBackStack(fragment::class.java.simpleName)
            }
            commit()
        }
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    private fun showAppointmentBottomSheet(appointment: Appointment) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_appointment, null)

        val serviceText = view.findViewById<TextView>(R.id.serviceTextView)
        val timeText = view.findViewById<TextView>(R.id.timeTextView)
        val acceptButton = view.findViewById<Button>(R.id.acceptButton)
        val rejectButton = view.findViewById<Button>(R.id.rejectButton)
        val pendingButton = view.findViewById<Button>(R.id.pendingButton)
        val NameCustomer = view.findViewById<TextView>(R.id.NameCustomer)
        val dateService = view.findViewById<TextView>(R.id.dateService)

        barberViewModel.loadCustomerById(appointment.clientId)
        barberViewModel.customer.observe(this) { customer ->
            NameCustomer.text = "العميل: ${customer?.name}"
        }

        dateService.text = "تاريخ الخدمة: ${appointment.date}"
        serviceText.text = "الخدمة: ${appointment.service}"
        timeText.text = "الوقت: ${appointment.time}"

        acceptButton.setOnClickListener {
            Toast.makeText(this, "تم قبول الموعد", Toast.LENGTH_SHORT).show()
            barberViewModel.updateAppointmentStatus(
                appointment.id, "accepted",
                onSuccess = { Toast.makeText(this, "تم تحديث الحالة", Toast.LENGTH_SHORT).show() },
                onFailure = { Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show() }
            )
            deleteAppointmentInRealTimeDatabase(appointment)
            bottomSheetDialog.dismiss()
        }

        rejectButton.setOnClickListener {
            Toast.makeText(this, "تم رفض الموعد", Toast.LENGTH_SHORT).show()
            barberViewModel.updateAppointmentStatus(
                appointment.id, "rejected",
                onSuccess = { Toast.makeText(this, "تم تحديث الحالة", Toast.LENGTH_SHORT).show() },
                onFailure = { Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show() }
            )
            deleteAppointmentInRealTimeDatabase(appointment)
            bottomSheetDialog.dismiss()
        }

        pendingButton.setOnClickListener {
            Toast.makeText(this, "تم وضع الموعد في الانتظار", Toast.LENGTH_SHORT).show()
            deleteAppointmentInRealTimeDatabase(appointment)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun deleteAppointmentInRealTimeDatabase(appointment: Appointment) {

        barberViewModel.deleteAppointmentInRealtimeDatabase(
            appointment.id,
            onSuccess = { check ->
                if (check) {
                    Toast.makeText(this, "تم حذف موعد بنجاح", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "خطأ في حذف موعد", Toast.LENGTH_SHORT).show()
                }
            })
    }


}