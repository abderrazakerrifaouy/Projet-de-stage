package com.example.projet_de_stage.view.barberUser

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.fragment.fragmentBarber.AppointmentsFragmentBareber
import com.example.projet_de_stage.fragment.fragmentBarber.BarberHome
import com.example.projet_de_stage.fragment.ProfileFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class BarberActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barber_home) // Replace with your actual layout file name

        // Initialize toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupBottomNavigation(bottomNavigation)

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(BarberHome())
        }
    }

    private fun setupBottomNavigation(bottomNav: BottomNavigationView) {
        bottomNav.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    loadFragment(BarberHome())
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.nav_appointments -> {
                    Toast.makeText(this, "Appointments clicked", Toast.LENGTH_SHORT).show()
                    loadFragment(AppointmentsFragmentBareber())
                    true
                }
                // Add more cases for other menu items
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            if (addToBackStack) {
                addToBackStack(fragment::class.java.simpleName)
            }
            commit()
        }
    }


}