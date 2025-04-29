package com.example.projet_de_stage.view.client

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.viewModel.ClientViewModel
import com.wdullaer.materialdatetimepicker.time.Timepoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.UUID

class RequestClient : AppCompatActivity() {
    private var selectedService: String = ""
    private val viewModel = ClientViewModel()
    private lateinit var date : String
    private lateinit var time : String


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
        val ivBarber = findViewById<ImageView>(R.id.ivBarber)

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
        barber.rating.toFloat().let { ratingBar.rating = it }


        if (shop.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(shop.imageUrl)
                .into(ivBarber)
        } else {
            ivBarber.setImageResource(shop.imageRes)
        }


        // Service selection
        rgServices.setOnCheckedChangeListener { _, checkedId ->
            selectedService = when (checkedId) {
                R.id.rbHaircut -> "حلاقة الرأس"
                R.id.rbBeard -> "حلاقة اللحية"
                R.id.rbBoth -> "حلاقة الرأس واللحية"
                else -> ""
            }
        }

        val blockedAppointments = listOf(
            Appointment(
                id = "1",
                clientId = "client1",
                time = "09:00",
                service = "حلاقة الرأس",
                status = "confirmed",
                date = "2025-04-30",
                shopId = "shop1",
                barberId = "barber1"
            ),
            Appointment(
                id = "2",
                clientId = "client2",
                time = "12:00",
                service = "حلاقة اللحية",
                status = "confirmed",
                date = "2025-04-30",
                shopId = "shop1",
                barberId = "barber2"
            ),
            Appointment(
                id = "3",
                clientId = "client3",
                time = "15:00",
                service = "حلاقة الرأس واللحية",
                status = "confirmed",
                date = "2025-04-05"
            )
        )

                        // Date picker
        btnSelectDate.setOnClickListener {
            showHourRangePicker(this, blockedAppointments) { selectedDate, selectedTime ->
                // استخدم التاريخ والوقت الذي اختاره

            }
        }

        // Book appointment
        btnBook.setOnClickListener {
            if (validateInputs()) {
                bookAppointment(shop, barber, client)
            }
        }
    }





    private fun validateInputs(): Boolean {
        if (selectedService.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار الخدمة", Toast.LENGTH_SHORT).show()
            return false
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار التاريخ", Toast.LENGTH_SHORT).show()
            return false
        }
        if (time.isEmpty()) {
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
            time = time,
            service = selectedService,
            status = "pending",
            date = date,
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



    @RequiresApi(Build.VERSION_CODES.O)
    fun showHourRangePicker(
        context: Context,
        blockedAppointments: List<Appointment>, // مواعيد محجوزة
        onSelected: (String, String) -> Unit
    ) {
        val today = LocalDate.now()

        val dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)

                // استخراج الساعات المحجوزة لهذا التاريخ
                val blockedHours = blockedAppointments
                    .filter { it.date == selectedDate.toString() }
                    .mapNotNull {
                        it.time.split(":").getOrNull(0)?.toIntOrNull()
                    }.toMutableSet()

                val now = LocalDateTime.now()
                if (selectedDate == today) {
                    // منع الساعات الماضية في هذا اليوم
                    val currentHour = now.hour
                    blockedHours.addAll((0 until currentHour).toList())
                }

                val availableHours = (9..18).filter { it !in blockedHours }

                if (availableHours.isEmpty()) {
                    Toast.makeText(context, "لا توجد ساعات متاحة في هذا اليوم", Toast.LENGTH_SHORT).show()
                    return@newInstance
                }

                val firstHour = availableHours.first()

                val tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                    { _, hourOfDay, _, _ ->
                        val formattedTime = String.format("%02d:00", hourOfDay)
                        onSelected(selectedDate.toString(), formattedTime)
                    },
                    firstHour, 0, true
                )

                val disabledTimepoints = blockedHours.map { Timepoint(it, 0) }.toTypedArray()
                if (disabledTimepoints.isNotEmpty()) {
                    tpd.setDisabledTimes(disabledTimepoints)
                }

                tpd.setTimeInterval(1, 60)
                tpd.show((context as AppCompatActivity).supportFragmentManager, "TimePickerDialog")
            },
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        )

        // منع الأيام السابقة
        dpd.minDate = Calendar.getInstance()

        dpd.show((context as AppCompatActivity).supportFragmentManager, "DatePickerDialog")
    }




}
