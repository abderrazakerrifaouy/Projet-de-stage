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

        // ربط الـ Views
        ivProfileImage = findViewById(R.id.ivProfileImage)
        etName         = findViewById(R.id.etName)
        etEmail        = findViewById(R.id.etEmail)
        etPhone        = findViewById(R.id.etPhone)
        etBirthDate    = findViewById(R.id.etBirthDate)
        etAddress      = findViewById(R.id.etAddress)
        etPassword     = findViewById(R.id.etPassword)
        btnSave        = findViewById(R.id.btnSave)
        progressBar    = findViewById(R.id.progressBarClient)

        // استلام بيانات العميل من Intent
        customer = intent.getParcelableExtra("CUSTOMER_DATA")!!

        // عرض البيانات الحالية
        ivProfileImage.setImageResource(customer.imageRes)
        etName.setText(customer.name)
        etEmail.setText(customer.email)
        etPhone.setText(customer.phone)
        etBirthDate.setText(customer.birthDate)
        etAddress.setText(customer.address)
        etPassword.setText(customer.password)
        etPhone.setText(customer.phone)

        // ضبط حدث الضغط على زر الحفظ
        btnSave.setOnClickListener {
            updateCustomer()
        }
    }

    /**
     * يجمع القيم من الحقول، ويستدعي ViewModel.updateCustomer
     * مع عرض ProgressBar وتعطيل الزر أثناء المعالجة.
     */
    private fun updateCustomer() {
        // جمع البيانات الجديدة
        customer.apply {
            name      = etName.text.toString().trim()
            email     = etEmail.text.toString().trim()
            birthDate = etBirthDate.text.toString().trim()
            address   = etAddress.text.toString().trim()
            password  = etPassword.text.toString()
            phone     = etPhone.text.toString()
            // رقم الهاتف ثابت هنا
        }

        // تعطيل الزر وإظهار التحميل
        btnSave.isEnabled = false
        progressBar.visibility = View.VISIBLE

        // مناداة ViewModel
        viewModel.updateCustomer(
            customer = customer,
            onSuccess = {
                // عند النجاح
                progressBar.visibility = View.GONE
                Toast.makeText(this, "تم تحديث الملف الشخصي بنجاح", Toast.LENGTH_SHORT).show()
                // عودة النتيجة
                val intent = Intent(this, HomeActivityClient::class.java)
                intent.putExtra("customer", customer)
                startActivity(intent)
                finish()
            },
            onFailure = { exception ->
                // عند الفشل
                progressBar.visibility = View.GONE
                btnSave.isEnabled = true
                Toast.makeText(this, "خطأ: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}

