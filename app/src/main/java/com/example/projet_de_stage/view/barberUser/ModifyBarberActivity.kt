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

class ModifyBarberActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etExperience: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var btnSave: Button
    private val viewModel = BarberViewModel()

    private lateinit var barber: Barber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_barber)

        // Initialize views
        etName = findViewById(R.id.etName)
        etExperience = findViewById(R.id.etExperience)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etBirthDate = findViewById(R.id.etBirthDate)
        btnSave = findViewById(R.id.btnSave)

        // Get passed Barber
        barber = intent.getParcelableExtra("barber")!!

        // Set existing data
        etName.setText(barber.name)
        etExperience.setText(barber.experience)
        etEmail.setText(barber.email)
        etPhone.setText(barber.phone)
        etPassword.setText(barber.password)
        etBirthDate.setText(barber.birthDate)

        // Save button click
        btnSave.setOnClickListener {
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
                    Toast.makeText(this, "Barber modified successfully", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        val shop = viewModel.getShopByBarberId(modifiedBarber.uid)
                        if (shop != null) {
                            shop.barbers.remove(barber)
                            shop.barbers.add(modifiedBarber)

                            viewModel.updateShop(shop) { success ->
                                if (success) {
                                Toast.makeText(this@ModifyBarberActivity, "Tme", Toast.LENGTH_SHORT).show()
                                } else {
                                Toast.makeText(this@ModifyBarberActivity, "Failed to modify Shope", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                    val intent = Intent(this@ModifyBarberActivity, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Failed to modify barber", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
