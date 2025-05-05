package com.example.projet_de_stage.view.admin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterAdmin.BarberAdapterShop
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.viewModel.AdminViewModel


class BarberListActivity : AppCompatActivity() {
    private val adminViewModel = AdminViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_details)

        val recyclerView = findViewById<RecyclerView>(R.id.barberRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val shop = getSampleShop()
        val adapter = BarberAdapterShop(shop.barbers) { barber ->

            Toast.makeText(this, "Removed ${barber.name}", Toast.LENGTH_SHORT).show()
            shop.barbers.remove(barber)
            adminViewModel.deleteBarberFromShop(
                shopId = shop.id,
                barberId = barber.uid,
                onSuccess = {
                    Toast.makeText(this, "Removed ${barber.name}", Toast.LENGTH_SHORT).show()
                },
                onFailure = { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            )

        }
        recyclerView.adapter = adapter

        findViewById<TextView>(R.id.shopNameTextView).text = shop.name
        findViewById<TextView>(R.id.shopAddressTextView).text = shop.address
        val imageView = findViewById<ImageView>(R.id.shopImageView)
        if (shop.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(shop.imageUrl)
                .into(imageView)
            imageView.clipToOutline = true
        } else {
            imageView.setImageResource(shop.imageRes)
        }
    }

    private fun getSampleShop(): Shop {
        return intent.getParcelableExtra("shop")!!
    }
}
