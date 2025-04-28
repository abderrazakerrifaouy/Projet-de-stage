package com.example.projet_de_stage.view.client

import android.annotation.SuppressLint
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
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.viewModel.ClientViewModel
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID

class RequestClient : AppCompatActivity() {
    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private var selectedService: String = ""
    private val viewModel = ClientViewModel()

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
        val client = intent.getParcelableExtra("CLIENT_DATA", Customer::class.java)

        Toast.makeText(this , " client name : ${client?.name} " , Toast.LENGTH_SHORT).show()
        if (shop == null || barber == null || client == null) {
            Toast.makeText(this, "خطأ في البيانات!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set shop and barber info
        tvShopName.text = shop.name
        tvBarberName.text = barber.name
        barber.rating?.toFloat()?.let { ratingBar.rating = it }

        // Service selection
        rgServices.setOnCheckedChangeListener { _, checkedId ->
            selectedService = when (checkedId) {
                R.id.rbHaircut -> "حلاقة الرأس"
                R.id.rbBeard -> "حلاقة اللحية"
                R.id.rbBoth -> "حلاقة الرأس واللحية"
                else -> ""
            }
        }

        // Date picker
        btnSelectDate.setOnClickListener { showDatePicker() }

        // Time picker
        btnSelectTime.setOnClickListener { showTimePicker() }

        // Book appointment
        btnBook.setOnClickListener {
            if (validateInputs()) {
                bookAppointment(shop, barber, client)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                findViewById<Button>(R.id.btnSelectDate).text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    @SuppressLint("DefaultLocale")
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                findViewById<Button>(R.id.btnSelectTime).text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    private fun validateInputs(): Boolean {
        if (selectedService.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار الخدمة", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedDate.isNullOrEmpty()) {
            Toast.makeText(this, "الرجاء اختيار التاريخ", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedTime.isNullOrEmpty()) {
            Toast.makeText(this, "الرجاء اختيار الوقت", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bookAppointment(shop: Shop, barber: Barber, client: Customer) {
        val appointment = Appointment(
            id = UUID.randomUUID().toString(),
            clientId = client.uid,
            time = selectedTime ?: "",
            service = selectedService,
            status = "قيد الانتظار",
            date = LocalDate.parse(selectedDate),
            shopId = shop.id,
            barberId = barber.uid
        )

        viewModel.addAppointment(
            appointment = appointment,
            onSuccess = {
                Toast.makeText(this, "تم حجز الموعد بنجاح", Toast.LENGTH_SHORT).show()
                finish() // رجوع بعد النجاح
            },
            onFailure = { e ->
                Toast.makeText(this, "خطأ أثناء الحجز: ${e.message}", Toast.LENGTH_SHORT).show()
            },
            onConflict = {
                Toast.makeText(this, "لديك موعد آخر مع هذا الحلاق في نفس التوقيت", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
