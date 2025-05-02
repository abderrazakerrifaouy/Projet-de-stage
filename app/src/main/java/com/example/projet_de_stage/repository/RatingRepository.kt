package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Rating
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository for managing Rating operations with Firestore.
 */
class RatingRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("ratings")

    /**
     * Adds a new rating.
     */
    fun addRating(
        rating: Rating,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(rating.id).set(rating)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Gets a rating by its ID.
     */
    fun getRatingById(
        id: String,
        onSuccess: (Rating?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                onSuccess(doc.toObject(Rating::class.java))
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Updates an existing rating.
     */
    fun updateRating(
        rating: Rating,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(rating.id).set(rating)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Deletes a rating by its ID.
     */
    fun deleteRating(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Gets all ratings for a specific shop.
     */
    fun getRatingsByShop(
        shopId: String,
        onSuccess: (List<Rating>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.whereEqualTo("shopId", shopId) // <-- أصلحنا "chopId"
            .get()
            .addOnSuccessListener { qs ->
                onSuccess(qs.documents.mapNotNull { it.toObject(Rating::class.java) })
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
