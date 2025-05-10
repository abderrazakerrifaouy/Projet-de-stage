package com.example.projet_de_stage.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
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
import kotlinx.coroutines.launch
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val viewModelAuth = AuthViewModel(AuthRepository())
    private lateinit var progressBar: ProgressBar
    private lateinit var languageSpinner: Spinner
    private lateinit var tvErrorMessage: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // ربط عناصر الواجهة
        val emailEditText = findViewById<EditText>(R.id.etEmailLogin)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val createAccountButton = findViewById<Button>(R.id.btnCreateAccount)
        val forgotPasswordTextView = findViewById<TextView>(R.id.tvForgotPassword)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        languageSpinner = findViewById(R.id.languageSpinner)
        progressBar = findViewById(R.id.progressBar)

        // إعداد السبينر لتغيير اللغة
        val languageNames = resources.getStringArray(R.array.language_names)
        val languageCodes = resources.getStringArray(R.array.language_codes)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageNames)
        languageSpinner.adapter = spinnerAdapter

        // قراءة اللغة المحفوظة من SharedPreferences
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val savedLangCode = sharedPref.getString("selected_language", Locale.getDefault().language)
        val defaultIndex = languageCodes.indexOf(savedLangCode).takeIf { it != -1 } ?: 0
        languageSpinner.setSelection(defaultIndex)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLangCode = languageCodes[position]
                val currentLangCode = resources.configuration.locales[0].language

                if (selectedLangCode != currentLangCode) {
                    changeAppLanguage(selectedLangCode)
                    // حفظ اللغة في SharedPreferences
                    sharedPref.edit { putString("selected_language", selectedLangCode) }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // عند الضغط على زر تسجيل الدخول
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (!checkFormEmail(email)) {
                tvErrorMessage.text = getString(R.string.invalid_email)
                tvErrorMessage.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (!checkFormPassword(password)) {
                tvErrorMessage.text = getString(R.string.invalid_password)
                tvErrorMessage.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // عرض شريط التحميل
            progressBar.visibility = View.VISIBLE
            tvErrorMessage.visibility = View.GONE
            viewModelAuth.loginUser(email, password)
        }

        // مراقبة حالة المصادقة
        lifecycleScope.launch {
            viewModelAuth.authState.collect { result ->
                progressBar.visibility = View.GONE

                result?.let {
                    if (it.isSuccess) {
                        val uid = it.getOrNull()
                        val user = viewModelAuth.getUserByUid(uid!!)

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

        // زر إنشاء حساب جديد
        createAccountButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // زر نسيت كلمة السر
        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    // التنقل إلى واجهة الحلاق
    private fun navigateToBarberHome(barber: Barber) {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit { putString("uid", barber.uid) }

        val intent = Intent(this, BarberActivityHome::class.java)
        intent.putExtra("barber", barber)
        startActivity(intent)
        finish()
    }

    // التنقل إلى واجهة الزبون
    private fun navigateToCustomerHome(customer: Customer) {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit { putString("uid", customer.uid) }

        val intent = Intent(this, HomeActivityClient::class.java)
        intent.putExtra("customer", customer)
        startActivity(intent)
        finish()
    }

    // التنقل إلى واجهة صاحب المحل
    private fun navigateToShopOwnerHome(shopOwner: ShopOwner) {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        prefs.edit { putString("uid", shopOwner.uid) }

        val intent = Intent(this, AdminActivityHome::class.java)
        intent.putExtra("shopOwner", shopOwner)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun checkFormEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkFormPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun changeAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // إعادة تشغيل النشاط لتطبيق التغييرات
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }
}
