package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.JoinRequest
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository class for managing JoinRequest data in Firestore.
 * Provides methods to add, update, delete, and retrieve join requests.
 * @author Electro Anas
 * @version 1.0
 * @since 2023-07-01
 * @see JoinRequest
 * @see FirebaseFirestore
 * @constructor Creates an instance of JoinRequestRepository.
 * @property db The Firebase Firestore instance.
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
     * @param shopOwnerId The ID of the shop owner.
     * @param onSuccess Callback invoked on successful retrieval.
     * @param onFailure Callback invoked on failure.
     */
    fun getRequestsByShopOwnerId(
        shopOwnerId: String,
        onSuccess: (List<JoinRequest>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.whereEqualTo("shopOwnerId", shopOwnerId)
            .get()
            .addOnSuccessListener { qs ->
                onSuccess(qs.documents.mapNotNull { it.toObject(JoinRequest::class.java) })
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Updates the status of a specific join request.
     * @param requestId The ID of the join request to update.
     * @param newStatus The new status to set.
     * @param onSuccess Callback invoked on successful update.
     * @param onFailure Callback invoked on failure.
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
