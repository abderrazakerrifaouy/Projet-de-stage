package com.example.projet_de_stage.repository


import com.example.projet_de_stage.data.Rating
import com.google.firebase.firestore.FirebaseFirestore

class RatingRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("ratings")

    fun addRating(
        rating: Rating,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(rating.id).set(rating)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

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

    fun updateRating(
        rating: Rating,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(rating.id).set(rating)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteRating(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getRatingsByShop(
        shopId: String,
        onSuccess: (List<Rating>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.whereEqualTo("chopId", shopId)
            .get()
            .addOnSuccessListener { qs ->
                onSuccess(qs.documents.mapNotNull { it.toObject(Rating::class.java) })
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
