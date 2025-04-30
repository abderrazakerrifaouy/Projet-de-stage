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

class RequestClient : AppCompatActivity() {
    private var selectedService: String = ""
    private val viewModel = ClientViewModel()
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var blockedAppointments: MutableList<Appointment>

    @SuppressLint("SuspiciousIndentation")
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

        if (shop == null || barber == null || client == null) {
            Toast.makeText(this, "خطأ في البيانات!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

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

        rgServices.setOnCheckedChangeListener { _, checkedId ->
            selectedService = when (checkedId) {
                R.id.rbHaircut -> "حلاقة الرأس"
                R.id.rbBeard -> "حلاقة اللحية"
                R.id.rbBoth -> "حلاقة الرأس واللحية"
                else -> ""
            }
        }

        if (!isInternetAvailable()) {
            showNoInternetDialog()
            return
        }

        blockedAppointments = mutableListOf()

        // باستخدام Coroutine لجلب البيانات من Firebase بشكل غير متزامن

            loadAppointments(barber.uid)


        btnSelectDate.setOnClickListener {
            showHourRangePicker(this, blockedAppointments) { selectedDate, selectedTime ->
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

    private fun validateInputs(): Boolean {
        if (selectedService.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار الخدمة", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!::date.isInitialized || date.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار التاريخ", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!::time.isInitialized || time.isEmpty()) {
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

        lifecycleScope.launch {
            viewModel.addAppointment(appointment ,
                onSuccess = {
                    Toast.makeText(this@RequestClient, "تم حجز الموعد بنجاح", Toast.LENGTH_SHORT).show()
                    finish()
                } ,
                onFailure = { e ->
                    Toast.makeText(this@RequestClient, "خطأ أثناء الحجز: ${e.message}", Toast.LENGTH_SHORT).show()

                } ,
                onConflict = {
                    Toast.makeText(this@RequestClient, "خطأ أثناء الحجز: ", Toast.LENGTH_SHORT).show()

                }
                )

        }
    }

    private  fun loadAppointments(barberId: String) {

        try {
                viewModel.getAppointmentsByIdBarebr(barberId)
                viewModel.appointmentsDate.observe(this@RequestClient){ list ->
                    blockedAppointments.clear()
                            blockedAppointments.addAll(list)
                }
            viewModel.error.observe(this) { e ->
                if (e != null) {
                    Toast.makeText(this@RequestClient, "خطأ أثناء تحميل المواعيد: ${e}", Toast.LENGTH_SHORT).show()
                }
            }

            } catch (e: Exception) {

                    Toast.makeText(this@RequestClient, "خطأ في جلب المواعيد: ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showHourRangePicker(
        context: Context,
        blockedAppointments: List<Appointment>, // مواعيد محجوزة
        onSelected: (String, String) -> Unit
    ) {
        val today = LocalDate.now()

        val dpd = DatePickerDialog.newInstance(
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

                val tpd = TimePickerDialog.newInstance(
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

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("لا يوجد اتصال")
            .setMessage("الرجاء التأكد من اتصالك بالإنترنت ثم حاول مرة أخرى.")
            .setPositiveButton("موافق") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
