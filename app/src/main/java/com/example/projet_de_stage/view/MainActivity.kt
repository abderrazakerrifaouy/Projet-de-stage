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
        FirebaseApp.initializeApp(this)

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
                                    showErrorDialog("لم يتم التعرف على نوع المستخدم.")
                                }
                            }
                            finish()
                        } catch (e: Exception) {
                            showErrorDialog("فشل في تحميل البيانات. تأكد من الاتصال بالإنترنت.\n${e.message}")
                        }
                    }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                showErrorDialog("لا يوجد اتصال بالإنترنت. يرجى التحقق من الشبكة والمحاولة مرة أخرى.")
            }
        }, 2000)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("خطأ في الاتصال")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("إعادة المحاولة") { _, _ ->
                recreate() // يعيد تشغيل الـ Activity
            }
            .setNegativeButton("خروج") { _, _ ->
                finish()
            }
            .show()
    }
}
