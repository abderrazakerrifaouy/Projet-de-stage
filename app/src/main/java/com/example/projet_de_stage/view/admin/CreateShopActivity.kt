package com.example.projet_de_stage.view.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.AuthRepository
import com.example.projet_de_stage.viewModel.AdminViewModel
import com.example.projet_de_stage.viewModel.AuthViewModel
import java.util.*

/**
 * Activity for creating a new shop.
 * Only accessible by admin or ShopOwner.
 */
class CreateShopActivity : AppCompatActivity() {

    private lateinit var viewModel: AdminViewModel
    private var shopOwner: ShopOwner? = null
    private var imageUri: Uri? = null

    // Views
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText // Currently unused, kept for future
    private lateinit var etAddress: EditText
    private lateinit var etBarberCount: EditText
    private lateinit var btnSubmit: Button
    private lateinit var ivShopImage: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private val authViewModel = AuthViewModel(AuthRepository())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shop)

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        initializeViews()
        loadShopOwnerData()
        setupListeners()
        observeViewModel()
    }

    /**
     * Bind views to their XML references.
     */
    private fun initializeViews() {
        etName = findViewById(R.id.etShopName)
        etDescription = findViewById(R.id.etShopDescription)
        etAddress = findViewById(R.id.etLocation)
        etBarberCount = findViewById(R.id.NBarbers)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivShopImage = findViewById(R.id.ivShopImage)
    }

    /**
     * Retrieve ShopOwner data from intent and fetch full data from ViewModel.
     */
    private fun loadShopOwnerData() {
        val ownerUid = intent.getStringExtra("shopOwner")
        if (ownerUid.isNullOrEmpty()) {
            Toast.makeText(this, "No Shop Owner ID provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.getShopOwnerById(ownerUid) { owner ->
            if (owner == null) {
                Toast.makeText(this, "ShopOwner $ownerUid not found", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                shopOwner = owner
                Toast.makeText(this, "Loaded owner data: ${owner.uid}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Setup click listeners for image selection and submit button.
     */
    private fun setupListeners() {
        findViewById<Button>(R.id.btnUploadImage).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSubmit.setOnClickListener {
            if (shopOwner == null) {
                Toast.makeText(this, "Please wait for shop owner data to load", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validateInput()) return@setOnClickListener

            val shop = Shop(
                id = UUID.randomUUID().toString(),
                name = etName.text.toString(),
                ownerId = shopOwner!!.uid,
                address = etAddress.text.toString(),
                numberOfBarbers = etBarberCount.text.toString().toInt(),
                imageRes = R.drawable.my_profile
            )

            viewModel.createShop(shop, imageUri, this@CreateShopActivity)
        }
    }

    /**
     * Validate user input fields.
     */
    private fun validateInput(): Boolean {
        if (etName.text.isEmpty() || etAddress.text.isEmpty() || etBarberCount.text.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /**
     * Observe ViewModel state to react to shop creation and errors.
     */
    private fun observeViewModel() {
        viewModel.shopCreationStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Shop successfully created", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Handle result from image picker.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            imageUri = data.data
            ivShopImage.setImageURI(imageUri)
        }
    }
}
