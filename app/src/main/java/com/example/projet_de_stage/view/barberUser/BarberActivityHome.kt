package com.example.projet_de_stage.view.barberUser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.fragment.fragmentBarber.AppointmentsFragmentBarber
import com.example.projet_de_stage.fragment.fragmentBarber.BarberHome
import com.example.projet_de_stage.fragment.fragmentBarber.ProfileFragment
import com.example.projet_de_stage.view.LoginActivity
import com.example.projet_de_stage.viewModel.BarberViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch
import java.util.Locale

class BarberActivityHome : AppCompatActivity() {

    private lateinit var barber: Barber
    private val barberViewModel = BarberViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber_home)

        barber = intent.getParcelableExtra<Barber>("barber")!!



        setupToolbar()
        observeNewAppointments()
        setupBottomNavigation(findViewById(R.id.bottom_navigation))

        if (savedInstanceState == null) {
            loadFragment(BarberHome())
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.welcome_message, barber.name)
    }

    private fun observeNewAppointments() {
        barberViewModel.listenToNewRealtimeAppointments(
            barberId = barber.uid,
            onNewAppointment = { appointment ->
                runOnUiThread {
                    showAppointmentBottomSheet(appointment)
                }
            },
            onError = { errorMessage ->
                Toast.makeText(this, getString(R.string.connection_error, errorMessage), Toast.LENGTH_SHORT).show()
            }
        )
    }

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

    @SuppressLint("SetTextI18n", "MissingInflatedId", "InflateParams")
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
            customerNameTextView.text = getString(R.string.customer_name, customer?.name)
        }

        serviceDateTextView.text = getString(R.string.service_date, appointment.date)
        serviceText.text = getString(R.string.service, appointment.service)
        timeText.text = getString(R.string.time, appointment.time)

        acceptButton.setOnClickListener {
            updateAppointmentStatus(appointment, "accepted", getString(R.string.appointment_accepted))
            bottomSheetDialog.dismiss()
        }

        rejectButton.setOnClickListener {
            updateAppointmentStatus(appointment, "rejected", getString(R.string.appointment_rejected))
            bottomSheetDialog.dismiss()
        }

        pendingButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.appointment_on_hold), Toast.LENGTH_SHORT).show()
            deleteAppointmentInRealTimeDatabase(appointment)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun updateAppointmentStatus(appointment: Appointment, status: String, toastMessage: String) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        barberViewModel.updateAppointmentStatus(
            appointment.id, status,
            onSuccess = {
                Toast.makeText(this, getString(R.string.status_updated), Toast.LENGTH_SHORT).show()
            },
            onFailure = {
                Toast.makeText(this, getString(R.string.error_message, it.message), Toast.LENGTH_SHORT).show()
            }
        )
        deleteAppointmentInRealTimeDatabase(appointment)
    }

    private fun deleteAppointmentInRealTimeDatabase(appointment: Appointment) {
        barberViewModel.deleteAppointmentInRealtimeDatabase(
            appointment.id,
            onSuccess = { check ->
                if (check) {
                    Toast.makeText(this, getString(R.string.appointment_deleted), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.appointment_delete_error), Toast.LENGTH_SHORT).show()
                }
            })
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        if (menu is MenuBuilder) {
            val menuBuilder = menu
            menuBuilder.setOptionalIconsVisible(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_language -> {
                showLanguageDialog()
                true
            }
            R.id.menu_theme -> {
                Toast.makeText(this, "Change Theme clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout -> {
                val prefs = this.getSharedPreferences("prefs", MODE_PRIVATE)
                prefs.edit() { putString("uid", "") }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Function to show a language selection dialog
    private fun showLanguageDialog() {
        val languages = arrayOf("العربية", "Français", "English")
        val languageCodes = arrayOf("ar", "fr", "en") // Add the language codes for Arabic, French, and English

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_language))  // This string is now translated
        builder.setItems(languages) { _, which ->
            val selectedLang = languageCodes[which]
            setLocale(selectedLang)  // Change locale when a language is selected
        }
        builder.show()
    }

    // Function to change the language and apply it to the app
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)

        // Adjust layout direction for RTL languages
        if (languageCode == "ar") {
            config.setLayoutDirection(locale)
        }

        resources.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language in SharedPreferences
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit().putString("lang", languageCode).apply()

        // Recreate the activity to apply the new language settings
        recreate()
    }

    override fun attachBaseContext(newBase: Context?) {
        val prefs = newBase?.getSharedPreferences("prefs", MODE_PRIVATE)
        val lang = prefs?.getString("lang", "fr") ?: "fr"  // Default to French if no language is set
        val locale = Locale(lang)
        val config = Configuration()
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        // Adjust layout direction for RTL languages (like Arabic)
        val context = newBase?.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}
