package com.example.projet_de_stage.view

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_de_stage.R

/**
 * Activity for handling the password recovery process.
 */
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var recoveryInputEditText: EditText
    private lateinit var sendResetLinkButton: Button
    private lateinit var backToLoginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize UI elements
        recoveryInputEditText = findViewById(R.id.etRecoveryInput)
        sendResetLinkButton = findViewById(R.id.btnSendResetLink)
        backToLoginTextView = findViewById(R.id.tvBackToLogin)

        // Set on click listeners
        sendResetLinkButton.setOnClickListener {
            handlePasswordReset()
        }

        backToLoginTextView.setOnClickListener {
            finish() // Close this activity and go back to login screen
        }
    }

    /**
     * Handles the password reset logic: validate input and send reset link.
     */
    private fun handlePasswordReset() {
        val recoveryInput = recoveryInputEditText.text.toString().trim()

        if (recoveryInput.isEmpty()) {
            showToast("Please enter your email or phone number")
            return
        }

        if (isValidEmail(recoveryInput) || isValidPhone(recoveryInput)) {
            // Here, you would call an API to send the reset instructions
            showToast("Password reset instructions sent to $recoveryInput")
            // After sending, close the activity
            finish()
        } else {
            showToast("Please enter a valid email or phone number")
        }
    }

    /**
     * Validates if the input is a valid email address.
     */
    private fun isValidEmail(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    /**
     * Validates if the input is a valid phone number.
     */
    private fun isValidPhone(input: String): Boolean {
        return Patterns.PHONE.matcher(input).matches()
    }

    /**
     * Displays a toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
