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
import androidx.core.content.edit

/**
 * LoginActivity is responsible for handling user login functionality.
 * It authenticates the user and redirects them to their respective home screen.
 */
class LoginActivity : AppCompatActivity() {

    private val viewModelAuth = AuthViewModel(AuthRepository())

    /**
     * This method is called when the activity is created.
     * It sets up the UI elements and handles the login and navigation actions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.etEmailLogin)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val createAccountButton = findViewById<Button>(R.id.btnCreateAccount)
        val forgotPasswordTextView = findViewById<TextView>(R.id.tvForgotPassword)

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModelAuth.loginUser(email, password)
        }

        // Collect authentication state and navigate accordingly
        lifecycleScope.launchWhenStarted {
            viewModelAuth.authState.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        val uid = it.getOrNull()
                        val user = viewModelAuth.getUserByUid(uid!!)

                        // Navigate to appropriate screen based on user type
                        when (user) {
                            is Barber -> navigateToBarberHome(user)
                            is Customer -> navigateToCustomerHome(user)
                            is ShopOwner -> navigateToShopOwnerHome(user)
                            else -> showError("Unknown user type")
                        }
                    } else {
                        showError("Login failed: ${it.exceptionOrNull()?.message}")
                    }
                }
            }
        }

        // Handle "Create Account" button click
        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handle "Forgot Password" click
        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    /**
     * Navigates to the Barber's home activity.
     */
    private fun navigateToBarberHome(barber: Barber) {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit() {
            putString("uid", barber.uid)
        }

        val intent = Intent(this@LoginActivity, BarberActivityHome::class.java)
        intent.putExtra("barber", barber)
        startActivity(intent)
        finish()
    }

    /**
     * Navigates to the Customer's home activity.
     */
    private fun navigateToCustomerHome(customer: Customer) {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit() {
            putString("uid", customer.uid)
        }

        val intent = Intent(this@LoginActivity, HomeActivityClient::class.java)
        intent.putExtra("customer", customer)
        startActivity(intent)
        finish()
    }

    /**
     * Navigates to the ShopOwner's home activity.
     */
    private fun navigateToShopOwnerHome(shopOwner: ShopOwner) {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit() {
            putString("uid", shopOwner.uid)
        }

        val intent = Intent(this@LoginActivity, AdminActivityHome::class.java)
        intent.putExtra("shopOwner", shopOwner)
        startActivity(intent)
        finish()
    }

    /**
     * Displays an error message using a Toast.
     */
    private fun showError(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }
}
