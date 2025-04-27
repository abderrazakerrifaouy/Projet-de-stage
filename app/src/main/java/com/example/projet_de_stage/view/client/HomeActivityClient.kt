package com.example.projet_de_stage.view.client

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.fragment.fragmentClient.FragmentListBarberShop
import com.example.projet_de_stage.fragment.fragmentClient.HistoryFragmentClient
import com.example.projet_de_stage.fragment.fragmentClient.ProfileFragmentClient
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivityClient : AppCompatActivity() {
    private lateinit var customer : Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_client)

        customer = intent.getParcelableExtra<Customer>("customer")!!
        // Set up bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_shops -> {
                    loadFragment(FragmentListBarberShop())
                    true
                }
                R.id.nav_history -> {  // Changed from nav_appointments to nav_history
                    // Load history fragment
                    loadFragment(HistoryFragmentClient()) // Assuming you'll create this fragment
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragmentClient())
                    true
                }
                else -> false
            }
        }

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(FragmentListBarberShop())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        var bundle = Bundle()
        bundle.putParcelable("customer", customer)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}