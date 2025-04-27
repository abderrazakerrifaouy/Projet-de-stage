package com.example.projet_de_stage.view.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.AuthRepository
import com.example.projet_de_stage.viewModel.AdmineViewModel
import com.example.projet_de_stage.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import java.util.*

class CreateShopActivity : AppCompatActivity() {

    private lateinit var viewModel: AdmineViewModel
    private var admine: ShopOwner? = null
    private var imageUri: Uri? = null

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etAddress: EditText
    private lateinit var nbarbers: EditText
    private lateinit var btnSubmit: Button
    private lateinit var ivShopImage: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val viewModelAuth = AuthViewModel(AuthRepository())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_shop)

        viewModel = ViewModelProvider(this)[AdmineViewModel::class.java]

        val admineUid = intent.getStringExtra("shopOwner")
        if (admineUid.isNullOrEmpty()) {
            Toast.makeText(this, "لا يوجد معرف المالك", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // جلب بيانات المالك
        viewModel.getShopOwnerById(admineUid) { owner ->
            if (owner == null) {
                Toast.makeText(this, "$admineUid لا يوجد في قاعدة البيانات", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                admine = owner
                Toast.makeText(this, "تم تحميل بيانات: ${owner.uid}", Toast.LENGTH_SHORT).show()
            }
        }

        // ربط الواجهات
        etName = findViewById(R.id.etShopName)
        etDescription = findViewById(R.id.etShopDescription)
        etAddress = findViewById(R.id.etLocation)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivShopImage = findViewById(R.id.ivShopImage)
        nbarbers = findViewById(R.id.NBarbers)

        // اختيار صورة
        findViewById<Button>(R.id.btnUploadImage).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // عند الضغط على زر إرسال
        btnSubmit.setOnClickListener {
            // التأكد من أن بيانات المالك تم تحميلها
            if (admine == null) {
                Toast.makeText(this, "الرجاء الانتظار حتى يتم تحميل بيانات المالك", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // التحقق من الحقول
            if (etName.text.isEmpty() || etAddress.text.isEmpty()) {
                Toast.makeText(this, "الرجاء ملء جميع الحقول", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // إنشاء كائن المحل
            val shop = Shop(
                id = UUID.randomUUID().toString(),
                name = etName.text.toString(),
                idOwner = admine!!.uid,
                address = etAddress.text.toString(),
                imageRes = R.drawable.my_profile , // استخدام صورة افتراضية إذا لم يتم رفع صورة
                nbarbers = nbarbers.text.toString().toInt()
            )

            // إرسال البيانات
            viewModel.createShop(shop, imageUri , this@CreateShopActivity)
        }

        // متابعة حالة إنشاء المحل
        viewModel.shopCreationStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "تم إنشاء المحل", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // متابعة حالة الخطأ
        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, "خطأ: $message", Toast.LENGTH_LONG).show()
        }
    }

    // التعامل مع النتيجة من اختيار الصورة
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            ivShopImage.setImageURI(imageUri)
        }
    }
}
