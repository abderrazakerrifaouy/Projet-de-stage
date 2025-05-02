package com.example.projet_de_stage.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.AuthRepository
import com.example.projet_de_stage.view.admin.AdminActivityHome
import com.example.projet_de_stage.view.barberUser.BarberActivityHome
import com.example.projet_de_stage.view.client.HomeActivityClient
import com.example.projet_de_stage.viewModel.AuthViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

/**
 * Main activity that handles user authentication and navigation to the corresponding home screen
 * based on the user's role (Barber, Customer, ShopOwner).
 */
class MainActivity : AppCompatActivity() {

    private val viewModelAuth = AuthViewModel(AuthRepository())

    /**
     * Called when the activity is created. Initializes Firebase and checks for internet connectivity.
     * If internet is available, it checks for a saved UID and navigates to the correct home screen.
     * If no UID is found, it navigates to the login screen.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        // Delay of 2 seconds to simulate splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            if (isInternetAvailable(this)) {
                val savedUid = getSharedPreferences("prefs", MODE_PRIVATE).getString("uid", "")
                if (!savedUid.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        try {
                            val user = viewModelAuth.getUserByUid(savedUid)
                            when (user) {
                                is Barber -> {
                                    val intent = Intent(this@MainActivity, BarberActivityHome::class.java)
                                    intent.putExtra("barber", user)
                                    startActivity(intent)
                                }
                                is Customer -> {
                                    val intent = Intent(this@MainActivity, HomeActivityClient::class.java)
                                    intent.putExtra("customer", user)
                                    startActivity(intent)
                                }
                                is ShopOwner -> {
                                    val intent = Intent(this@MainActivity, AdminActivityHome::class.java)
                                    intent.putExtra("shopOwner", user)
                                    startActivity(intent)
                                }
                                else -> {
                                    showErrorDialog("User type not recognized.")
                                }
                            }
                            finish()
                        } catch (e: Exception) {
                            showErrorDialog("Failed to load data. Please check your internet connection.\n${e.message}")
                        }
                    }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                showErrorDialog("No internet connection. Please check your network and try again.")
            }
        }, 2000) // 2 second delay
    }

    /**
     * Checks if the device is connected to the internet.
     * @param context The application context
     * @return Boolean indicating if the internet is available
     */
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Shows an error dialog with a message and options to retry or exit the app.
     * @param message The error message to display
     */
    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Connection Error")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Retry") { _, _ ->
                recreate() // Restart the activity
            }
            .setNegativeButton("Exit") { _, _ ->
                finish() // Close the activity
            }
            .show()
    }
}
