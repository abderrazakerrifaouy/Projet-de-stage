package com.example.projet_de_stage.view.client

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.fragment.fragmentClient.FragmentListBarberShop
import com.example.projet_de_stage.fragment.fragmentClient.HistoryFragmentClient
import com.example.projet_de_stage.fragment.fragmentClient.ProfileFragmentClient
import com.example.projet_de_stage.view.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

/**
 * Home screen for the Customer user.
 * Provides navigation to shop list, appointment history, and profile.
 */
class HomeActivityClient : AppCompatActivity() {

    private lateinit var customer: Customer // Holds the customer object passed from login/register
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_client)

        // Retrieve Customer object from Intent
        customer = intent.getParcelableExtra("customer")!!
        toolbar = findViewById(R.id.toolbarClient)

        // Set up the toolbar with the customer's name
        setSupportActionBar(toolbar)

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


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        if (menu is MenuBuilder) {
            val menuBuilder = menu
            menuBuilder.setOptionalIconsVisible(true)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_language -> {
                showLanguageDialog()
                true
            }
            R.id.menu_theme -> {
                Toast.makeText(this, "Change Theme clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout -> {
                val prefs = this.getSharedPreferences("prefs", MODE_PRIVATE)
                prefs.edit() { putString("uid", "") }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Function to show a language selection dialog
    private fun showLanguageDialog() {
        val languages = arrayOf("العربية", "Français", "English")
        val languageCodes = arrayOf("ar", "fr", "en") // Add the language codes for Arabic, French, and English

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_language))  // This string is now translated
        builder.setItems(languages) { _, which ->
            val selectedLang = languageCodes[which]
            setLocale(selectedLang)  // Change locale when a language is selected
        }
        builder.show()
    }

    // Function to change the language and apply it to the app
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)

        // Adjust layout direction for RTL languages
        if (languageCode == "ar") {
            config.setLayoutDirection(locale)
        }

        resources.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language in SharedPreferences
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit() { putString("lang", languageCode) }

        // Recreate the activity to apply the new language settings
        recreate()
    }

    override fun attachBaseContext(newBase: Context?) {
        val prefs = newBase?.getSharedPreferences("prefs", MODE_PRIVATE)
        val lang = prefs?.getString("lang", "fr") ?: "fr"  // Default to French if no language is set
        val locale = Locale(lang)
        val config = Configuration()
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        // Adjust layout direction for RTL languages (like Arabic)
        val context = newBase?.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}
