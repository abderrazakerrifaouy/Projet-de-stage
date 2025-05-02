package com.example.projet_de_stage.view.client

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.viewModel.ClientViewModel

/**
 * Activity for updating the profile information of the customer.
 */
class UpdateCustomerProfileActivity : AppCompatActivity() {

    private val viewModel: ClientViewModel by viewModels()
    private lateinit var customer: Customer

    private lateinit var ivProfileImage: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_customer_profile)

        // Binding the Views to their respective UI components
        ivProfileImage = findViewById(R.id.ivProfileImage)
        etName         = findViewById(R.id.etName)
        etEmail        = findViewById(R.id.etEmail)
        etPhone        = findViewById(R.id.etPhone)
        etBirthDate    = findViewById(R.id.etBirthDate)
        etAddress      = findViewById(R.id.etAddress)
        etPassword     = findViewById(R.id.etPassword)
        btnSave        = findViewById(R.id.btnSave)
        progressBar    = findViewById(R.id.progressBarClient)

        // Receiving customer data from the Intent
        customer = intent.getParcelableExtra("CUSTOMER_DATA")!!

        // Displaying the current data
        ivProfileImage.setImageResource(customer.imageRes)
        etName.setText(customer.name)
        etEmail.setText(customer.email)
        etPhone.setText(customer.phone)
        etBirthDate.setText(customer.birthDate)
        etAddress.setText(customer.address)
        etPassword.setText(customer.password)

        // Setting up the Save button click listener
        btnSave.setOnClickListener {
            updateCustomer()
        }
    }

    /**
     * Collects the updated data from the input fields and calls ViewModel.updateCustomer.
     * Displays the ProgressBar and disables the button during processing.
     */
    private fun updateCustomer() {
        // Collecting new data from the input fields
        customer.apply {
            name      = etName.text.toString().trim()
            email     = etEmail.text.toString().trim()
            birthDate = etBirthDate.text.toString().trim()
            address   = etAddress.text.toString().trim()
            password  = etPassword.text.toString()
            phone     = etPhone.text.toString()
        }

        // Disabling the Save button and showing the progress bar
        btnSave.isEnabled = false
        progressBar.visibility = View.VISIBLE

        // Calling ViewModel to update the customer data
        viewModel.updateCustomer(
            customer = customer,
            onSuccess = {
                // Success callback
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                // Returning the updated customer data to the previous screen
                val intent = Intent(this, HomeActivityClient::class.java)
                intent.putExtra("customer", customer)
                startActivity(intent)
                finish()
            },
            onFailure = { exception ->
                // Failure callback
                progressBar.visibility = View.GONE
                btnSave.isEnabled = true
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}
