package com.example.projet_de_stage.view

import com.example.projet_de_stage.R
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.AuthRepository
import com.example.projet_de_stage.view.admin.AdminActivityHome
import com.example.projet_de_stage.view.barberUser.BarberActivityHome
import com.example.projet_de_stage.view.client.HomeActivityClient
import com.example.projet_de_stage.viewModel.AuthViewModel


class LoginActivity : AppCompatActivity() {
    private val viewModelAuth =  AuthViewModel(AuthRepository())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val email = findViewById<EditText>(R.id.etEmailLogin)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        btnLogin.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            viewModelAuth.loginUser(email, password)


        }


        lifecycleScope.launchWhenStarted {
            viewModelAuth.authState.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(this@LoginActivity, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show()
                        val uid = it.getOrNull()
                        val user = viewModelAuth.getUserByUid(uid!!)
                        when (user) {
                            is Barber -> {
                                var prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                                var editor = prefs.edit()
                                editor.putString("uid", user.uid)
                                editor.apply()
                                val intent = Intent(this@LoginActivity, BarberActivityHome::class.java)
                                intent.putExtra("barber", user)
                                startActivity(intent)
                                finish()
                            }
                            is Customer -> {
                                var prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                                var editor = prefs.edit()
                                editor.putString("uid", user.uid)
                                editor.apply()
                                val intent = Intent(this@LoginActivity, HomeActivityClient::class.java)
                                intent.putExtra("customer", user)
                                startActivity(intent)
                                finish()
                            }
                            is ShopOwner -> {
                                var prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                                var editor = prefs.edit()
                                editor.putString("uid", user.uid)
                                editor.apply()
                                val intent = Intent(this@LoginActivity, AdminActivityHome::class.java)
                                intent.putExtra("shopOwner", user)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                Toast.makeText(this@LoginActivity, "نوع المستخدم غير معروف", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "فشل تسجيل الدخول: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()

        }

        val forgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}