package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Customer
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository responsible for managing Customer-related operations
 * with Firestore such as create, read, update, and delete.
 */
class CustomerRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("customers")

    /**
     * Adds a new customer to Firestore.
     *
     * @param customer The customer to be added.
     * @param onSuccess Callback when the operation is successful.
     * @param onFailure Callback when the operation fails.
     */
    fun addCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(customer.uid).set(customer)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves a customer by their unique ID.
     *
     * @param id The unique ID of the customer.
     * @param onSuccess Callback returning the found customer or null.
     * @param onFailure Callback when the operation fails.
     */
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

    /**
     * Updates an existing customer's information.
     *
     * @param customer The updated customer object.
     * @param onSuccess Callback when the update is successful.
     * @param onFailure Callback when the update fails.
     */
    fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        collection.document(customer.uid).set(customer)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Deletes a customer by ID from Firestore.
     *
     * @param id The unique ID of the customer to be deleted.
     * @param onSuccess Callback when deletion is successful.
     * @param onFailure Callback when deletion fails.
     */
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
