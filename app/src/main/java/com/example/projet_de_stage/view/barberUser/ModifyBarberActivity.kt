package com.example.projet_de_stage.view.barberUser

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.view.MainActivity
import com.example.projet_de_stage.viewModel.BarberViewModel
import kotlinx.coroutines.launch

/**
 * Activity to modify a barber's profile information.
 * Receives a Barber object via Intent and allows updating their data.
 */
class ModifyBarberActivity : AppCompatActivity() {

    // UI Components
    private lateinit var etName: EditText
    private lateinit var etExperience: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var btnSave: Button

    // ViewModel
    private val viewModel = BarberViewModel()

    // Current Barber
    private lateinit var barber: Barber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_barber)

        // Initialize input fields
        initViews()

        // Retrieve barber passed from previous screen
        barber = intent.getParcelableExtra("barber")!!

        // Fill fields with current barber info
        populateFields()

        // Save updated data when user clicks the save button
        btnSave.setOnClickListener { onSaveClicked() }
    }

    /**
     * Initialize view references from layout
     */
    private fun initViews() {
        etName = findViewById(R.id.etName)
        etExperience = findViewById(R.id.etExperience)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etBirthDate = findViewById(R.id.etBirthDate)
        btnSave = findViewById(R.id.btnSave)
    }

    /**
     * Populate EditText fields with current barber data
     */
    private fun populateFields() {
        etName.setText(barber.name)
        etExperience.setText(barber.experience)
        etEmail.setText(barber.email)
        etPhone.setText(barber.phone)
        etPassword.setText(barber.password)
        etBirthDate.setText(barber.birthDate)
    }

    /**
     * Handle save button click: update barber and associated shop data
     */
    private fun onSaveClicked() {
        val modifiedBarber = barber.copy(
            name = etName.text.toString(),
            experience = etExperience.text.toString(),
            email = etEmail.text.toString(),
            phone = etPhone.text.toString(),
            password = etPassword.text.toString(),
            birthDate = etBirthDate.text.toString()
        )

        viewModel.modifyBarber(modifiedBarber) { success ->
            if (success) {
                Toast.makeText(this, "Barber updated successfully", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    val shop = viewModel.getShopByBarberId(modifiedBarber.uid)
                    if (shop != null) {
                        shop.barbers.remove(barber)
                        shop.barbers.add(modifiedBarber)

                        viewModel.updateShop(shop) { shopUpdateSuccess ->
                            if (shopUpdateSuccess) {
                                Toast.makeText(this@ModifyBarberActivity, "Shop updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@ModifyBarberActivity, "Failed to update shop", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                // Navigate back to main activity
                val intent = Intent(this@ModifyBarberActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Failed to update barber", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
