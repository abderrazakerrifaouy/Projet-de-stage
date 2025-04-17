package com.example.projet_de_stage.view.admin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterAdmin.ShopManagementPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AdminActivityHome : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admine)

        // إعداد ViewPager و TabLayout
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // إنشاء أدابتر للتبويبات
        val adapter = ShopManagementPagerAdapter(this)
        viewPager.adapter = adapter

        // ربط TabLayout مع ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "طلبات الحلاقة"
                1 -> "طلبات الانضمام"
                2 -> "محلاتي"
                3 -> "التقييمات"
                else -> null
            }
        }.attach()
    }
}