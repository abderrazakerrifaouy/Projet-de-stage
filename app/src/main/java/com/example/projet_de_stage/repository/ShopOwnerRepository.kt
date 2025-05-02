package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.ShopOwner
import com.google.firebase.firestore.FirebaseFirestore

class ShopOwnerRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("shop_owners")

    fun addShopOwner(
        shopOwner: ShopOwner,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(shopOwner.uid).set(shopOwner)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getShopOwnerById(
        id: String,
        onSuccess: (ShopOwner?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                onSuccess(doc.toObject(ShopOwner::class.java))
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateShopOwner(
        shopOwner: ShopOwner,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(shopOwner.uid).set(shopOwner)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteShopOwner(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getAllShopOwners(
        onSuccess: (List<ShopOwner>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.get()
            .addOnSuccessListener { querySnapshot ->
                val owners = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(ShopOwner::class.java)
                }
                onSuccess(owners)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
