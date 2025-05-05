package com.example.projet_de_stage.view.barberUser

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.view.MainActivity
import com.example.projet_de_stage.viewModel.BarberViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Activity to modify a barber's profile information.
 * Receives a Barber object via Intent and allows updating their data, including uploading a profile image.
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
    private lateinit var btnUploadImage: Button
    private lateinit var profileImage: ImageView

    // ViewModel
    private val viewModel = BarberViewModel()

    // Current Barber
    private lateinit var barber: Barber
    private var imageUri: Uri? = null

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

        // Open image picker when user clicks the upload image button
        btnUploadImage.setOnClickListener { openImagePicker() }
    }

    /**
     * Initialize view references from layout
     */
    private fun initViews() {
        etName = findViewById(R.id.etNameBarber)
        etExperience = findViewById(R.id.etExperienceBarber)
        etEmail = findViewById(R.id.etEmailBarber)
        etPhone = findViewById(R.id.etPhoneBarber)
        etPassword = findViewById(R.id.etPasswordBarber)
        etBirthDate = findViewById(R.id.etBirthDateBarber)
        btnSave = findViewById(R.id.btnSaveBarber)
        btnUploadImage = findViewById(R.id.btnUploadImageBarber)
        profileImage = findViewById(R.id.profileImageBarber)
    }

    /**
     * Populate EditText fields with current barber data
     * If barber has an image URL, load it into the profile image
     * Otherwise, set a default image
     * @see Barber
     * @see ImageView
     */
    private fun populateFields() {
        etName.setText(barber.name)
        etExperience.setText(barber.experience)
        etEmail.setText(barber.email)
        etPhone.setText(barber.phone)
        etPassword.setText(barber.password)
        etBirthDate.setText(barber.birthDate)
        if (barber.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(barber.imageUrl)
                .into(profileImage)
        } else {
            profileImage.setImageResource(R.drawable.my_profile)
        }
    }

    /**
     * Handle save button click: update barber and associated shop data
     * If an image was picked, upload it and save the barber with the image URL
     * Otherwise, just save the barber
     * @see Barber
     * @see BarberViewModel
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

        // If an image was picked, upload it and save the barber with the image URL

        lifecycleScope.launch {
            try {
                val imageUrl = createShopWithImage(imageUri, this@ModifyBarberActivity)
                if (imageUrl != null) {
                    modifiedBarber.imageUrl = imageUrl
                }
            } catch (e: Exception) {
                Toast.makeText(this@ModifyBarberActivity, "Image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }

                viewModel.modifyBarber(modifiedBarber) { success ->
                    if (success) {
                        Toast.makeText(this@ModifyBarberActivity, "Barber modified successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ModifyBarberActivity, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@ModifyBarberActivity, "Failed to modify barber", Toast.LENGTH_SHORT).show()
                    }
                }

        }


    }

    /**
     * Open image picker to choose a profile image
     */
    private fun openImagePicker() {
        // Open image picker (this example uses an intent to open gallery)
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    /**
     * Handle result from image picker
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            imageUri = data?.data
            profileImage.setImageURI(imageUri)
        }
    }

    /**
     * Upload the image and create the shop with the updated image URL in Firestore
     */


    // هذه هي دالة Coroutine التي تقوم بتحميل الصورة إلى Cloudinary
    fun createShopWithImage(
        imageUri: Uri?,
        context: Context
    ): String? {

        // إذا لم يتم توفير صورة، نعيد رابط الصورة الافتراضي
        if (imageUri == null) {
            return null
        }

        // استخدام Coroutine داخل Thread منفصل
        var imageUrl: String? = null

        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    // الحصول على تدفق البيانات للصورة
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    val bytes = inputStream?.readBytes()

                    if (bytes == null) {
                        throw Exception("Failed to read the image")
                    }

                    // بناء الطلب الخاص برفع الصورة إلى Cloudinary
                    val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", "shop_image.jpg",
                            bytes.toRequestBody("image/*".toMediaTypeOrNull()))
                        .addFormDataPart("upload_preset", "abderrazake")
                        .build()

                    val request = Request.Builder()
                        .url("https://api.cloudinary.com/v1_1/dmldc4hal/image/upload")
                        .post(requestBody)
                        .build()

                    val client = OkHttpClient()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val jsonObject = JSONObject(responseBody ?: "")
                        imageUrl = jsonObject.getString("secure_url")
                    } else {
                        throw Exception("Image upload failed: ${response.message}")
                    }
                } catch (e: IOException) {
                    throw Exception("Error uploading image", e)
                }
            }
        }

        // إرجاع الرابط بعد رفع الصورة بنجاح
        return imageUrl
    }

}
