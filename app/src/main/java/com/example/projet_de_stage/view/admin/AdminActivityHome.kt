package com.example.projet_de_stage.view.admin

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
import androidx.viewpager2.widget.ViewPager2
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterAdmin.ShopManagementPagerAdapter
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.view.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import androidx.core.content.edit

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

    private lateinit var shopOwner: ShopOwner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admine)

        val toolbar = findViewById<Toolbar>(R.id.toolbarAdmine)
        setSupportActionBar(toolbar)

        shopOwner = intent.getParcelableExtra<ShopOwner>("shopOwner")!!

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val adapter = ShopManagementPagerAdapter(this, shopOwner)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.barber_requests)
                1 -> getString(R.string.join_requests)
                2 -> getString(R.string.my_shops)
                else -> null
            }
        }.attach()
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
        prefs.edit().putString("lang", languageCode).apply()

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

