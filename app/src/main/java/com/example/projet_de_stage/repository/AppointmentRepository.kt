package com.example.projet_de_stage.repository

import android.util.Log
import com.example.projet_de_stage.data.Appointment
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository that manages all appointment-related operations
 * using both Firestore and Firebase Realtime Database.
 */
class AppointmentRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val firestoreCollection = firestore.collection("appointments")

    private val realtimeDb = FirebaseDatabase.getInstance().reference
    private val realtimeAppointments = realtimeDb.child("appointments")

    /**
     * Adds a new appointment if no conflict exists (same barber, date, and time).
     */
    fun addAppointment(
        appointment: Appointment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("barberId", appointment.barberId)
            .whereEqualTo("date", appointment.date)
            .whereEqualTo("time", appointment.time)
            .get()
            .addOnSuccessListener {
                firestoreCollection.document(appointment.id)
                    .set(appointment)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }

                realtimeAppointments.child(appointment.id).setValue(appointment)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves all appointments made by a specific customer.
     */
    fun getAppointmentsByCustomerId(
        customerId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("clientId", customerId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    /**
     * Retrieves appointments by barber ID and status (e.g., pending, accepted).
     */
    fun getAllAppointmentsByBarberIdandStatus(
        status: String,
        barberId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("barberId", barberId)
            .whereEqualTo("status", status)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Failed to fetch appointments", exception)
                onFailure(exception)
            }
    }

    /**
     * Retrieves all appointments related to a specific barber regardless of status.
     */
    fun getAllAppointmentsByBarberId(
        barberId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("barberId", barberId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Failed to fetch appointments", exception)
                onFailure(exception)
            }
    }

    /**
     * Updates the status field of an appointment in both Firestore and Realtime Database.
     */
    fun updateAppointmentStatus(
        appointmentId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection.document(appointmentId)
            .update("status", newStatus)
            .addOnSuccessListener {
                realtimeAppointments.child(appointmentId).child("status").setValue(newStatus)
                onSuccess()
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Listens for new "pending" appointments for a specific barber in Realtime Database.
     */
    fun listenToNewAppointmentsForBarber(
        barberId: String,
        onNewAppointment: (Appointment) -> Unit,
        onError: (DatabaseError) -> Unit
    ) {
        val seenAppointments = mutableSetOf<String>()

        realtimeAppointments.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val appointment = snapshot.getValue(Appointment::class.java)
                if (appointment != null &&
                    appointment.barberId == barberId &&
                    appointment.status == "pending" &&
                    !seenAppointments.contains(appointment.id)
                ) {
                    seenAppointments.add(appointment.id)
                    onNewAppointment(appointment)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    /**
     * Deletes an appointment from the Realtime Database.
     */
    fun deleteAppointmentInRealtimeDatabase(
        appointmentId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        realtimeAppointments.child(appointmentId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure() }
    }

    /**
     * Retrieves all appointments for a specific shop.
     */
    fun getAppointmentsByShopId(
        shopId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("shopId", shopId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
