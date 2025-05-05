package com.example.projet_de_stage.repository

import android.content.Context
import android.net.Uri
import com.example.projet_de_stage.data.Barber
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

/**
 * Repository class for handling shop-related operations.
 */
class ShopRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("shops")

    /**
     * Creates a shop with an optional image.
     * Uploads image to Cloudinary if provided.
     *
     * @param shop The shop object to be created.
     * @param imageUri The URI of the image to be uploaded (if any).
     * @param context The context used for accessing application resources.
     * @param onSuccess Callback invoked upon success.
     * @param onFailure Callback invoked upon failure.
     */
    fun createShopWithImage(
        shop: Shop,
        imageUri: Uri?,
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val shopRef = collection.document(shop.id)

        if (imageUri == null) {
            // If no image is provided
            shopRef.set(shop)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
            return
        }

        // Upload image to Cloudinary
        val context = context.applicationContext
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()

        if (bytes == null) {
            onFailure(Exception("Failed to read the image"))
            return
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "shop_image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), bytes))
            .addFormDataPart("upload_preset", "abderrazake") // Set your Cloudinary upload preset here
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/dmldc4hal/image/upload") // Set your Cloudinary Cloud Name here
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

                    // Add the image URL to the shop and store it in Firestore
                    val shopWithImage = shop.copy(imageUrl = imageUrl)

                    shopRef.set(shopWithImage)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }

                } else {
                    onFailure(Exception("Image upload failed: ${response.message}"))
                }
            }
        })
    }

    /**
     * Retrieves all shops from Firestore.
     *
     * @return A list of all shops.
     */
    suspend fun getAllShops(): List<Shop> {
        val snapshot = collection
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .await() // Wait for the task to finish
        return snapshot.toObjects(Shop::class.java)
    }

    /**
     * Retrieves shops by the owner's ID.
     *
     * @param ownerId The ID of the shop owner.
     * @return A list of shops owned by the specified owner.
     */
    suspend fun getShopsByOwnerId(ownerId: String): List<Shop> {
        return try {
            val snapshot = collection
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()

            snapshot.toObjects(Shop::class.java)
        } catch (_: Exception) {
            emptyList() // Return empty list in case of failure
        }
    }

    /**
     * Adds a barber to a specific shop.
     *
     * @param shopId The ID of the shop.
     * @param barber The barber object to be added.
     * @param onSuccess Callback invoked upon success.
     * @param onFailure Callback invoked upon failure.
     */
    fun addBarberToShop(
        shopId: String,
        barber: Barber,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val shopRef = collection.document(shopId)

        shopRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Retrieve the shop from Firestore
                    val shop = documentSnapshot.toObject(Shop::class.java)

                    if (shop != null) {
                        // Add the barber to the list of barbers
                        val updatedBarbersList = shop.barbers.toMutableList()
                        updatedBarbersList.add(barber)

                        // Create a new shop with the updated barbers list
                        val updatedShop = shop.copy(barbers = updatedBarbersList)

                        // Update Firestore with the new shop data
                        shopRef.set(updatedShop)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onFailure(it) }
                    } else {
                        onFailure(Exception("Shop not found"))
                    }
                } else {
                    onFailure(Exception("Shop does not exist"))
                }
            }
            .addOnFailureListener { onFailure(it) }
    }

    /**
     * Retrieves a shop by its ID.
     *
     * @param id The ID of the shop.
     * @param onSuccess Callback invoked upon success with the shop data.
     * @param onFailure Callback invoked upon failure.
     */
    fun getShopById(
        id: String,
        onSuccess: (Shop) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val shop = documentSnapshot.toObject(Shop::class.java)
                    if (shop != null) {
                        onSuccess(shop)
                    } else {
                        onFailure(Exception("Shop not found"))
                    }
                } else {
                    onFailure(Exception("Shop does not exist"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Updates a shop's information in Firestore.
     *
     * @param shop The updated shop object.
     * @param onSuccess Callback invoked upon success.
     * @param onFailure Callback invoked upon failure.
     */
    fun updateShop(
        shop: Shop,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Boolean) -> Unit
    ) {
        collection.document(shop.id)
            .set(shop)
            .addOnSuccessListener { onSuccess(true) }
            .addOnFailureListener { onFailure(false) }
    }


    fun deleteBarberFromShop(
        shopId: String,
        barberId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val shopRef = collection.document(shopId)
        shopRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val shop = documentSnapshot.toObject(Shop::class.java)
                    if (shop != null) {
                        val updatedBarbersList = shop.barbers.filter { it.uid != barberId }
                        val updatedShop = shop.copy(barbers = updatedBarbersList.toMutableList())
                        shopRef.set(updatedShop)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onFailure(it) }
                    } else {
                        onFailure(Exception("Failed to parse shop data."))
                    }
                } else {
                    onFailure(Exception("Shop with ID $shopId does not exist."))
                }
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

}
