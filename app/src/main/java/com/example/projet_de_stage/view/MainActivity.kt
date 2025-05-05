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

class MainActivity : AppCompatActivity() {

    private val viewModelAuth = AuthViewModel(AuthRepository())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // تحقق من اتصال الإنترنت أولاً
        if (!isInternetAvailable(this)) {
            showErrorDialog()
            return
        }

        // ثم نبدأ باقي العملية بعد 2 ثواني
        FirebaseApp.initializeApp(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val savedUid = getSharedPreferences("prefs", MODE_PRIVATE).getString("uid", "")
            if (!savedUid.isNullOrEmpty()) {
                lifecycleScope.launch {
                    try {
                        val user = viewModelAuth.getUserByUid(savedUid)
                        when (user) {
                            is Barber -> {
                                startActivity(Intent(this@MainActivity, BarberActivityHome::class.java).apply {
                                    putExtra("barber", user)
                                })
                            }
                            is Customer -> {
                                startActivity(Intent(this@MainActivity, HomeActivityClient::class.java).apply {
                                    putExtra("customer", user)
                                })
                            }
                            is ShopOwner -> {
                                startActivity(Intent(this@MainActivity, AdminActivityHome::class.java).apply {
                                    putExtra("shopOwner", user)
                                })
                            }
                            else -> {
                                showErrorDialog()
                            }
                        }
                        finish()
                    } catch (e: Exception) {
                        showErrorDialog()
                    }
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.connection_error_title))
            .setMessage(getString(R.string.connection_error_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.retry)) { _, _ ->
                recreate()
            }
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                finish()
            }
            .show()
    }

}
