package com.example.projet_de_stage.repository

import android.net.Uri
import com.example.projet_de_stage.data.Shop
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ShopRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("shops")

    fun createShopWithImage(
        shop: Shop,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val shopRef = collection.document(shop.id)

        if (imageUri == null) {
            // ما كيناش صورة
            shopRef.set(shop)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
            return
        }


    }

    suspend fun syncShopsWithDatabase(updatedShops: List<Shop>) {
        val snapshot = collection.get().await()
        val currentShops = snapshot.toObjects(Shop::class.java)

        val currentIds = currentShops.map { it.id }
        val updatedIds = updatedShops.map { it.id }

        // Shops to add
        val shopsToAdd = updatedShops.filter { it.id !in currentIds }

        // Shops to delete
        val shopsToDelete = currentShops.filter { it.id !in updatedIds }

        // Shops to update (if changed)
        val shopsToUpdate = updatedShops.filter { shop ->
            currentShops.any { it.id == shop.id && it != shop }
        }

        shopsToAdd.forEach { shop ->
            collection.document(shop.id).set(shop).await()
        }

        shopsToDelete.forEach { shop ->
            collection.document(shop.id).delete().await()
        }

        shopsToUpdate.forEach { shop ->
            collection.document(shop.id).set(shop).await()
        }
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
