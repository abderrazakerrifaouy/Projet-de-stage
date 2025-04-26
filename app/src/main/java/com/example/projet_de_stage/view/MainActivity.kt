package com.example.projet_de_stage.view

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.projet_de_stage.view.LoginActivity
import com.example.projet_de_stage.view.admin.AdminActivityHome
import com.example.projet_de_stage.view.admin.CreateShopActivity
import com.example.projet_de_stage.view.barberUser.BarberActivityHome
import com.example.projet_de_stage.view.client.HomeActivityClient
import com.example.projet_de_stage.viewModel.AuthViewModel
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModelAuth =  AuthViewModel(AuthRepository())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val savedUid = getSharedPreferences("prefs", MODE_PRIVATE).getString("uid", "")
            if (!savedUid.isNullOrEmpty()) {
                lifecycleScope.launch {
                    val user = viewModelAuth.getUserByUid(savedUid)
                    when (user) {
                        is Barber -> {
                            val intent = Intent(this@MainActivity, BarberActivityHome::class.java)
                            intent.putExtra("barber", user)
                            startActivity(intent)
                            finish()
                        }

                        is Customer -> {
                            val intent = Intent(this@MainActivity, HomeActivityClient::class.java)
                            intent.putExtra("customer", user)
                            startActivity(intent)
                            finish()
                        }

                        is ShopOwner -> {

                            val intent = Intent(this@MainActivity, AdminActivityHome::class.java)
                            intent.putExtra("shopOwner", user)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000) // مثلا تأخير 2 ثواني

//

    }
}