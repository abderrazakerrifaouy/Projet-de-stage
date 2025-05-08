package com.example.projet_de_stage.view.barberUser

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.BarberAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.viewModel.AdminViewModel

class MyShopActivity : AppCompatActivity() {
    private val adminViewModel = AdminViewModel()
    private lateinit var shop: Shop
    private lateinit var barber: Barber
    private lateinit var shopImageView: ImageView
    private lateinit var shopNameTextView: TextView
    private lateinit var shopAddressTextView: TextView
    private lateinit var barberRecyclerView: RecyclerView
    private lateinit var leaveShopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_shop)

        shop = intent.getParcelableExtra<Shop>("shop") ?: run {
            Toast.makeText(this, "Shop data not found", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        barber = intent.getParcelableExtra<Barber>("barber") ?: run {
            Toast.makeText(this, "Barber data not found", Toast.LENGTH_LONG).show()
            finish()
            return
        }


        shopImageView = findViewById(R.id.shopImageViewBarber)
        shopNameTextView = findViewById(R.id.shopNameTextViewBarber)
        shopAddressTextView = findViewById(R.id.shopAddressTextViewBarber)
        barberRecyclerView = findViewById(R.id.barberRecyclerViewBarber)
        leaveShopButton = findViewById(R.id.leaveShopButton)

        setupUI()
        setupLeaveShopButton()
    }

    private fun setupUI() {
        shopNameTextView.text = shop.name
        shopAddressTextView.text = shop.address

        // تحميل صورة المحل
        if (shop.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(shop.imageUrl)
                .placeholder(R.drawable.app_name)
                .into(shopImageView)
        }

        val barberAdapter = BarberAdapter(shop.barbers) {
            // حدث عند الضغط على حلاق (يمكن تركه فارغاً الآن)
        }

        barberRecyclerView.layoutManager = LinearLayoutManager(this)
        barberRecyclerView.adapter = barberAdapter
    }

    private fun setupLeaveShopButton() {
        leaveShopButton.setOnClickListener {
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
        finish()
    }
}
