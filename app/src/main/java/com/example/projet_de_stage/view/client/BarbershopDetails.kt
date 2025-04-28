package com.example.projet_de_stage.view.client

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.BarberAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import kotlin.jvm.java
import kotlin.let

class BarbershopDetails : AppCompatActivity() {
    private lateinit var ivShopBanner: ImageView
    private lateinit var tvShopName: TextView
    private lateinit var rvBarbers: RecyclerView
    private lateinit var barberAdapter: BarberAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barbershop_details)

        ivShopBanner = findViewById(R.id.ivShopBanner)
        tvShopName = findViewById(R.id.tvShopName)
        rvBarbers = findViewById(R.id.rvBarbers)

        val shop = intent.getParcelableExtra("SHOP_DATA", Shop::class.java)
        val client = intent.getParcelableExtra("CLIENT_DATA", Customer::class.java)
        shop?.let { setupUI(it , client) }
    }

    private fun setupUI(shop: Shop, client: Customer?) {
        ivShopBanner.setImageResource(shop.imageRes)
        tvShopName.text = shop.name

        barberAdapter = BarberAdapter(shop.barbers) { selectedBarber ->
            val intent = Intent(this, RequestClient::class.java)
            intent.putExtra("SHOP_DATA", shop)
            intent.putExtra("BARBER_DATA", selectedBarber)
            intent.putExtra("CLIENT_DATA", client)
            startActivity(intent)
        }

        rvBarbers.layoutManager = LinearLayoutManager(this)
        rvBarbers.adapter = barberAdapter
    }


}