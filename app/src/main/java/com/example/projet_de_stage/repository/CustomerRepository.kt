package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Customer
import com.google.firebase.firestore.FirebaseFirestore

class CustomerRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("customers")

    fun addCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(customer.uid).set(customer)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getCustomerById(
        id: String,
        onSuccess: (Customer?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).get()
            .addOnSuccessListener { doc ->
                onSuccess(doc.toObject(Customer::class.java))
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(customer.uid).set(customer)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteCustomer(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
