package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Barber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class BarberRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("barbers")

    fun addBarber(
        barber: Barber,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(barber.uid).set(barber)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

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

    fun updateBarber(
        barber: Barber,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(barber.uid).set(barber)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteBarber(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }

            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getAllBarber(
        onSuccess: (List<Barber>) -> Unit,
        onFailure: (Exception) -> Unit
    ){
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
