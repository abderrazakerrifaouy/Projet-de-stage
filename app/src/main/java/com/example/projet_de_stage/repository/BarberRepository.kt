package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Barber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Repository responsible for handling CRUD operations for Barber entities.
 */
class BarberRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("barbers")

    /**
     * Adds a new barber to the Firestore database.
     */
    fun addBarber(
        barber: Barber,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(barber.uid).set(barber)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves a barber by their unique ID (UID).
     */
    fun getBarberById(
        id: String,
        onSuccess: (Barber?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                onSuccess(doc.toObject(Barber::class.java))
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Updates the details of an existing barber.
     */
    fun updateBarber(
        barber: Barber,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Boolean) -> Unit
    ) {
        collection.document(barber.uid).set(barber)
            .addOnSuccessListener { onSuccess(true) }
            .addOnFailureListener { onFailure(false) }
    }

    /**
     * Deletes a barber by their ID.
     */
    fun deleteBarber(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves all barbers sorted by name in ascending order.
     */
    fun getAllBarber(
        onSuccess: (List<Barber>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val barbers = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Barber::class.java)
                }
                onSuccess(barbers)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
