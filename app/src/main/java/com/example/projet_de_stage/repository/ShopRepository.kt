package com.example.projet_de_stage.repository

import android.content.Context
import android.net.Uri
import com.example.projet_de_stage.data.Shop
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ShopRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("shops")

    fun createShopWithImage(
        shop: Shop,
        imageUri: Uri?,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val shopRef = collection.document(shop.id)

        if (imageUri == null) {
            // ما كيناش صورة
            shopRef.set(shop)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
            return
        }

        // دابا نحمل الصورة إلى Cloudinary
        val context = context.applicationContext
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()

        if (bytes == null) {
            onFailure(Exception("فشل في قراءة الصورة"))
            return
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "shop_image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), bytes))
            .addFormDataPart("upload_preset", "abderrazake") // دير هنا upload preset ديالك
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/dmldc4hal/image/upload") // دير هنا Cloud Name ديالك
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody ?: "")
                    val imageUrl = jsonObject.getString("secure_url")

                    // دابا نحط رابط الصورة في ال Shop ونسجلو في Firestore
                    val shopWithImage = shop.copy(imageUrl = imageUrl)

                    shopRef.set(shopWithImage)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }

                } else {
                    onFailure(Exception("فشل رفع الصورة: ${response.message}"))
                }
            }
        })
    }


    suspend fun getAllShops(): List<Shop> {
        val snapshot = collection
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .await()                    // ينتظر انتهاء الـ Task
        return snapshot.toObjects(Shop::class.java)
    }

    suspend fun getShopsByOwnerId(ownerId: String): List<Shop> {
        return try {
            val snapshot = collection
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()

            snapshot.toObjects(Shop::class.java)
        } catch (_: Exception) {
            emptyList() // أو تقدر ترجع null ولا دير logging حسب الحاجة
        }
    }



}
