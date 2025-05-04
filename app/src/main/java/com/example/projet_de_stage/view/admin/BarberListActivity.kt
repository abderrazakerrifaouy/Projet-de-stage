package com.example.projet_de_stage.view.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterAdmin.BarberAdapterShop
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop


class BarberListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_details)

        val recyclerView = findViewById<RecyclerView>(R.id.barberRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val shop = getSampleShop() // استخدم البيانات التي عندك
        val adapter = BarberAdapterShop(shop.barbers) { barber ->
            Toast.makeText(this, "Removed ${barber.name}", Toast.LENGTH_SHORT).show()
            // Logic for removal if needed
        }
        recyclerView.adapter = adapter

        findViewById<TextView>(R.id.shopNameTextView).text = shop.name
        findViewById<TextView>(R.id.shopAddressTextView).text = shop.address
        findViewById<ImageView>(R.id.shopImageView).setImageResource(shop.imageRes)
    }

    private fun getSampleShop(): Shop {
        return Shop(
            id = "1",
            name = "The Dapper Cut",
            address = "123 Main Street, Anytown, USA",
            barbers = mutableListOf(
                Barber("1", "Alex Johnson", "5 Years Experience", "", "", "", "", "", R.drawable.my_profile, 4.8f, 120),
                Barber("2", "Maria Garcia", "Senior Barber", "", "", "", "", "", R.drawable.my_profile, 4.9f, 250),
                Barber("3", "Sam Chen", "3 Years Experience", "", "", "", "", "", R.drawable.my_profile, 4.5f, 85)
            )
        )
    }
}
