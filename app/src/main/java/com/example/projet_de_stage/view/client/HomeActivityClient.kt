package com.example.projet_de_stage.view.client

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.fragment.fragmentClient.FragmentListBarberShop
import com.example.projet_de_stage.fragment.fragmentClient.HistoryFragmentClient
import com.example.projet_de_stage.fragment.fragmentClient.ProfileFragmentClient
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Home screen for the Customer user.
 * Provides navigation to shop list, appointment history, and profile.
 */
class HomeActivityClient : AppCompatActivity() {

    private lateinit var customer: Customer // Holds the customer object passed from login/register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_client)

        // Retrieve Customer object from Intent
        customer = intent.getParcelableExtra("customer")!!

        // Temporary toast to show customer's name (can be removed in production)
        Toast.makeText(this, customer.name, Toast.LENGTH_SHORT).show()

        // Initialize BottomNavigationView
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_shops -> {
                    loadFragment(FragmentListBarberShop())
                    true
                }
                R.id.nav_history -> {
                    loadFragment(HistoryFragmentClient())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragmentClient())
                    true
                }
                else -> false
            }
        }

        // Load the default fragment (Shop list) only on first creation
        if (savedInstanceState == null) {
            loadFragment(FragmentListBarberShop())
        }
    }

    /**
     * Loads a fragment into the container and passes the customer object to it via arguments.
     *
     * @param fragment The fragment to be loaded.
     */
    private fun loadFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putParcelable("customer", customer)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
