package com.example.projet_de_stage.view.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterAdmin.ShopManagementPagerAdapter
import com.example.projet_de_stage.data.ShopOwner
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * AdminActivityHome
 *
 * This activity represents the main screen for the admin (ShopOwner) where they can manage:
 * 1. Barber requests
 * 2. Join requests
 * 3. Their shops
 *
 * It uses ViewPager2 with TabLayout to switch between the sections.
 */
class AdminActivityHome : AppCompatActivity() {

    // Logged-in shop owner passed from the previous activity
    private lateinit var shopOwner: ShopOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admine)

        // Retrieve the ShopOwner object from the intent
        shopOwner = intent.getParcelableExtra<ShopOwner>("shopOwner")!!

        // Initialize UI components
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Set up ViewPager adapter
        val adapter = ShopManagementPagerAdapter(this, shopOwner)
        viewPager.adapter = adapter

        // Link TabLayout with ViewPager using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Barber Requests"      // Previously: طلبات الحلاقة
                1 -> "Join Requests"        // Previously: طلبات الانضمام
                2 -> "My Shops"             // Previously: محلاتي
                else -> null
            }
        }.attach()
    }
}
