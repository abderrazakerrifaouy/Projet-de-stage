package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.JoinRequest
import com.google.firebase.firestore.FirebaseFirestore

class JoinRequestRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("joinRequests")

    fun addRequest(
        request: JoinRequest,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(request.id).set(request)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getRequestById(
        id: String,
        onSuccess: (JoinRequest?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                onSuccess(doc.toObject(JoinRequest::class.java))
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateRequest(
        request: JoinRequest,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(request.id).set(request)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteRequest(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getRequestsByShopOwnerIdId(
        shopOwnerId: String,
        onSuccess: (List<JoinRequest>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.whereEqualTo("idShopOwner", shopOwnerId)
            .get()
            .addOnSuccessListener { qs ->
                onSuccess(qs.documents.mapNotNull { it.toObject(JoinRequest::class.java) })
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
