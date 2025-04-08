package com.example.projet_de_stage.view

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_de_stage.R
import kotlin.text.isEmpty
import kotlin.text.trim

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etRecoveryInput: EditText
    private lateinit var btnSendResetLink: Button
    private lateinit var tvBackToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        etRecoveryInput = findViewById(R.id.etRecoveryInput)
        btnSendResetLink = findViewById(R.id.btnSendResetLink)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)

        // تعيين مستمعين للنقر
        btnSendResetLink.setOnClickListener {
            handlePasswordReset()
        }

        tvBackToLogin.setOnClickListener {
            finish() // إغلاق هذه النشاط والعودة لتسجيل الدخول
        }
    }

    private fun handlePasswordReset() {
        val recoveryInput = etRecoveryInput.text.toString().trim()

        if (recoveryInput.isEmpty()) {
            showToast("الرجاء إدخال البريد الإلكتروني أو رقم الهاتف")
            return
        }

        if (isValidEmail(recoveryInput) || isValidPhone(recoveryInput)) {
            // هنا عادةً تقوم باستدعاء API لإرسال تعليمات إعادة التعيين
            showToast("تم إرسال تعليمات إعادة التعيين إلى $recoveryInput")
            // بعد الإرسال، يمكنك إغلاق النشاط
            finish()
        } else {
            showToast("الرجاء إدخال بريد إلكتروني أو رقم هاتف صحيح")
        }
    }

    private fun isValidEmail(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    private fun isValidPhone(input: String): Boolean {
        return Patterns.PHONE.matcher(input).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}