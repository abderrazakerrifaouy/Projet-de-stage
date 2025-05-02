package com.example.projet_de_stage.view.client

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.BarberAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop

/**
 * Displays the details of a selected barbershop including its name, image, and list of barbers.
 * From here, the client can choose a barber to book an appointment.
 */
class BarbershopDetails : AppCompatActivity() {

    private lateinit var ivShopBanner: ImageView
    private lateinit var tvShopName: TextView
    private lateinit var rvBarbers: RecyclerView
    private lateinit var barberAdapter: BarberAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barbershop_details)

        // Initialize UI components
        ivShopBanner = findViewById(R.id.ivShopBanner)
        tvShopName = findViewById(R.id.tvShopName)
        rvBarbers = findViewById(R.id.rvBarbers)

        // Retrieve data passed via intent
        val shop = intent.getParcelableExtra("SHOP_DATA", Shop::class.java)
        val client = intent.getParcelableExtra("CLIENT_DATA", Customer::class.java)

        // Setup UI only if shop is not null
        shop?.let { setupUI(it, client) }
    }

    /**
     * Sets up the shop image, name, and the list of barbers.
     * @param shop The shop to display
     * @param client The current logged-in client
     */
    private fun setupUI(shop: Shop, client: Customer?) {
        // Load shop banner image if available
        if (shop.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(shop.imageUrl)
                .into(ivShopBanner)
        } else {
            ivShopBanner.setImageResource(R.drawable.app_name) // Default image fallback
        }

        tvShopName.text = shop.name

        // Initialize barber adapter with click listener to navigate to RequestClient activity
        barberAdapter = BarberAdapter(shop.barbers) { selectedBarber ->
            val intent = Intent(this, RequestClient::class.java).apply {
                putExtra("SHOP_DATA", shop)
                putExtra("BARBER_DATA", selectedBarber)
                putExtra("CLIENT_DATA", client)
            }
            startActivity(intent)
        }

        rvBarbers.layoutManager = LinearLayoutManager(this)
        rvBarbers.adapter = barberAdapter
    }
}
