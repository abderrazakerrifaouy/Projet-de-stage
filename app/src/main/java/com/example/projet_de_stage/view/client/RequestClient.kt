package com.example.projet_de_stage.view.client

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop
import java.util.Calendar
import kotlin.isInitialized
import kotlin.jvm.java
import kotlin.let
import kotlin.text.format
import kotlin.text.isEmpty
import kotlin.to

class RequestClient : AppCompatActivity() {
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private var selectedService: String = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_client)

        // Initialize UI components
        val tvShopName = findViewById<TextView>(R.id.tvShopName)
        val tvBarberName = findViewById<TextView>(R.id.tvBarberName)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val btnSelectDate = findViewById<Button>(R.id.btnSelectDate)
        val btnSelectTime = findViewById<Button>(R.id.btnSelectTime)
        val btnBook = findViewById<Button>(R.id.btnBook)
        val rgServices = findViewById<RadioGroup>(R.id.rgServices)

        // Get data from intent
        val shop = intent.getParcelableExtra("SHOP_DATA", Shop::class.java)
        val barber = intent.getParcelableExtra("BARBER_DATA", Barber::class.java)

        // Set shop and barber info
        tvShopName.text = shop?.name
        tvBarberName.text = barber?.name
        barber?.rating?.toFloat()?.let { ratingBar.rating = it }

        // Set up service selection
        rgServices.setOnCheckedChangeListener { _, checkedId ->
            selectedService = when (checkedId) {
                R.id.rbHaircut -> "حلاقة الرأس"
                R.id.rbBeard -> "حلاقة اللحية"
                R.id.rbBoth -> "حلاقة الرأس واللحية"
                else -> ""
            }
        }

        // Date picker
        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        // Time picker
        btnSelectTime.setOnClickListener {
            showTimePicker()
        }

        // Book appointment
        btnBook.setOnClickListener {
            if (validateInputs()) {
                bookAppointment(shop, barber)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                findViewById<Button>(R.id.btnSelectDate).text = selectedDate
            },
            year,
            month,
            day
        )

        // Disable past dates
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                findViewById<Button>(R.id.btnSelectTime).text = selectedTime
            },
            hour,
            minute,
            true // 24-hour format
        )
        timePicker.show()
    }

    private fun validateInputs(): Boolean {
        if (selectedService.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار الخدمة", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!this::selectedDate.isInitialized) {
            Toast.makeText(this, "الرجاء اختيار التاريخ", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!this::selectedTime.isInitialized) {
            Toast.makeText(this, "الرجاء اختيار الوقت", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun bookAppointment(shop: Shop?, barber: Barber?) {
        // Get current user ID (you'll need to implement your own user management)
//        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
          val userId = 0



        val appointment = hashMapOf(
            "userId" to userId,
            "shopId" to shop?.id,
            "barberId" to barber?.id,
            "service" to selectedService,
            "date" to selectedDate,
            "time" to selectedTime,
            "status" to "pending", // pending, confirmed, completed, cancelled
            "createdAt" to System.currentTimeMillis()
        )

        // Add to Firestore

    }
}