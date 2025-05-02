package com.example.projet_de_stage.view.client

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.viewModel.ClientViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Activity for customers to book an appointment with a barber in a shop.
 */
class RequestClient : AppCompatActivity() {

    private var selectedService: String = ""
    private val viewModel = ClientViewModel()
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var blockedAppointments: MutableList<Appointment>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_client)

        val tvShopName = findViewById<TextView>(R.id.tvShopName)
        val tvBarberName = findViewById<TextView>(R.id.tvBarberName)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val btnSelectDate = findViewById<Button>(R.id.btnSelectDate)
        val btnBook = findViewById<Button>(R.id.btnBook)
        val rgServices = findViewById<RadioGroup>(R.id.rgServices)
        val ivBarber = findViewById<ImageView>(R.id.ivBarber)

        val shop = intent.getParcelableExtra("SHOP_DATA", Shop::class.java)
        val barber = intent.getParcelableExtra("BARBER_DATA", Barber::class.java)
        val client = intent.getParcelableExtra("CLIENT_DATA", Customer::class.java)

        // Validate input data from intent
        if (shop == null || barber == null || client == null) {
            Toast.makeText(this, "Invalid data received!", Toast.LENGTH_SHORT).show() // CHANGED
            finish()
            return
        }

        setupUI(shop, barber)
        setupServiceSelection(rgServices)

        if (!isInternetAvailable()) {
            showNoInternetDialog()
            return
        }

        blockedAppointments = mutableListOf()
        loadAppointments(barber.uid)

        btnSelectDate.setOnClickListener {
            showHourRangePicker(this, blockedAppointments.filter {
                it.status == "pending" || it.status == "accepted"
            }) { selectedDate, selectedTime ->
                date = selectedDate
                time = selectedTime
            }
        }

        btnBook.setOnClickListener {
            if (validateInputs()) {
                bookAppointment(shop, barber, client)
            }
        }
    }

    /**
     * Load barber/shop info into the UI
     */
    private fun setupUI(shop: Shop, barber: Barber) {
        findViewById<TextView>(R.id.tvShopName).text = shop.name
        findViewById<TextView>(R.id.tvBarberName).text = barber.name
        findViewById<RatingBar>(R.id.ratingBar).rating = barber.rating

        val ivBarber = findViewById<ImageView>(R.id.ivBarber)
        if (shop.imageUrl.isNotEmpty()) {
            Glide.with(this).load(shop.imageUrl).into(ivBarber)
        } else {
            ivBarber.setImageResource(shop.imageRes)
        }
    }

    /**
     * Setup service selection logic for radio group
     */
    private fun setupServiceSelection(rgServices: RadioGroup) {
        rgServices.setOnCheckedChangeListener { _, checkedId ->
            selectedService = when (checkedId) {
                R.id.rbHaircut -> "Haircut"
                R.id.rbBeard -> "Beard Trim"
                R.id.rbBoth -> "Haircut & Beard"
                else -> ""
            }
        }
    }

    /**
     * Validate that all required fields are selected
     */
    private fun validateInputs(): Boolean {
        if (selectedService.isEmpty()) {
            Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show() // CHANGED
            return false
        }
        if (!::date.isInitialized || date.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show() // CHANGED
            return false
        }
        if (!::time.isInitialized || time.isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show() // CHANGED
            return false
        }
        return true
    }

    /**
     * Book a new appointment and send to backend
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun bookAppointment(shop: Shop, barber: Barber, client: Customer) {
        val appointment = Appointment(
            id = UUID.randomUUID().toString(),
            clientId = client.uid,
            time = time,
            service = selectedService,
            status = "pending",
            date = date,
            shopId = shop.id,
            barberId = barber.uid
        )

        lifecycleScope.launch {
            viewModel.addAppointment(appointment,
                onSuccess = {
                    Toast.makeText(this@RequestClient, "Appointment booked successfully", Toast.LENGTH_SHORT).show() // CHANGED
                    finish()
                },
                onFailure = { e ->
                    Toast.makeText(this@RequestClient, "Booking failed: ${e.message}", Toast.LENGTH_SHORT).show() // CHANGED
                }
            )
        }
    }

    /**
     * Load blocked appointments for selected barber
     */
    private fun loadAppointments(barberId: String) {
        try {
            viewModel.getAppointmentsByBarberId(barberId)
            viewModel.appointmentsDate.observe(this) { list ->
                blockedAppointments.clear()
                blockedAppointments.addAll(list)
            }
            viewModel.error.observe(this) { e ->
                e?.let {
                    Toast.makeText(this, "Error loading appointments: $e", Toast.LENGTH_SHORT).show() // CHANGED
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error fetching appointments: ${e.message}", Toast.LENGTH_SHORT).show() // CHANGED
        }
    }

    /**
     * Display date and time pickers with disabled time slots
     */
    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showHourRangePicker(
        context: Context,
        blockedAppointments: List<Appointment>,
        onSelected: (String, String) -> Unit
    ) {
        val today = LocalDate.now()

        val dpd = DatePickerDialog.newInstance(
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val blockedHours = blockedAppointments
                    .filter { it.date == selectedDate.toString() }
                    .mapNotNull { it.time.split(":").getOrNull(0)?.toIntOrNull() }
                    .toMutableSet()

                val now = LocalDateTime.now()
                if (selectedDate == today) {
                    val currentHour = now.hour
                    blockedHours.addAll(0 until currentHour)
                }

                val availableHours = (9..18).filterNot { it in blockedHours }
                if (availableHours.isEmpty()) {
                    Toast.makeText(context, "No available times for this day", Toast.LENGTH_SHORT).show() // CHANGED
                    return@newInstance
                }

                val firstHour = availableHours.first()
                val tpd = TimePickerDialog.newInstance(
                    { _, hourOfDay, _, _ ->
                        val formattedTime = String.format("%02d:00", hourOfDay)
                        onSelected(selectedDate.toString(), formattedTime)
                    },
                    firstHour, 0, true
                )

                tpd.setDisabledTimes(blockedHours.map { Timepoint(it, 0) }.toTypedArray())
                tpd.setTimeInterval(1, 60)
                tpd.show((context as AppCompatActivity).supportFragmentManager, "TimePickerDialog")
            },
            today.year, today.monthValue - 1, today.dayOfMonth
        )

        dpd.minDate = Calendar.getInstance()
        dpd.show((context as AppCompatActivity).supportFragmentManager, "DatePickerDialog")
    }

    /**
     * Check if device has internet access
     */
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Show a dialog when no internet is detected
     */
    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection") // CHANGED
            .setMessage("Please check your connection and try again.") // CHANGED
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
