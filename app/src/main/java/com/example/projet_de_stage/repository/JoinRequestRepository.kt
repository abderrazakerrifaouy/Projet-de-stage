package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.JoinRequest
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository class for managing JoinRequest data in Firestore.
 */
class JoinRequestRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("joinRequests")

    /**
     * Adds a new join request to Firestore.
     */
    fun addRequest(
        request: JoinRequest,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(request.id).set(request)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves a join request by its ID.
     */
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

    /**
     * Updates an existing join request.
     */
    fun updateRequest(
        request: JoinRequest,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(request.id).set(request)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Deletes a join request from Firestore.
     */
    fun deleteRequest(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves all join requests for a given shop owner.
     */
    fun getRequestsByShopOwnerId(
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

    /**
     * Updates the status of a specific join request.
     */
    fun updateRequestStatus(
        requestId: String,
        newStatus: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(requestId).update("status", newStatus)
            .addOnSuccessListener { onSuccess(true) }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
